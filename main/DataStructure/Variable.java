package oop.ex6.main.DataStructure;

import java.util.ArrayList;

/**
 * Variable class that represents a variable.
 */
public class Variable {
    private final String type;
    private boolean assigned;
    private final boolean isFinal;
    protected boolean isGlobal;
    protected int scopePlace;

    /**
     Constructor for creating a new Variable object.
     @param assigned - boolean representing whether the variable has been assigned a value
     @param isFinal - boolean representing whether the variable is final or not
     @param type - String representing the variable's data type
     */
    public Variable(boolean assigned, boolean isFinal, String type, boolean isGlobal, int scopePlace) {
        this.assigned = assigned;
        this.isFinal = isFinal;
        this.type = type;
        this.isGlobal = isGlobal;
        this.scopePlace = scopePlace;
    }
    /**
     Checks if the variable definition is legal by comparing the scope of the variable with the given scopes.
     @param integers - the scopes of all variables with the same name.
     @param algoScope - the scope of the current variable.
     @param ind - the current index in the algoScope list.
     @return - true if the variable definition is legal, false otherwise.
     */
    public boolean isLegalDefinition(ArrayList<Integer> integers, ArrayList<ArrayList<Integer>>
            algoScope, int ind) {
        if(!isGlobal()) {
            for(int i = 0; i < integers.size(); i++) {
                if((i != 0 || integers.size() == 1) &&
                        algoScope.get(integers.get(i)).equals(algoScope.get(ind))) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * This method returns if the variable is global
     * @return boolean indicate if yes or not
     */
    public boolean isGlobal() {
        return isGlobal;
    }

    /**
     Getter method for the variable's data type
     @return - the variable's data type as a String
     */
    public String getType() {
        return type;
    }

    /**
     Setter method for the 'assigned' field, sets it to true
     */
    public void setAssignment() {
        assigned = true;
    }

    /**
     Getter method for the 'assigned' field
     @return - boolean representing whether the variable has been assigned a value
     */
    public boolean isAssigned() {
        return assigned;
    }

    /**
     Getter method for the 'isFinal' field
     @return - boolean representing whether the variable is final or not
     */
    public boolean isFinal() {
        return isFinal;
    }
}
