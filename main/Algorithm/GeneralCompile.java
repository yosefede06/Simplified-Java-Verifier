package oop.ex6.main.Algorithm;
import oop.ex6.main.AllPattern;

abstract public class GeneralCompile extends AllPattern implements CompileAlgorithm {
    private static final String TRUE_STRING = "true";
    private static final String FALSE_STRING = "false";
    private static final String BOOLEAN_STRING = "boolean";
    private static final String INT_STRING = "int";
    private static final String DOUBLE_STRING = "double";
    /**
     * This method checks if the assigned value matches the name of a variable.
     * @param assignedValue the assigned value to check if it matches a variable name
     * @return true if the assigned value matches the name of a variable, false otherwise
     */
    public boolean isMatchesVariable(String assignedValue) {
        if(!assignedValue.equals(TRUE_STRING) && !assignedValue.equals(FALSE_STRING)) {
            return nameVariablePattern.matcher(assignedValue).matches();
        }
        return false;
    }

    /**
     * This method checks if two types are matching.
     * @param type1 the first type to check
     * @param type2 the second type to check
     * @return true if the two types are matching, false otherwise
     */
    public boolean checkMatchingTypes(String type1, String type2) {
        if(type1.equals(type2)) {
            return true;
        }
        if(type1.equals(BOOLEAN_STRING) && (type2.equals(INT_STRING) || type2.equals(DOUBLE_STRING))) {
            return true;
        }
        return type1.equals(DOUBLE_STRING) && type2.equals(INT_STRING);
    }
}
