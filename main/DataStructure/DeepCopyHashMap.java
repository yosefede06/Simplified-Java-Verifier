package oop.ex6.main.DataStructure;

import java.util.HashMap;

/**
 * This class is responsible for creating a deep copy of a given HashMap
 */
public class DeepCopyHashMap {
    /**
     * The method takes an original HashMap of variables and returns a deep copy of it.
     * @param original the original HashMap of variables
     * @return a deep copy of the original HashMap
     */
    public static HashMap<String, Variable> deepCopy(HashMap<String, Variable> original) {
        HashMap<String, Variable> copy = new HashMap<>();
        for (String key : original.keySet()) {
            Variable originalVariable = original.get(key);
            copy.put(key, new Variable(originalVariable.isAssigned(), originalVariable.isFinal(),
                    originalVariable.getType(), true, -1));
        }
        return copy;
    }
}