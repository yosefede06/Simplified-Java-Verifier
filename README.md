yosefede06
r301299



##File description


    - CompileAlgorithm: CompileAlgorithm is an interface that requires its implementation to have a method called
    "compile" that takes a String input and returns a boolean.

    - CompileCommentsAlgorithm: The CompileCommentsAlgorithm class is responsible for checking the validity
    of comments in the code by overriding the compile method in the CompileAlgorithm interface. It takes a string
    codeAnalyze as input and returns a boolean indicating if the comments in the code are valid.

    - CompileGlobalAlgorithm: The CompileGlobalAlgorithm class is a subclass of GeneralCompile used to compile global
    variables and data structures. It uses a regular expression pattern to match and compile the code and checks
    the validity of the statements. The class has a compile method that overrides the method from the superclass
    and a compileHelper method to compile individual variable statements.

    - CompileLocalAlgorithm: CompileLocalAlgorithm is a Java class in the "oop.ex6.main" package that compiles code
    for local scoped variables. It is a subclass of GeneralCompile and uses a supplier functional interface
    to get the current scope and a LocalDataStructure to store the local data. The class contains a compile()
    method that overrides the compile method in the superclass and a compileHelper() method that performs checks
    for variables and method calls.

    - CompileMethodAlgorithm: The CompileMethodAlgorithm class extends the GeneralCompile class and is responsible
    for compiling methods in S-Java code. It has a constructor that initializes the localData and allCode objects,
    and various methods to check the legality of closures, extract method arguments, save full methods in a map,
    clean method code, and count if/while statements in a line of code.

    - GeneralCompile: "GeneralCompile" is an abstract Java class that extends the "AllPattern" class and implements the
    "CompileAlgorithm" interface. It contains methods to check if a given value matches a variable name and to check
    if two types match.

    - Code: This is the "Code" class in the "oop.ex6.main.DataStructure" package. It contains two variables, "code"
    and "codeOutOfMethod", representing the code inside of methods and outside of methods, respectively.

    - DeepCopyHashMap: This class creates a deep copy of a given HashMap of variables.

    - GlobalDataStructure: Class "GlobalDataStructure" stores method information and global scope data variables
    in HashMap.

    - LocalDataStructure: Class LocalDataStructure is a data structure used to store the local scopes and has data
    members for a map of methods, ArrayLists for the scope, and a Scope object.

    - Scope: The class Scope represents a scope in a Java program, either global or local. It contains all
    variables defined in the scope and its parent scopes and has methods to add variables, update the global map,
    and get a variable from the scope.

    - Variable: The class "Variable" represents a variable in the program and has properties such as type,
    assigned status, final status, global status, and scope. The class has methods to check if the variable definition
    is legal, to get and set the variable's properties, and to retrieve the variable's type and assigned status.

    - AllPattern: The class "AllPattern" contains multiple regex patterns for matching and validating strings.

    - CompileFactory: CompileFactory is a class responsible for creating instances of different CompileAlgorithm
    objects by implementing the factory pattern. It has a method named "createFactory" to create instances of the
    desired CompileAlgorithm based on the compileName parameter.

    - The class Sjavac is responsible for checking the validity of Java code by checking its structure, methods,
    and comments. It does this by creating instances of compile factory and calls methods of the compileGlobalAlgorithm,
    compileLocalAlgorithm, compileMethodAlgorithm, and compileCommentsAlgorithm classes to perform various checks on
    the code. If all checks pass, it returns LEGAL_CODE, otherwise it returns ILLEGAL_CODE.


*******************************
*           Design            *
*******************************

    - The main class extends the AllPattern abstract class which defines regex patterns used to check different
    parts of the code.

    - The program is divided into two main parts, Global and Local.
    The Global code refers to the code outside of methods, and the Local code refers to the code inside methods.
    The code is first processed to remove comments and basic syntax using a regular expression.
    Then, it checks the structure of the code, including the methods and global variables,
    using compileAlgorithm instances.

*******************************
*    Implementation issues    *
*******************************

    Explained in questions.


*******************************
*    Answers to questions     *
*******************************

    *******************************
    **********   6.1   ************
    *******************************

    In the README file, please report on how you addressed any errors in the s-Java code for this exercise.
    Additionally, explain your reasoning behind the approach you took.

    - We opted to handle exceptions with the try and catch mechanism, specifically for IOExceptions.
    This approach checks for valid input files and arguments at the start of the program.
    We deemed using exceptions to handle the code itself an inappropriate solution.
    The main objective of the program is to determine if the input code is LEGAL or ILLEGAL.
    As such, statements like "int a = True" or "int a = 4" should be treated equally,
    as both are considered "legal" outputs.

    *******************************
    **********   6.2   ************
    *******************************

    We have divided our code into smaller, independent units for better organization and understanding.

    To begin, we created two packages, Algorithm and DataStructure.
    The Algorithm package contains all of the algorithms implemented in this exercise,
    and the DataStructure package includes classes for each type of check, such as code, scope, and variables.
    It also tracks both global and local variables to identify any potential issues.

    We have a class named AllPattern that serves as a collection of all patterns related to methods,
    variables, and other elements. This class is crucial for ensuring the validity of the code.

    Additionally, we have a Factory class to handle the different types of compilations needed,
    including comments, methods, global variables, and more.

    • How would you modify your code to add new types of variables (e.g., float)?

        - To add support for a new type of variable, such as "float", the following modifications would
        need to be made to the code:

            1. First, we would add the new type "float" and its corresponding regular expression to the
             AllPattern class.

            2. Next, we would modify the checkMatchingTypes method in the GeneralCompile class to define the
            compatibility rules between the new float type and other types (such as int). (The checkMatchingTypes
            method would then be utilized by both the CompileLocalAlgorithm and CompileLocalAlgorithm to verify the
            validity of float assignments throughout the entire code.)

    • Below are three features your program currently does not support. Please select two of
    them, and describe which modifications/extensions you would have to make in your code
    in order to support them. Please briefly describe which classes you would add to your code,
    which methods you would add to existing classes, and which classes you would modify. You
    are not required to implement these features.

    – Classes.

        - To support classes in the code, a new regex pattern needs to be added at the AllPattern class to check
        the syntax validity. A new compileClassAlgorithm is then added, working similarly to the CompileMethodAlgorithm
        and defining the main logic. Every class that extends the GeneralCompile class will have a private instance
        obtained from the constructor of the LocalDataStructure, and in cases where it is legal to access classes
        from the global scope, the GlobalDataStructure is also held.

        - In the data structures, a new class structure needs to be added to save class name, members,
        constructor parameters, private and public fields. This data is too much to be saved in a single
        data structure, so a new class called ClassStructure is created. The ClassStructure class holds all the data
        in its fields, which are Hashmaps. Finally, the ClassStructure is added to the Global/LocalDataStructure.

    – Different methods’ return types (i.e int foo()).

        - To support return types in methods, the regex pattern in the AllPattern class that checks the syntax
        validity of methods needs to be modified to include return types (e.g. (int|char|...|void)\s*<METHOD_NAME>...).
        Additionally, the data structure that holds method parameters (HashMap<String, String[]> methodMap)
        would need to be updated to include return types. To accomplish this, I would create a new class that
        holds all the hashmaps, similar to the approach taken for implementing classes. Finally, a final check on
        the return types would need to be added to the CompileHelper of the CompileLocalAlgorithm.

    – Using methods of standard java (i.e System.out.println).

        - To support the use of standard Java methods such as System.out.println, we would need to add a new HashMap
         in the GlobalDataStructure class to store the methods of the standard Java libraries.
         This HashMap would map the method names to their respective signatures, such as the return type and parameters.
         We would also need to modify the compileHelper method in the CompileLocalAlgorithm to check if the method
         being used is present in the HashMap of standard Java methods.
         Additionally, we would need to handle with regex the standard Java library import statements in the code,
         so that the program knows which library to access to use these methods and save those in a new data structure
         which will be used on the logic of the compileAlgorithms.

    *******************************
    **********   6.3   ************
    *******************************

    In your README file, please describe two of the main regular expressions you
    used in your code.

    1. String NAME_VARIABLE_REGEX = "((([a-zA-Z])(\w)*)|(_(\w)+))"
        Defines a pattern for valid variable names in the code.
        The expression is split into two parts, separated by the pipe symbol "|":

            - The first part, "(([a-zA-Z])(\w)*)", matches a sequence of characters that starts with an uppercase or
             lowercase letter and is followed by zero or more word characters (letters, digits, or underscores).

            - The second part, "_ (\w)+", matches a sequence of one or more word characters that starts with an
             underscore.

        The whole expression matches either of these two patterns and ensures that the string follows the rules for a
        valid variable name.

    2. String REPLACE_COMMENTS_REGEX = "((//)(.*)\\s*)"
       This is a regular expression used to match and replace comments in code.
       Here's what each part of the expression means:

        - // matches the characters "//".
        - .* matches any number of characters (including zero characters).
        - \\s* matches any number of white-space characters (including zero white-space characters).
        - The entire expression ((//)(.*)\\s*) matches any comment that starts with "//" and is
         followed by any number of characters and white-space characters.

       In summary, this regular expression matches single-line comments in Java code
       that start with "//" and can contain any characters and white-space characters after it.


    *******************************
    *********   Tests    **********
    *******************************

    - Test 1 : First test, we check good comments that start with // in the beggining of line and good
    declaration of the variable that finish with ;

    - Test 2 :  As required it's not legal that a comment not start at the beginning of the line, so we check it.

    - Test 3 : Bad declaration of the variable, operators are not supported

    - Test 4 : This test checks all the good Legal name for a variable with a good declaration.

    - Test 5 : This test checks all the legal declaration for a variable and

    - Test 6 : it can not be two variable with the same name

    - Test 7 : Two local variable with the same name can be defined inside different blocks, even
             if one is nested in the other. A variable (local or global) may have the same name as a method.

    - Test 8 : Type of the second argument is not in the declaration of the method .

    - Test 9 : Two functions, foo and boo, boo calls foo in this method with local variable of type int declared on
               it and call itself recursively.

    - Test 10 : Same functions but here the method boo calls foo without argument and it's not work.

    - Test 11 : Check of two return statement (basic)

    - Test 12 : Illegal declaration of variable after return statement

    - Test 13 : Return statement in the if statement and in the end of the method (good)

    - Test 14 : Double declaration of a variable (not good)

    - Test 15 : Good method + declaration final global variable

    - Test 16 : Any variable may be declared using the modifier final, which makes it a constant. Such
                variables must be initialized with some value at declaration time

    - Test 17 : Check of local scope with assignment value of a variable to a that is initialize in the scope
                before. Additionally, a double can also be assigned with an int, and a boolean can also be assigned
                with an int and a double.

    - Test 18 : Many methods with checking if / while statement with spaces also and local declaration variable

    - Test 19 : A method may not be declared inside another method.

    - Test 20 : Complex test with many checks like local declaration with call to other function and comments.
