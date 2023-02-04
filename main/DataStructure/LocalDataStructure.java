package oop.ex6.main.DataStructure;

import java.util.ArrayList;
import java.util.HashMap;
/**
 * The class LocalDataStructure is a data structure used to store the local scopes.
 */
public class LocalDataStructure {
    public HashMap<String, String[]> methodMap;
    public ArrayList<ArrayList<Integer>> algoScope;
    public ArrayList<String> codeScope;
    public Scope scope;

    /**
     * Constructor for the LocalDataStructure
     * @param methodMap map of methods
     */
    public LocalDataStructure (HashMap<String, String[]> methodMap) {
        this.methodMap = methodMap;
        algoScope = new ArrayList<>();
        codeScope = new ArrayList<>();
    }

    /**
     * Initialize a new scope with the given global variables
     * @param dataGlobalVariables global variable to include in the new scope
     */
    public void initializeScope(HashMap<String, Variable> dataGlobalVariables) {
        scope = new Scope(dataGlobalVariables, algoScope.size());
    }
}
