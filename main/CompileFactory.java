package oop.ex6.main;

import oop.ex6.main.Algorithm.*;
import oop.ex6.main.DataStructure.Code;
import oop.ex6.main.DataStructure.GlobalDataStructure;
import oop.ex6.main.DataStructure.LocalDataStructure;

import java.util.HashMap;
import java.util.function.Supplier;

/**
 * CompileFactory class responsible for creating instances of different CompileAlgorithm objects.
 */
public class CompileFactory {
    private static final String COMMENTS = "comments";
    private static final String METHOD = "method";
    private static final String GLOBAL = "global";
    private static final String LOCAL = "local";

    private final HashMap<String, String[]> methodMap;
    private final GlobalDataStructure globalDataStructure;
    private final LocalDataStructure localDataStructure;
    private final Supplier<Integer> currLocalScope;
    private final Code allCode;

    /**
     * Constructor of CompileFactory
     * @param methodMap a HashMap that stores all methods and their parameters as strings
     * @param globalDataStructure the data structure that holds all global variables
     * @param localDataStructure the data structure that holds all local variables
     * @param currLocalScope  the current local scope number
     * @param allCode the code of the program in string form
     */
    public CompileFactory (HashMap<String, String[]> methodMap,
                           GlobalDataStructure globalDataStructure,
                           LocalDataStructure localDataStructure,
                           Supplier<Integer> currLocalScope,
                           Code allCode) {

        this.methodMap = methodMap;
        this.globalDataStructure = globalDataStructure;
        this.localDataStructure = localDataStructure;
        this.currLocalScope = currLocalScope;
        this.allCode = allCode;
    }

    /**
     * Method to create an instance of the desired CompileAlgorithm based on the compileName parameter.
     * @param compileName the name of the algorithm to be created
     * @return an instance of the desired CompileAlgorithm
     */
    public CompileAlgorithm createFactory (String compileName) {
        switch (compileName) {
            case COMMENTS:
                return  new CompileCommentsAlgorithm();
            case METHOD:
                return new CompileMethodAlgorithm(localDataStructure, allCode);
            case GLOBAL:
                return new CompileGlobalAlgorithm(globalDataStructure);
            case LOCAL:
                return new CompileLocalAlgorithm(currLocalScope, localDataStructure);
            default:
                return null;
        }
    }
}
