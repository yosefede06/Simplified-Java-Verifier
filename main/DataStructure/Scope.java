package oop.ex6.main.DataStructure;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * The class Scope represents a scope in the program, which can be global or local.
 * It contains all the variables defined within the scope and its parent scopes.
 */
public class Scope {
    private final ArrayList<HashMap<String, Variable>> dataScopes = new ArrayList<>();
    private HashMap<String, Variable> globalVariables;

    /**
     * Constructor for the scope, receives the global scope map and the size of the scope chain.
     * @param globalVariables global scope map
     * @param sizeArrayList size of the scope chain
     */
    public Scope(HashMap<String, Variable> globalVariables, int sizeArrayList) {
        this.globalVariables = globalVariables;
        for(int i = 0; i < sizeArrayList; i++) {
            dataScopes.add(new HashMap<>());
        }
    }

    /**
     * This method updates the global map
     * @param copyMap new map to update our
     */
    public void updateGlobalMap(HashMap<String, Variable> copyMap) {
        globalVariables = copyMap;
    }

    /**
     * This method adds variable to the scope
     * @param assigned boolean indicating whether the variable has been assigned a value.
     * @param scope scope of the variable
     * @param nameVariable name of the variable
     * @param isFinal boolean if the variable is final or not
     * @param typeVariable type of the variable
     */
    public void addVariable(boolean assigned, int scope, String nameVariable, boolean isFinal, String typeVariable) {
        Variable newVariable = new Variable(assigned, isFinal, typeVariable, false, scope);
        dataScopes.get(scope).put(nameVariable, newVariable);
    }

    /**
     * This method gets a variable from the scope
     * @param allScopes list of scopes to be searched for the variable
     * @param nameVariable name of the variable
     * @return the variable if found, otherwise null
     */
    public Variable getVariable(ArrayList<Integer> allScopes, String nameVariable) {
        for (int j = allScopes.size() - 1; j >= 0; j--) {
            Variable variableInScope = dataScopes.get(allScopes.get(j)).get(nameVariable);
            if(variableInScope != null) {
                return variableInScope;
            }
        }
        return globalVariables.get(nameVariable);
    }
}
