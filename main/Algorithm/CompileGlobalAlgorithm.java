package oop.ex6.main.Algorithm;

import oop.ex6.main.DataStructure.GlobalDataStructure;
import oop.ex6.main.DataStructure.Variable;

import java.util.regex.Matcher;

/**
 * CompileGlobalAlgorithm is a subclass of GeneralCompile and is used to compile
 * global variables and data structures.
 */
public class CompileGlobalAlgorithm extends GeneralCompile {
    private final GlobalDataStructure globalData;
    private static final String TRUE_STRING = "true";
    private static final String FALSE_STRING = "false";
    private static final String BOOLEAN_STRING = "boolean";
    private static final String FINAL_REGEX = "final";
    private static final String REGEX_SPACE = "\\s*;\\s*";

    /**
     * Constructor that initializes the GlobalDataStructure object.
     * @param globalData GlobalDataStructure object to be initialized.
     */
    public CompileGlobalAlgorithm (GlobalDataStructure globalData) {
        this.globalData = globalData;
    }

    /**
     * Overridden compile method from the GeneralCompile class.
     * Matches the code for global variables and data structures and calls the
     * compileHelper method to compile them.
     * @param codeAnalyze Code to be compiled.
     * @return Boolean value indicating the success or failure of the compilation process.
     */
    @Override
    public boolean compile(String codeAnalyze) {
        Matcher globalVariablesMatcher = structureVariablePattern.matcher(codeAnalyze);
        while(globalVariablesMatcher.find()) {
            String currCode = codeAnalyze.substring(globalVariablesMatcher.start(),
                    globalVariablesMatcher.end());
            if(!compileHelper(currCode.replaceAll(REGEX_SPACE, ""))) {
                return false;
            }
        }
        return true;
    }

    /**
     * This method is used to compile the given variable statement and check its validity.
     * @param variable : The string input representing the variable statement.
     * @return boolean : Returns 'true' if the statement is valid, 'false' otherwise.
     */
    private boolean compileHelper(String variable) {
        boolean isFinal = false;
        String firstTypeVariable = variable.split("\\s+")[0];
        if(firstTypeVariable.matches(FINAL_REGEX)) {
            isFinal = true;
            firstTypeVariable = variable.split("\\s+")[1];
        }
        if(possibleTypesPattern.matcher(firstTypeVariable).matches()) {
            String cleanVariable = variable.replaceFirst("(final\\s+)?" + POSSIBLE_TYPES + "\\s*",
                    "");
            String[] listVariables = cleanVariable.split("\\s*,\\s*");
            for (String listVariable : listVariables) {
                if (listVariable.contains("=")) {
                    String[] assignedVariable = listVariable.split("\\s*=\\s*");
                    String newVariable = assignedVariable[0];
                    String assignedValue = assignedVariable[1];
                    Variable checkNewVariable = globalData.dataGlobalVariables.get(newVariable);
                    // condition to check valid assignment.
                    if (isMatchesVariable(assignedValue)) {
                        Variable checkAssigned = globalData.dataGlobalVariables.get(assignedValue);
                        if (checkAssigned == null ||
                                !checkAssigned.isAssigned() ||
                                (checkAssigned.isAssigned() && !checkMatchingTypes(firstTypeVariable,
                                        checkAssigned.getType()))) {
                            return false;
                        }

                    }
                    if ((assignedValue.equals(TRUE_STRING) || assignedValue.equals(FALSE_STRING)) &&
                            !firstTypeVariable.equals(BOOLEAN_STRING)) {
                        return false;
                    }
                    if (checkNewVariable != null) {
                        return false;
                    }
                    globalData.dataGlobalVariables.put(newVariable,
                            new Variable(true, isFinal, firstTypeVariable, true, -1));
                } else {
                    if (globalData.dataGlobalVariables.containsKey(listVariable)) {
                        return false;
                    }
                    globalData.dataGlobalVariables.put(listVariable,
                            new Variable(false, isFinal, firstTypeVariable, true, -1));
                }
            }
        }
        else if (variable.contains("=")) {
            String[] listVariables = variable.split("\\s*,\\s*");
            for (String listVariable : listVariables) {
                String[] assignedVariable = listVariable.split("\\s*=\\s*");
                String newVariable = assignedVariable[0];
                String assignedValue = assignedVariable[1];
                Variable checkNewVariable = globalData.dataGlobalVariables.get(newVariable);
                if (checkNewVariable == null || checkNewVariable.isFinal()) {
                    return false;
                }
                if (isMatchesVariable(assignedValue)) {
                    Variable checkAssigned = globalData.dataGlobalVariables.get(assignedValue);
                    if (checkAssigned == null ||
                            !checkAssigned.isAssigned() ||
                            (checkAssigned.isAssigned() && !checkMatchingTypes(checkNewVariable.getType(),
                                    checkAssigned.getType()))) {
                        return false;

                    }
                } else {
                    if (!variablePattern.matcher
                            (checkNewVariable.getType() + " " + variable + ";").matches()) {
                        return false;
                    }
                }
                if ((assignedValue.equals(TRUE_STRING) || assignedValue.equals(FALSE_STRING)) &&
                        !checkNewVariable.getType().equals(BOOLEAN_STRING)) {
                    return false;
                }
                checkNewVariable.setAssignment();
            }
        }
        //method calls
        else {
            String nameOfMethod = variable.replaceFirst("\\s*[(].*", "");
            String[] methodParameters = globalData.methodMap.get(nameOfMethod);
            // first check if method is defined methodParameters != null
            if(methodParameters == null) {
                return false;
            }
            String[] parametersOfCallMethod = getArgsFromMethod(variable);
            if(parametersOfCallMethod.length != methodParameters.length) {
                return false;
            }
            for(int j = 0; j < parametersOfCallMethod.length; j++) {
                if(!variablePattern.matcher(methodParameters[j] + " test = " +
                        parametersOfCallMethod[j] + ";").matches()) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * This method extracts arguments from a given method signature and returns them as an array of strings.
     * @param method The method signature as a string.
     * @return The arguments of the method as an array of strings.
     */
    private String[] getArgsFromMethod(String method) {
        return method.replaceFirst("[\\s\\S]*?[(]\\s*", "")
                .replaceFirst("\\s*[)][\\s\\S]*", "")
                .split("\\s*,\\s*");
    }
}
