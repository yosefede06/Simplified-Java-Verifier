package oop.ex6.main.Algorithm;

/**
 * This class extends GeneralCompile and implements the CompileAlgorithm interface.
 * It is responsible for checking the validity of comments in the code.
 */
public class CompileCommentsAlgorithm extends GeneralCompile {

    /**
     * This method overrides the compile method in the CompileAlgorithm interface.
     * It takes a string codeAnalyze as input and returns a boolean value indicating if the comments in the
     * code are valid.
     * @param codeAnalyze the string to be analyzed.
     * @return true if the comments in the code are valid, false otherwise.
     */
    @Override
    public boolean compile(String codeAnalyze) {
        String[] allLines = codeAnalyze.split("\\n");
        for (String allLine : allLines) {
            if (!commentsPattern.matcher(allLine).matches()) {
                return false;
            }
        }
        return true;
    }
}
