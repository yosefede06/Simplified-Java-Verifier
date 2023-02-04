package oop.ex6.main.Algorithm;

import oop.ex6.main.DataStructure.Code;
import oop.ex6.main.DataStructure.LocalDataStructure;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The CompileMethodAlgorithm class extends the GeneralCompile class and is responsible for compiling
 * methods in S-Java code.
 */
public class CompileMethodAlgorithm extends GeneralCompile {
    private int methodInd = 0;
    private final LocalDataStructure localData;
    private Code allCode;

    /**
     * Constructor of CompileMethodAlgorithm
     * @param localData The LocalDataStructure object containing the current program's variables and methods.
     * @param allCode The Code object containing the entire program's code
     */
    public CompileMethodAlgorithm (LocalDataStructure localData, Code allCode) {
        this.localData = localData;
        this.allCode = allCode;
    }

    /**
     * This method checks if a closure is legal
     * @param s closure to check
     * @return true if the closure is legal, false otherwise
     */
    boolean checkLegalClosure(String s) {
        return Pattern.compile("\\s*}\\s*").matcher(s).matches();
    }

    /**
     * This method compiles the code by extracting all methods and checking if they are properly structured.
     * @param whatever a parameter that is not used in the implementation
     * @return true if all methods are properly structured, false otherwise
     */
    @Override
    public boolean compile(String whatever) {
        Matcher methodLocation = structureMethodPattern.matcher(allCode.code);
        int saveStart = 0;
        int endMethod = 0;
        while(methodLocation.find()) {
            int startMethod = methodLocation.start();
            endMethod = methodLocation.end();
            String fullMethod = allCode.code.substring(startMethod, endMethod);
            if(!cleanMethod(fullMethod)) {
                return false;
            }
            if(!saveFullMethod(fullMethod)) {
                return false;
            }
            allCode.codeOutOfMethod += allCode.code.substring(saveStart, startMethod);
            saveStart = endMethod;
        }
        allCode.codeOutOfMethod += allCode.code.substring(endMethod);
        return true;
    }

    /**
     * This method extracts arguments from a given method as a string
     * @param method the full string representation of a method
     * @return an array of strings representing the arguments of the method
     */
    private String[] getArgsFromMethod(String method) {
        return method.replaceFirst("[\\s\\S]*?[(]\\s*", "")
                .replaceFirst("\\s*[)][\\s\\S]*", "")
                .split("\\s*,\\s*");
    }

    /**
     * This method saves a full method in map
     * @param fullMethod full method to be saved
     * @return true if the method is successfully saved, false otherwise
     */
    private boolean saveFullMethod(String fullMethod) {
        fullMethod = fullMethod.replaceAll("void\\s*", "");
        String methodName = fullMethod.replaceAll("\\s*[(][\\s\\S]*", "");
        if(localData.methodMap.containsKey(methodName)) return false;
        String[] methodArgs = getArgsFromMethod(fullMethod);
        if(methodArgs[0].equals("")) {
            methodArgs = new String[0];
        }
        String[] methodArgsType = new String[methodArgs.length];
        for(int i = 0; i < methodArgs.length; i++) {
            String[] argument = methodArgs[i].split(" ");
            methodArgsType[i] = argument[0];
        }
        localData.methodMap.put(methodName,methodArgsType);
        return true;
    }

    /**
     * This method cleans the method code
     * @param methodCode method code to check and clean
     * @return true if all the check pass, false otherwise
     */
    private boolean cleanMethod(String methodCode) {
        int countOpened = countFindIfWhile(methodCode);
        boolean response = this.checkClosureIfWhile(methodCode, countOpened + 1);
        if(response) {
            methodCode = methodCode.replaceAll(IF_WHILE_CONDITION, "").
                    replaceAll("[}]", "") + "}";
            return fullMethodRegexPattern.matcher(methodCode).matches();
        }
        return false;
    }

    /**
     * This method returns the number of if or while in the given file line
     * @param line line of code
     * @return number of if or while
     */
    private int countFindIfWhile(String line) {
        Matcher m = ifWhileRegexPattern.matcher(line);
        int count = 0;
        while(m.find()) {
            count++;
        }
        return count;
    }

    /**
     * This method retrieves all arguments present in the given input based on a delimiter
     * @param input The string containing the input.
     * @param delimiter The string defining the delimiter for separating the arguments.
     * @return  An array of strings that represent the arguments.
     */
    String[] retrieveAllArguments(String input, String delimiter) {
        String regex = "\\s*\\(([^\\)]*)\\)\\s*";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);
        String[] arguments = new String[0];
        if (matcher.find()) {
            String matchedString = matcher.group(1);
            if(matchedString.trim().equals("")) {
                return arguments;
            }
            arguments = matchedString.split(delimiter);
            for (int i = 0; i < arguments.length; i++) {
                arguments[i] = arguments[i].trim();
            }

            return arguments;
        }
        return arguments;
    }

    /**
     * This method checks if the code inside the method is well-formed.
     * It tracks the count of opened and closed brackets, and stores the code and algorithm scope accordingly.
     * @param codeInsideTheMethod  The code inside the method to be checked for closure of
     *                             if/while statements.
     * @param openBrackets The count of opened brackets in the code.
     * @return true if the closure of if/while statements is done correctly, false otherwise.
     */
    private boolean checkClosureIfWhile(String codeInsideTheMethod, int openBrackets) {
        int countOpened = 0, countBalance = 0;
        int valueToUp = 0;
        ArrayList<Integer> globalCount = new ArrayList<>();
        String currCodeScope = "";
        String[] allLines = codeInsideTheMethod.split("\\n");
        boolean closureIndicator = false;
        for(String line : allLines) {
            if(countBalance > 0 && line.contains("{") || line.contains("}")) {
                localData.codeScope.add(currCodeScope);
                ArrayList<Integer> copyList = deepCopy(globalCount);
                if(copyList.size() > 0) copyList.set(0, methodInd);
                localData.algoScope.add(copyList);
                currCodeScope = "";
            }
            if(line.contains("{")) {
                if(line.contains("void")) {
                    String[] argumentsRetrievedCode = retrieveAllArguments(line, ",");
                    if(argumentsRetrievedCode.length > 0 && !argumentsRetrievedCode[0].equals("")) {
                        for (int i = 0; i < argumentsRetrievedCode.length; i++) {
                            String[] tempArgs = argumentsRetrievedCode[i].split("\\s+");
                            if(tempArgs.length != 2) {
                                return false;
                            }
                            // Add ";" at the end of each element
                            argumentsRetrievedCode[i] = initializeVariableWithSomeValue(tempArgs[0], tempArgs[1]) + ";";
                        }
                        String addToScope = String.join("\n", argumentsRetrievedCode);
                        currCodeScope += addToScope;
                    }
                }
                else {
                    String[] argumentsRetrievedCode = retrieveAllArguments(line, "&{2}|[|]{2}");
                    if(argumentsRetrievedCode.length > 0 && !argumentsRetrievedCode[0].equals("")) {
                        for (String arg : argumentsRetrievedCode) {
                            if (!booleanRegexConstantPattern.matcher(arg).matches()) {
                                String addToScope = "boolean " + arg + " = " + arg + ";\n";
                                currCodeScope += addToScope;
                            }

                        }
                    }
                }
                if(closureIndicator) {
                    globalCount.add(0);
                    globalCount.set(countBalance, valueToUp + 1);
                }
                else {
                    globalCount.add(0);
                }
                countOpened++;
                countBalance++;
                closureIndicator = false;
            }
            else if(line.contains("}")) {
                if(!checkLegalClosure(line)) {
                    return false;
                }
                if(countBalance - 1 >= 0) {
                    valueToUp = globalCount.get(countBalance - 1);
                    globalCount.remove(countBalance - 1);
                }
                countBalance--;
                closureIndicator = true;
            }

            else {
                currCodeScope += line;
            }
            if(countBalance < 0) {
                return false;
            }
        }
        if(!(countBalance==0)) {
            return false;
        }
        methodInd++;
        return countOpened == openBrackets;
    }

    /**
     * A helper method that initializes a variable with a default value based on its type
     * @param sType the type of the variable
     * @param sValue the name of the variable
     * @return the variable initialized with a default value
     */
    private String initializeVariableWithSomeValue(String sType, String sValue) {
        String sInitialized = sType + " " + sValue + " = ";
        if(sType.equals("String")) {
            return sInitialized + "\"initialized\"";
        }
        if(sType.equals("char")) {
            return sInitialized + "'a'";
        }
        return sInitialized + "3";
    }

    /**
     * This method creates a deep copy of the input list.
     * @param fromCopyList The list to be copied.
     * @return A new list with the same elements as the input list.
     */
    private ArrayList<Integer> deepCopy(ArrayList<Integer> fromCopyList) {
        return new ArrayList<>(fromCopyList);
    }
}
