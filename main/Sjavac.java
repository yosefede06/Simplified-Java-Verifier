package oop.ex6.main;

import oop.ex6.main.Algorithm.*;
import oop.ex6.main.DataStructure.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * This is the class Sjavac that is responsible for checks the code.
 */
public class Sjavac extends AllPattern {
    /**
     * Handle errors
     */
    private static final int LEGAL_CODE = 0;
    private static final int ILLEGAL_CODE = 1;
    private static final int ERROR_MESSAGE = 2;
    private static final String ARGUMENT_ERROR = "Error! The number of arguments should be of length 1";
    private static final String ERROR_OPENING_FILE = "Error: Error opening file";
    private final Code allCode = new Code("", "");
    private final boolean isException;
    private final HashMap<String, String[]> methodMap = new HashMap<>();
    private int currLocalScope = 0;
    GlobalDataStructure globalDataStructure = new GlobalDataStructure(methodMap);
    LocalDataStructure localDataStructure = new LocalDataStructure(methodMap);
    CompileAlgorithm compileGlobalAlgorithm;
    CompileAlgorithm compileLocalAlgorithm;
    CompileAlgorithm compileMethodAlgorithm;
    CompileAlgorithm compileCommentsAlgorithm;

    /**
     * This is the main method of the program.
     * It creates an instance of Sjavac with the given arguments and checks if there is an Exception.
     * If there isn't an exception, it calls the compileCode() method.
     * @param args the command line arguments passed to the program
     */
    public static void main(String[] args) {
        Sjavac sjavac = new Sjavac(args);
        if(!sjavac.gotException()) {
            sjavac.compileCode();
        }
    }

    /**
     * The Sjavac constructor takes in a String array as an argument
     * which represents the command line arguments.
     * @param args the command line arguments passed to the program
     */
    public Sjavac(String[] args) {
        super();
        isException = !readFile(args);

    }

    /**
     * This method performs various checks on the code to determine if it is a valid Java program.
     * It's the verification of all the code.
     * It checks for comments using a regex and removes them.
     * It then checks the structure of the code, including the methods and global variables.
     * If all checks pass, it returns true, otherwise it returns false.
     * @return boolean indicating if the code is a valid Java program
     */
    private boolean checkFullCode() {
        // first we check regex for comments
        if (compileCommentsAlgorithm.compile(allCode.code)) {
            //here we clean comments lines
            allCode.code = allCode.code.replaceAll(REPLACE_COMMENTS_REGEX, "");
            if (compileMethodAlgorithm.compile("") &&
                    codeInsideBracketsAndGlobalPatternNotMethods.matcher(allCode.codeOutOfMethod).matches()){
                if (!compileGlobalAlgorithm.compile(allCode.codeOutOfMethod)) {
                    return false;
                }
                localDataStructure.initializeScope(globalDataStructure.dataGlobalVariables);
                HashMap<String, Variable> copyGlobalVariables =
                        DeepCopyHashMap.deepCopy(globalDataStructure.dataGlobalVariables);
                int currIndicatorMethod = 0;
                for(int k = 0; k < localDataStructure.algoScope.size(); k++) {
                    String currScope = localDataStructure.codeScope.get(k);
                    if(currIndicatorMethod != localDataStructure.algoScope.get(k).get(0)) {
                        globalDataStructure.dataGlobalVariables =
                                DeepCopyHashMap.deepCopy(copyGlobalVariables);
                        localDataStructure.scope.updateGlobalMap(globalDataStructure.dataGlobalVariables);
                        currIndicatorMethod++;
                    }
                    currLocalScope = k;
                    if(!compileLocalAlgorithm.compile(currScope.replaceAll("return\\s*;", ""))) {
                        return false;
                    }
                }
            } else {
                return false;
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * This method used to compile the code. It creates instances of compile factory, compiles
     * global, local, method, and comment algorithms, and checks if the code is legal.
     * If the code is legal, it prints LEGAL_CODE,otherwise, it prints ILLEGAL_CODE.
     */
    private void compileCode() {
        CompileFactory compileFactory = new CompileFactory(methodMap, globalDataStructure, localDataStructure,
                () -> currLocalScope, allCode);
        compileGlobalAlgorithm = compileFactory.createFactory("global");
        compileLocalAlgorithm = compileFactory.createFactory("local");
        compileMethodAlgorithm = compileFactory.createFactory("method");
        compileCommentsAlgorithm = compileFactory.createFactory("comments");
        if (checkFullCode()) {
            System.out.println(LEGAL_CODE);
        } else {
            System.out.println(ILLEGAL_CODE);
        }
    }

    /**
     * This method read a file, checks the arguments and if there are good, open the file.
     * @param args the arguments passed to the method
     * @return true if the file was read correctly, false otherwise
     */
    private boolean readFile(String[] args) {
        return checkArgs(args) && openFile(args[0]);
    }

    /**
     * This method opens the file and reads its contents.
     * If there's an error opening the file, it prints ERROR_MESSAGE and ERROR_OPENING_FILE.
     * @param nameFile the name of the file to be opened
     * @return true if the file was opened and read correctly, false otherwise
     */
    private boolean openFile(String nameFile) {
        String line;
        try (FileReader fileReader = new FileReader(nameFile);
             BufferedReader bufferedReader = new BufferedReader(fileReader)) {
            while ((line = bufferedReader.readLine()) != null) {
                allCode.code += (line + "\n");
            }
            /* READ FROM THE FILE */
        } catch (IOException e) {
            System.out.println(ERROR_MESSAGE);
            System.err.println(ERROR_OPENING_FILE);
            return false;
        }
        return true;
    }

    /**
     * This method checks if the arguments passed to the method are correct.
     * If the number of arguments is not equal to 1, it throws an IOException and prints ERROR_MESSAGE and
     * ARGUMENT_ERROR.
     * @param args the arguments passed to the method
     * @return true if the arguments are correct, false otherwise
     */
    private boolean checkArgs(String[] args) {
        try {
            if (args.length != 1) {
                throw new IOException();
            }
        } catch (IOException ioException) {
            System.out.println(ERROR_MESSAGE);
            System.err.println(ARGUMENT_ERROR);
            return false;
        }
        return true;
    }

    /**
     * This method returns the value of isException
     * @return the value of isException
     */
    public boolean gotException() {
        return isException;
    }


}
