package Lambda;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 * No shorthand
 *  \a.\b.b # This is fine
 *  \ab.b   # This is not, although in mathematics, its equivalent
 *
 * \x.\y.\z a
 * causes an error
 *
 * Need to check replacing on terms within terms. What about recursion!!!
 * 
 * Fix colour formatted output on 'TERMS' command. (equals is green, but nothing else it!)
 * (Actually, this also needs to happen for general output. e.g. beta equivalence!)
 *  
 * Check everything is outputting properly with FANCY_UI turned on. Still lots of
 * Console.print which should be something else
 * 
 * Search for TODOs!
 * 
 * https://github.com/sjsyrek/malc is useful
 * https://www.cs.cornell.edu/courses/cs3110/2008fa/recitations/rec26.html so's this
 */

public class Main {
    public static void main(String[] args) {
        // Dictionary mapping term names to their expressions
        Map<String, LambdaExpression> terms = new HashMap<String, LambdaExpression>();
                
        // Debug flag
        boolean debugMode = false;
        
        // Display introductory message
        DisplayMessage.Info(Constants.LAMBDA_CALCULUS, Constants.LAMBDA_CALCULUS_INFO);

        // Load any script files passed as arguments to application
        parseArguments(args, terms);

        runTests(terms);
        
        String input = "";
        do {            
            Console.print(Constants.LAMBDA + Constants.PROMPT,  Constants.PROMPT_COLOR);        
            try {
                input = Console.readInput().toLowerCase().trim();
            } catch (IOException e) {
                // On any exceptions reading from stdin, repeat loop
                DisplayMessage.Error(Constants.ERROR_READING_FROM_STDIN, e);
                continue;
            }

            if (input.equals(Constants.ALPHA_COMMAND)) {
                DisplayMessage.Info(Constants.ALPHA_EQUIVALENCE, Constants.ALPHA_EQUIVALENCE_INFO);
                ArrayList<String> alphas = new ArrayList<String>();
                int alphaCount = 1;
                boolean commandReceived;
                do {
                    commandReceived = false;
                    Console.print(Constants.ALPHA + Integer.toString(alphaCount) + Constants.PROMPT, Constants.PROMPT_COLOR);
                    try {
                        input = Console.readInput().trim();
                        // Just incase user tries to quit from alpha-equivalence loop..
                        if ((input.equals(Constants.QUIT_COMMAND)) || (input.equals(Constants.EXIT_COMMAND))) {
                            break;
                        } else if (input.equals(Constants.DEBUG_COMMAND)) {
                            commandReceived = true;
                            debugMode = debugCommand(debugMode);
                        } else if (input.equals(Constants.TERMS_COMMAND)) {
                            commandReceived = true;
                            termsCommand(terms);
                        } else if (input.equals(Constants.HELP_COMMAND)) {
                            commandReceived = true;
                            helpCommand();
                        } else if (input.startsWith(Constants.LOAD_COMMAND)) {
                            commandReceived = true;
                            loadCommand(input.replace(Constants.LOAD_COMMAND, "").trim(), terms, true);
                        }
                    } catch (IOException e) {
                        DisplayMessage.Error(Constants.ERROR_READING_FROM_STDIN, e);
                        continue;
                    }
                    
                    // Only add the input to the list for comparison if its not an empty string or a command
                    if ((!input.equals("")) && (!commandReceived)) {
                        alphas.add(input);
                        alphaCount++;
                    }
                } while (!input.equals(""));
                if (((input.equals(Constants.QUIT_COMMAND)) || (input.equals(Constants.EXIT_COMMAND)))) {
                    DisplayMessage.Info(Constants.QUITTING, Constants.QUIT_MESSAGE);
                } else {
                    if (alphas.size() < 2) {
                        DisplayMessage.Error(Constants.ERROR_MUST_PROVIDE_AT_LEAST_TWO_TERMS);
                    } else {
                        if (AlphaEquivalent(alphas.get(0), alphas.subList(1, alphas.size()), terms)) {
                            Console.print(Constants.ALPHA + Constants.PROMPT + Constants.TRUE, Constants.PROMPT_COLOR);                            
                        } else {
                            Console.print(Constants.ALPHA + Constants.PROMPT + Constants.FALSE, Constants.PROMPT_COLOR);                            
                        }
                        Console.println();
                    }
                }
            } else if (input.equals(Constants.DEBUG_COMMAND)) {
                debugMode = debugCommand(debugMode);
            } else if (input.equals(Constants.TERMS_COMMAND)) {
                termsCommand(terms);
            } else if (input.equals(Constants.HELP_COMMAND)) {
                helpCommand();
            } else if (input.startsWith(Constants.LOAD_COMMAND)) {
                loadCommand(input.replace(Constants.LOAD_COMMAND, "").trim(), terms, true);
            } else if ((input.startsWith(Constants.QUIT_COMMAND)) || (input.startsWith(Constants.EXIT_COMMAND))) {
                DisplayMessage.Info(Constants.QUITTING, Constants.QUIT_MESSAGE);
            } else {
                // In all other cases, treat the input as a term
                // or expression to be parsed and evaluated
                try {
                    String[] tokens = Tokeniser.Tokenise(input);
                    if (tokens.length > 0) {
                        if (Parser.IsTermDeclaration(tokens)) {
                            String termName = Parser.ParseTermDeclaration(tokens, terms, true);
                            Console.print(Constants.BETA + Constants.PROMPT,  Constants.PROMPT_COLOR);
                            
                            formattedOutput(termName);
                            Console.print(Character.toString(Constants.SPACE));
                            Console.outputToken(Character.toString(Constants.EQUALS));
                            Console.print(Character.toString(Constants.SPACE));
                            formattedOutput(terms.get(termName).OutputString());
                            Console.println();
                        } else {                            
                            LambdaExpression expression = Evaluate(tokens, debugMode, terms);
                            LambdaExpression expressionNewIDs = Parser.StartParseExpression(Tokeniser.Tokenise(expression.OutputTempIDString()), true, terms);
                            String alphaEquivalentTerm = AlphaEquivalentTerm(expressionNewIDs.OutputTempIDString(), terms);
                            if (alphaEquivalentTerm == null) {
                                Console.print(Constants.BETA + Constants.PROMPT, Constants.PROMPT_COLOR);
                                formattedOutput(expression.OutputString());
                            } else {
                                Console.print(Constants.BETA + Constants.PROMPT, Constants.PROMPT_COLOR);
                                formattedOutput(alphaEquivalentTerm);
                            }
                            Console.println();                            
                        }
                    }
                } catch (ParseException e) {
                    DisplayMessage.Error(Constants.ERROR_PARSE_EXCEPTION, e);
                }
            }
        } while ((!input.equals(Constants.QUIT_COMMAND)) && (!input.equals(Constants.EXIT_COMMAND)));
    }
    
    private static LambdaExpression Evaluate(String[] tokens, boolean debugMode, Map<String, LambdaExpression> terms) throws ParseException {
        LambdaExpression expression = Parser.StartParseExpression(tokens, true, terms);
        if (debugMode) {
            Console.print(Constants.BETA + Constants.PROMPT, Constants.PROMPT_COLOR);
            formattedOutput(expression.OutputString());
            Console.println();                                
        }
        
        BoolRef updated = new BoolRef(false);
        do {
            updated.value = false;
            expression = Evaluate(expression, updated);      

            if (debugMode && updated.value) {
                Console.print(Constants.BETA + Constants.PROMPT,  Constants.PROMPT_COLOR);
                formattedOutput(expression.OutputString());
                Console.println();
            }
            //expression = Parser.StartParseExpression(Tokeniser.Tokenise(expression.OutputString()), true, terms);
        } while (updated.value);
        
        return expression;
    }

    private static LambdaExpression Evaluate(LambdaExpression expression, BoolRef updated) {
        if (expression instanceof LambdaApplication){
            LambdaApplication application = (LambdaApplication)expression;
            LambdaExpression firstExpression = application.GetFirstExpression();
            LambdaExpression secondExpression = application.GetSecondExpression();
            
            if (firstExpression instanceof LambdaFunction) {
                LambdaFunction lastFunction = (LambdaFunction)firstExpression;
                LambdaExpression substituted = lastFunction.GetExpression().Substitute(lastFunction, secondExpression);
                updated.value = true;
                return substituted;
            }
            else
            {
                return (LambdaExpression)(new LambdaApplication(Evaluate(firstExpression, updated),
                                                                Evaluate(secondExpression, updated)));
            }
        } else if (expression instanceof LambdaFunction) {
            LambdaFunction function = (LambdaFunction)expression;
            return (LambdaExpression)(new LambdaFunction((LambdaName)function.GetName().DeepClone(), Evaluate(function.GetExpression(), updated)));
        }

        return expression.DeepClone();
    }
    
    private static boolean AlphaEquivalent(String first, List<String> remaining, Map<String, LambdaExpression> terms) {
        try {
            boolean alphaEquivalent = true;
            String[] firstTokens = Tokeniser.Tokenise(first);
            LambdaExpression firstExpression = Parser.StartParseExpression(firstTokens, true, terms);
            String firstExpressionIDString = firstExpression.OutputIDString();
            
            for(int i = 0; i < remaining.size(); i++) {
                String[] nextTokens = Tokeniser.Tokenise(remaining.get(i));
                LambdaExpression nextExpression = Parser.StartParseExpression(nextTokens, true, terms);
                String nextExpressionIDString = nextExpression.OutputIDString();
                if (!firstExpressionIDString.equals(nextExpressionIDString)) {
                    alphaEquivalent = false;
                    break;
                }
            }
            return alphaEquivalent;                     
        } catch (ParseException e) {
            DisplayMessage.Error(Constants.ERROR_PARSE_EXCEPTION, e);
            return false;
        }        
    }
    
    private static String AlphaEquivalentTerm(String expressionStr, Map<String, LambdaExpression> terms) {
        try {
            LambdaExpression expression = Parser.StartParseExpression(Tokeniser.Tokenise(expressionStr), true, terms);
            String originalExpressionString = expression.OutputIDString();
            for(Map.Entry<String, LambdaExpression> term : terms.entrySet()) {
                LambdaExpression termExpression = term.getValue();
                String termExpressionString = termExpression.OutputIDString();
                if (originalExpressionString.equals(termExpressionString)) {
                    return term.getKey();
                }
            }

        } catch (ParseException e) {
            return null;
        }
        return null;
    }
    
    // Toggle the debugMode flag and output state to stdout
    private static boolean debugCommand(boolean debugMode) {
        boolean newDebugMode = !debugMode;
        DisplayMessage.Debug(String.format(Constants.DEBUG_MODE, newDebugMode ? Constants.ON : Constants.OFF));
        return newDebugMode;
    }
    
    // List the commands available
    private static void helpCommand() {
        for(String helpLine: Constants.allHelpLines) {
            DisplayMessage.Info(Constants.HELP, helpLine);                
        }
    }
    
    // Load a script file
    private static void loadCommand(String filename, Map<String, LambdaExpression> terms, boolean warnOnRedefinition) {
        int termsParsed = 0;
        int expressionsParsed = 0;        
        int errors = 0;
        int lineNumber = 0;
        
        DisplayMessage.Info(Constants.LOADING_FILE, filename);
        try {
            File file = new File(filename);
            if (!file.exists()) {
                DisplayMessage.Error(String.format(Constants.ERROR_FILE_DOES_NOT_EXIST, filename));
                return;
            }
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF8"));
            String line;
            
            while((line = bufferedReader.readLine()) != null)
            {
                lineNumber++;
                line = line.toLowerCase().trim();
                try {
                    String[] tokens = Tokeniser.Tokenise(line);
                    if (tokens.length > 0) {
                        if(tokens[0].equals(Character.toString(Constants.INCLUDE_FILE))){
                            for (int i = 1; i < tokens.length; i++) {
                                loadCommand(tokens[i], terms, false);
                            }
                        } else {
                            if (Parser.IsTermDeclaration(tokens, lineNumber)) {
                                Parser.ParseTermDeclaration(tokens, terms, warnOnRedefinition);
                                termsParsed++;
                            } else {
                                Parser.StartParseExpression(tokens, new IntRef(0), false, terms);
                                expressionsParsed++;
                            }
                        }
                    }
                } catch (ParseException e) {
                    DisplayMessage.Error(String.format(Constants.ERROR_PARSE_EXCEPTION_ON_LINE, filename, lineNumber), line, e);
                    errors++;
                }
            }
            bufferedReader.close();
            if ((termsParsed == 0) && (expressionsParsed == 0)) {
                DisplayMessage.Warning(String.format(Constants.WARNING_FILE_CONTAINS_NOTHING, filename));
            } else {
                DisplayMessage.Info(Constants.LOADING_FILE, String.format(Constants.TERMS_AND_EXPRESSIONS_PARSED, filename, termsParsed, expressionsParsed));
            }
            if (errors > 0) {
                DisplayMessage.Error(String.format(Constants.ERRORS_FOUND, filename, errors));
            }
        } catch (IOException e) {
            DisplayMessage.Error(String.format(Constants.ERROR_UNABLE_OPEN_FILE, filename), e);
        }
    }

    // Display all terms in dictionary
    private static void termsCommand(Map<String, LambdaExpression> terms) {
        int termsFound = 0;
        for(Map.Entry<String, LambdaExpression> term : terms.entrySet()) {
            String termName = term.getKey();
            LambdaExpression termExpression = term.getValue();
            formattedOutput(termName);
            Console.print(Character.toString(Constants.SPACE));
            formattedOutput(Character.toString(Constants.EQUALS));
            Console.print(Character.toString(Constants.SPACE));
            formattedOutput(termExpression.OutputString());
            Console.println();
            termsFound++;
        }
        DisplayMessage.Info(Constants.TERMS, String.format(Constants.TERMS_MESSAGE, termsFound));
    }
    
    // Parse any arguments sent to main()
    // Each argument will be treated as a path to a script file
    // which we will parse
    private static void parseArguments(String[] args, Map<String, LambdaExpression> terms) {        
        for (int i = 0; i < args.length; i++){
            String filename = args[i];
            loadCommand(filename, terms, true);
        }
    }

    // TODO: Move this to Console?
    private static void formattedOutput(String input) {
        //Console.print(Constants.LAMBDA + Constants.PROMPT, Constants.PROMPT_COLOR);
        String[] tokens = Tokeniser.Tokenise(input);
        for (int i = 0; i < tokens.length; i++) {
            String currentToken = tokens[i];
            if (i > 0) {
                String prevToken = tokens[i - 1];
                if (Parser.isValidIdentifierName(prevToken) && Parser.isValidIdentifierName(currentToken)) {
                    Console.print(" ");
                }
            }
            
            Console.outputToken(currentToken);
        }
    }
    
    private static void test(String testStr, String resultStr, Map<String, LambdaExpression> terms) throws ParseException {
        assert Evaluate(Tokeniser.Tokenise(testStr), false, terms).OutputString().equals(
               Evaluate(Tokeniser.Tokenise(resultStr), false, terms).OutputString());
    }
    
    private static void runTests(Map<String, LambdaExpression> terms) {
        try {
            String[][] tests = {
                    // Boolean Tests
                    {"and true true",           "true"},
                    {"and true false",          "false"},
                    {"or true false",           "true"},
                    {"or false false",          "false"},
                    {"not true",                "false"},
                    {"not false",               "true"},
                    {"not (and true true)",     "false"},
                    {"xor true true",           "false"},
                    {"xor true false",          "true"},
                     
                    // Maths Tests
                    {"add two three",           "five"},
                    {"add two (add one three)", "six"},
                    {"succ two",                "three"},
                    {"succ (succ two)",         "four"},
                    {"mult two two",            "four"},
                    
                    // Tuple Tests
                    {"first (pair one two)",    "one"},
                    {"second (pair one two)",   "two"},
            };

            for(int i=0; i< tests.length; i++) {
                Console.println("Testing: " + tests[i][0]);
                test(tests[i][0], tests[i][1], terms);
            }            
        } catch (ParseException e) {
            assert true == false;
        }
    }
}