package oop.ex6.main.DataStructure;

import java.util.HashMap;

/**
 * The class GlobalDataStructure is a data structure used to store the global scope
 */
public class GlobalDataStructure {
    public HashMap<String, String[]> methodMap;
    public HashMap<String, Variable> dataGlobalVariables;

    /**
     * Constructor of GlobalDataStructure
     * @param methodMap Hashmap of methods.
     */
    public GlobalDataStructure (HashMap<String, String[]> methodMap) {
        methodMap = methodMap;
        dataGlobalVariables = new HashMap<>();
    }
}
