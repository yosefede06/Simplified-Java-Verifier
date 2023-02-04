package oop.ex6.main.Algorithm;

import oop.ex6.main.DataStructure.LocalDataStructure;
import oop.ex6.main.DataStructure.Variable;

import java.util.ArrayList;
import java.util.function.Supplier;
import java.util.regex.Matcher;
/**
 * CompileLocalAlgorithm is a subclass of GeneralCompile and is used to compile
 * local variables and data structures.
 */
public class CompileLocalAlgorithm extends GeneralCompile {
    private final Supplier<Integer> getCurrScope;
    private final LocalDataStructure localData;

    /**
     * The constructor for CompileLocalAlgorithm that sets the value of the current scope and local data.
     * @param getCurrScope supplier functional interface to get the current scope
     * @param localData LocalDataStructure to store local data.
     */
    public CompileLocalAlgorithm (Supplier<Integer> getCurrScope, LocalDataStructure localData) {
        this.getCurrScope = getCurrScope;
        this.localData = localData;
    }

    /**
     * Overrides the compile method in GeneralCompile to compile the code for local scoped variables.
     * @param codeAnalyze The code to be compiled for local scoped variables.
     * @return True if the compilation was successful, False otherwise.
     */
    @Override
    public boolean compile(String codeAnalyze) {
        Matcher globalVariablesMatcher = structureVariablePattern.matcher(codeAnalyze);
        while(globalVariablesMatcher.find()) {
            String currCode = codeAnalyze.substring(globalVariablesMatcher.start(),
                    globalVariablesMatcher.end());
            if(!compileHelper(
                    currCode.replaceAll("\\s*;\\s*", ""),
                    superMethodCheckScope(getCurrScope.get()),
                    getCurrScope.get())) {
                return false;
            }
        }
        return true;
    }

    /**
     * A helper function for compile() that performs checks for variables and method calls
     * @param variable a string containing information about the variable or method call
     * @param integers a list of integers representing the scope of the variable or method call
     * @param ind the index of the variable or method call in the list of code statements
     * @return a boolean indicating whether the variable or method call is valid
     */
    private boolean compileHelper(String variable, ArrayList<Integer> integers, int ind) {
        boolean isFinal = false;
        String firstTypeVariable = variable.split("\\s+")[0];
        if(firstTypeVariable.matches("final")) {
            isFinal = true;
            firstTypeVariable = variable.split("\\s+")[1];
        }
        if(variable.contains("return")) {
            return returnPattern.matcher(variable).matches();
        }
        else if(possibleTypesPattern.matcher(firstTypeVariable).matches()) {
            String cleanVariable = variable.replaceFirst("(final\\s+)?" +
                    POSSIBLE_TYPES + "\\s*", "");
            String[] listVariables = cleanVariable.split("\\s*,\\s*");
            for (String listVariable : listVariables) {
                if (listVariable.contains("=")) {
                    String[] assignedVariable = listVariable.split("\\s*=\\s*");
                    String newVariable = assignedVariable[0];
                    String assignedValue = assignedVariable[1];
                    Variable retrieveNewVariable = localData.scope.getVariable(integers, newVariable);
                    // condition to check valid assignment.
                    if (isMatchesVariable(assignedValue)) {
                        Variable retrieveAssignedVariable = localData.scope.getVariable(integers,
                                assignedValue);
                        if (retrieveAssignedVariable == null ||
                                !retrieveAssignedVariable.isAssigned() ||
                                (retrieveAssignedVariable.isAssigned() &&
                                        !checkMatchingTypes(firstTypeVariable,
                                        retrieveAssignedVariable.getType()))) {
                            return false;
                        }
                    }
                    if ((assignedValue.equals("true") || assignedValue.equals("false")) &&
                            !firstTypeVariable.equals("boolean")) {
                        return false;
                    }
                    if (retrieveNewVariable != null && retrieveNewVariable.isLegalDefinition(integers,
                            localData.algoScope, ind)) {
                        return false;
                    }
                    localData.scope.addVariable(true, ind, newVariable, isFinal, firstTypeVariable);
                } else {
                    Variable currLocalCheck = localData.scope.getVariable(integers, listVariable);
                    if (currLocalCheck != null && currLocalCheck.isLegalDefinition(integers,
                            localData.algoScope, ind)) {
                        return false;
                    }
                    localData.scope.addVariable(false, ind, listVariable, isFinal, firstTypeVariable);
                }
            }
        }
        else if (variable.contains("=")) {
            String[] listVariables = variable.split("\\s*,\\s*");
            for (String listVariable : listVariables) {
                String[] assignedVariable = listVariable.split("\\s*=\\s*");
                String newVariable = assignedVariable[0];
                String assignedValue = assignedVariable[1];
                Variable retrieveNewVariable = localData.scope.getVariable(integers, newVariable);
                if (retrieveNewVariable == null || retrieveNewVariable.isFinal()) {
                    return false;
                }
                retrieveNewVariable.setAssignment();
                if (isMatchesVariable(assignedValue)) {
                    Variable checkAssigned = localData.scope.getVariable(integers, assignedValue);
                    if (checkAssigned == null ||
                            !checkAssigned.isAssigned() ||
                            (checkAssigned.isAssigned() && !checkMatchingTypes(retrieveNewVariable.getType(),
                                    checkAssigned.getType()))) {
                        return false;
                    }
                } else {
                    if (!variablePattern.matcher(retrieveNewVariable.getType() + " "
                            + variable + ";").matches()) {
                        return false;
                    }
                }
                if ((assignedValue.equals("true") || assignedValue.equals("false")) &&
                        !retrieveNewVariable.getType().equals("boolean")) {
                    return false;
                }
            }
        }
        //method calls
        else {
            String nameOfMethod = variable.replaceFirst("\\s*[(].*", "");
            String[] methodParameters = localData.methodMap.get(nameOfMethod);
            // first check if method is defined methodParameters != null
            if(methodParameters == null) {
                return false;
            }
            String[] parametersOfCallMethod = getArgsFromMethod(variable);
            //check no parameters
            if(parametersOfCallMethod.length > 0 && parametersOfCallMethod[0].equals("")) {
                return methodParameters.length == 0;
            }
            if(parametersOfCallMethod.length != methodParameters.length) {
                return false;
            }
            for(int j = 0; j < parametersOfCallMethod.length; j++) {
                Variable parameterVariable = localData.scope.getVariable(integers, parametersOfCallMethod[j]);
                if(!isMatchesVariable(parametersOfCallMethod[j]) || parameterVariable != null
                        && parameterVariable.isAssigned()) {
                    if(!variablePattern.matcher(methodParameters[j] + " test = "
                            + parametersOfCallMethod[j] + ";").matches()) {
                        return false;
                    }
                }
                else {
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

    /**
     * method to check the scope of a super method
     * @param i the index of the current method in the local data
     * @return  an ArrayList of integers, each representing the index of a method in the local data
     * that matches the current method's scope
     */
    private ArrayList<Integer> superMethodCheckScope(int i) {
        ArrayList<Integer> res = new ArrayList<>();
        res.add(i);
        ArrayList<Integer> currentAlgoScope = localData.algoScope.get(i);
        for(int j = 0; j < i; j++) {
            if(checkMatchScope(localData.algoScope.get(j), currentAlgoScope)) {
                res.add(j);
            }
        }
        return res;
    }

    /**
     * This method checks if two scopes match
     * @param outScope the outer scope to be compared
     * @param inScope the inner scope to be compared
     * @return a boolean value indicating whether the two scopes match
     */
    private boolean checkMatchScope(ArrayList<Integer> outScope, ArrayList<Integer> inScope) {
        if(inScope.size() < outScope.size()) {
            return false;
        }
        for(int i = 0; i < outScope.size(); i++) {
            if(!(outScope.get(i).equals(inScope.get(i)))) {
                return false;
            }
        }
        return true;
    }
}
