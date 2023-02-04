package oop.ex6.main;

import java.util.regex.Pattern;

public class AllPattern {
    private static final String IS_FINAL = "<IS_FINAL>";
    private static final String TYPE_VARIABLE = "<TYPE_VARIABLE>";
    private static final String IS_OBLIGATORY_DECLARATION = "<IS_OBLIGATORY_DECLARATION>";
    private static final String STRUCTURE_METHOD_REGEX =
            "void([\\s\\S]*?[{][^{]*)(\\s*?\\n\\s*return[\\s\\S]*?})+";
    private static final String STRUCTURE_VARIABLE_REGEX = "\\w[\\s\\S]*?;";
    private static final String TYPE_VARIABLE_REGEX =
            "\\s*(((<IS_FINAL>[^\\S\\r\\n]+))?)<TYPE_VARIABLE>[^\\S\\r\\n]+";
    private static final String NAME_VARIABLE_REGEX = "((([a-zA-Z])(\\w)*)|(_(\\w)+))";
    private static final String VALUE_VARIABLE_REGEX = "(%s)";
    private static final String DECLARATION_VARIABLE_REGEX =
            "([^\\S\\r\\n]*=[^\\S\\r\\n]*(" + VALUE_VARIABLE_REGEX + "|" + NAME_VARIABLE_REGEX
                    + ")[^\\S\\r\\n]*)<IS_OBLIGATORY_DECLARATION>[^\\S\\r\\n]*";
    private static final String MORE_NAME_VARIABLE_REGEX = NAME_VARIABLE_REGEX + DECLARATION_VARIABLE_REGEX +
            "(,[^\\S\\r\\n]*" + NAME_VARIABLE_REGEX + DECLARATION_VARIABLE_REGEX + ")*";
    private static final String VARIABLE_REGEX =
            "(" + TYPE_VARIABLE_REGEX + MORE_NAME_VARIABLE_REGEX + ";\\s*" + ")";
    private static final String LEGAL_CHARS = "[^\\’”,\\\"]";
    private static final String INT_VALUE_REGEX = "[+-]?\\d+";
    private static final String DOUBLE_VALUE_REGEX = "[+-]?\\d+[.]?\\d*|[+-]?\\d*[.]?\\d+";
    private static final String STRING_VALUE_REGEX = '"' + LEGAL_CHARS + "*" + '"';
    private static final String BOOLEAN_VALUE_REGEX = "true|false|" + DOUBLE_VALUE_REGEX;
    private static final String CHAR_VALUE_REGEX = "'" + LEGAL_CHARS + "'";
    public static final String POSSIBLE_TYPES = "(boolean|char|String|double|int)";
    private static final String TYPE_AND_NAME =
            "([^\\S\\r\\n]*(final)?\\s*" + POSSIBLE_TYPES + "[^\\S\\r\\n]*" +
                    NAME_VARIABLE_REGEX + "[^\\S\\r\\n]*)";
    private static final String ARGUMENTS =
            "[(]" + TYPE_AND_NAME + "(,[^\\S\\r\\n]*" + TYPE_AND_NAME + ")*[)]";
    private static final String POSSIBLE_CONSTANTS =
            String.join("|", DOUBLE_VALUE_REGEX, STRING_VALUE_REGEX,
                    BOOLEAN_VALUE_REGEX, CHAR_VALUE_REGEX, INT_VALUE_REGEX);
    private static final String JUST_OBLIGATORY_DECLARATION_VARIABLE_REGEX =
            "([^\\S\\r\\n]*=[^\\S\\r\\n]*((" + POSSIBLE_CONSTANTS + ")|" + NAME_VARIABLE_REGEX
                    + ")[^\\S\\r\\n]*)[^\\S\\r\\n]*";
    private static final String JUST_DECLARATION =
            "\\s*" + NAME_VARIABLE_REGEX + JUST_OBLIGATORY_DECLARATION_VARIABLE_REGEX +
                    "(,[^\\S\\r\\n]*" + NAME_VARIABLE_REGEX + JUST_OBLIGATORY_DECLARATION_VARIABLE_REGEX + ")*;\\s*";
    private static final String NAME_VARIABLE_FOR_CALL_METHOD = "[^\\S\\r\\n]*(" + NAME_VARIABLE_REGEX + "|(" +
            POSSIBLE_CONSTANTS + "))[^\\S\\r\\n]*";
    private static final String ARGUMENTS_CALL_METHOD =
            "[(]" + NAME_VARIABLE_FOR_CALL_METHOD + "(,[^\\S\\r\\n]*" +
                    NAME_VARIABLE_FOR_CALL_METHOD + ")*[)]";
    private static final String METHOD_NAME = "[^\\S\\r\\n]*[a-zA-Z](\\w)*[^\\S\\r\\n]*";
    private static final String CALL_METHOD_NAME = "\\s*[a-zA-Z](\\w)*[^\\S\\r\\n]*";
    private static final String METHOD_REGEX =
            "\\s*void" + METHOD_NAME + "((" + ARGUMENTS + ")|([(][^\\S\\r\\n]*[)]))[^\\S\\r\\n]*";
    private static final String CALL_METHOD_REGEX = "(" + CALL_METHOD_NAME + "((" + ARGUMENTS_CALL_METHOD +
            ")|([(][)]))[^\\S\\r\\n]*;\\s*)";
    private static final String ONE_CONDITION = String.join("|", BOOLEAN_VALUE_REGEX,
            NAME_VARIABLE_REGEX);
    private static final String CONDITION =
            "([^\\S\\r\\n]*([^\\S\\r\\n]*" + ONE_CONDITION + ")([^\\S\\r\\n]*(&{2}|[|]{2})[^\\S\\r\\n]*(" +
                    ONE_CONDITION + ")[^\\S\\r\\n]*)*[^\\S\\r\\n]*)";
    private static final String RETURN_REGEX = "\\s*return[^\\S\\r\\n]*;\\s*";
    public static final String IF_WHILE_CONDITION =
            "\\s*(if|while)[^\\S\\r\\n]*[(]" + CONDITION + "[)][^\\S\\r\\n]*[{][^\\S\\r\\n]*";
    private static final String POSSIBLE_INSIDE_CURLY_BRACKETS = String.join("|",
            handleVariableRegex(),
            CALL_METHOD_REGEX,
            JUST_DECLARATION,
            RETURN_REGEX);
    private static final String POSSIBLE_INSIDE_CURLY_BRACKETS_NOT_METHODS = String.join("|",
            handleVariableRegex(),
            JUST_DECLARATION);
    private static final String INSIDE_CURLY_BRACKETS =
            "(\\s*(" + POSSIBLE_INSIDE_CURLY_BRACKETS + ")?\\s*)(\\n(\\s*" +
                    POSSIBLE_INSIDE_CURLY_BRACKETS + "[^\\S\\r\\n]*))*\\s*";
    private static final String INSIDE_CURLY_BRACKETS_NOT_METHODS =
            "(\\s*(" + POSSIBLE_INSIDE_CURLY_BRACKETS_NOT_METHODS + ")?\\s*)(\\n(\\s*" +
                    POSSIBLE_INSIDE_CURLY_BRACKETS_NOT_METHODS + "[^\\S\\r\\n]*))*\\s*";
    private static final String LINE_WITH_COMMENTS_REGEX = "(^(//)(.*)\\s*)";
    public static final String REPLACE_COMMENTS_REGEX = "((//)(.*)\\s*)";
    private static final String COMMENTS_REGEX =
            "(" + String.join("|", LINE_WITH_COMMENTS_REGEX, "([^/]*\\s*)") + ")*";
    /**
     * Patterns
     */
    public final Pattern nameVariablePattern;
    public final Pattern variablePattern;
    public final Pattern fullMethodRegexPattern;
    public final Pattern ifWhileRegexPattern;
    public final Pattern commentsPattern;
    public final Pattern possibleTypesPattern;
    public final Pattern codeInsideBracketsAndGlobalPatternNotMethods;
    public final Pattern booleanRegexConstantPattern;
    public final Pattern returnPattern;
    public final Pattern structureMethodPattern;
    public final Pattern structureVariablePattern;

    /**
     * Constructor to initialize the regex patterns used to match various elements.
     */
    public AllPattern() {
        variablePattern = Pattern.compile(handleVariableRegex());
        nameVariablePattern = Pattern.compile(NAME_VARIABLE_REGEX);
        fullMethodRegexPattern = Pattern.compile(fullMethodRegex());
        codeInsideBracketsAndGlobalPatternNotMethods = Pattern.compile(INSIDE_CURLY_BRACKETS_NOT_METHODS);
        ifWhileRegexPattern = Pattern.compile(IF_WHILE_CONDITION);
        commentsPattern = Pattern.compile(COMMENTS_REGEX);
        possibleTypesPattern = Pattern.compile(POSSIBLE_TYPES);
        returnPattern = Pattern.compile(RETURN_REGEX);
        booleanRegexConstantPattern = Pattern.compile(BOOLEAN_VALUE_REGEX);
        structureMethodPattern = Pattern.compile(STRUCTURE_METHOD_REGEX);
        structureVariablePattern = Pattern.compile(STRUCTURE_VARIABLE_REGEX);
    }

    /**
     * This method to generate the regex pattern to match full methods.
     * @return a String representing the regex pattern to match full methods.
     */
    public String fullMethodRegex() {
        return METHOD_REGEX + "[{[^\\S\\r\\n]*\n]" + INSIDE_CURLY_BRACKETS +
                "\\n\\s*return[^\\S\\r\\n]*;[^\\S\\r\\n]*?\\n(\\n)*?[^\\S\\r\\n]*[}]\\s*";
    }

    /**
     * Method to replace a type in a regex pattern for matching variables.
     * @param v1 the type to replace
     * @param v2 the regex pattern for the type
     * @return a String representing the updated regex pattern
     */
    static public String replaceType(String v1, String v2) {
        return String.format(VARIABLE_REGEX.replaceAll(TYPE_VARIABLE, v1), v2, v2);
    }

    /**
     * Method to replace the final and declaration keywords in the regular expression for
     * matching variable declarations.
     * @param s the regular expression for matching variable declarations
     * @param v1 the string to replace the final keyword in the regular expression
     * @param v2 the string to replace the declaration keyword in the regular expression
     * @return a String representing the regular expression with the final and declaration
     * keywords replaced
     */
    static public String replaceFinal(String s, String v1, String v2) {
        return s.replaceAll(IS_FINAL, v1).replaceAll(IS_OBLIGATORY_DECLARATION, v2);
    }
    /**
     * Method to generate the regular expression for matching variable types.
     * @return a String representing the regular expression for matching variable types
     */
    static public String typeRegex() {
        return String.join("|",
                replaceType("int", INT_VALUE_REGEX),
                replaceType("String", STRING_VALUE_REGEX),
                replaceType("boolean", BOOLEAN_VALUE_REGEX),
                replaceType("char", CHAR_VALUE_REGEX),
                replaceType("double", DOUBLE_VALUE_REGEX));
    }
    /**
     * Method to handle the regular expression for matching variable declarations.
     * @return - a String representing the regular expression for matching variable declarations
     */
    static public String handleVariableRegex() {
        String s = typeRegex();
        return String.join("|",
                replaceFinal(s,"final", ""),
                replaceFinal(s,"", "?"));

    }
}
