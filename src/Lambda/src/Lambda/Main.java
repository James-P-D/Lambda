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
import java.util.Stack;

/*
 * No shorthand
 *  \a.\b.b # This is fine
 *  \ab.b   # This is not, although in mathematics, its equivalent
 *
 * \x.\y.\z a
 * causes an error
 *
 * Check that when declaring new terms (either through console or through
 * script file) that it doesn't clash with a reserved word (e.g. 'help', 'quit')
 *
 * Use alpha-equivalence to check if final value matches an existing term
 *
 * Warn on terms with same alpha-equivalence? No too much work!
 *
 * Fix colour formatted output on 'TERMS' command. (equals is green, but nothing else it!)
 * (Actually, this also needs to happen for general output. e.g. beta equivalence!)
 *  
 * Remove ExpandAllTerms() - Is it actually still used?
 *
 * Check everything is outputting properly with FANCY_UI turned on. Still lots of
 * Console.print which should be something else
 * 
 * Search for TODOs!
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
                            
                            Console.outputToken(termName);
                            Console.outputToken(Character.toString(Constants.SPACE));
                            Console.outputToken(Character.toString(Constants.EQUALS));
                            Console.outputToken(Character.toString(Constants.SPACE));
                            Console.outputToken(terms.get(termName).OutputString());
                            Console.println();
                        } else {
                            
                            LambdaExpression expression = Parser.StartParseExpression(tokens, true, terms);
                            Console.print(Constants.BETA + Constants.PROMPT,  Constants.PROMPT_COLOR);
                            Console.outputToken(expression.OutputString());
                            Console.println();                                
                            Console.print(Constants.BETA + Constants.PROMPT,  Constants.PROMPT_COLOR);
                            Console.outputToken(expression.OutputIDString());
                            Console.println();                                
                            Console.println();                                
                            
                            int counter = 0;
                            while (expression instanceof LambdaApplication) {
                                expression = Evaluate((LambdaApplication)expression);                                
                                Console.print(Constants.BETA + Constants.PROMPT,  Constants.PROMPT_COLOR);
                                Console.outputToken(expression.OutputString());
                                Console.println();
                                Console.print(Constants.BETA + Constants.PROMPT,  Constants.PROMPT_COLOR);
                                Console.outputToken(expression.OutputIDString());
                                Console.println();                                
                                Console.println();                                

                                counter++;
                                if (counter > Constants.MAX_EVALUATION_LOOP) {
                                    Console.outputToken("Breaking loop\n");
                                    break;
                                }
                            }
                            String alphaEquivalentTerm = AlphaEquivalentTerm(expression, terms);
                            if (alphaEquivalentTerm == null) {
                                Console.print(Constants.BETA + Constants.PROMPT, Constants.PROMPT_COLOR);
                                Console.outputToken(expression.OutputString());
                            } else {
                                Console.print(Constants.BETA + Constants.PROMPT, Constants.PROMPT_COLOR);
                                Console.outputToken(alphaEquivalentTerm);
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
    
    private static String AlphaEquivalentTerm(LambdaExpression expression, Map<String, LambdaExpression> terms) {
        String originalExpressionString = expression.OutputIDString();
        for(Map.Entry<String, LambdaExpression> term : terms.entrySet()) {
            LambdaExpression termExpression = term.getValue();
            String termExpressionString = termExpression.OutputIDString();
            if (originalExpressionString.equals(termExpressionString)) {
                return term.getKey();
            }
        }
        return null;
    }
    
    private static LambdaExpression Evaluate(LambdaApplication application) {
        LambdaExpression firstExpression = ((LambdaApplication)application).GetFirstExpression();
        LambdaExpression secondExpression = application.GetSecondExpression();
        
        if (firstExpression instanceof LambdaApplication) {
            LambdaExpression evaluated = Evaluate((LambdaApplication)firstExpression);
            return new LambdaApplication(evaluated, secondExpression);
        } else if (firstExpression instanceof LambdaFunction) {            
            LambdaFunction lastFunction = (LambdaFunction)firstExpression;
            
            LambdaExpression substituted = lastFunction.GetExpression().Substitute(lastFunction, secondExpression);
            return substituted;
        }
        return application;
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
                                String termName = Parser.ParseTermDeclaration(tokens, terms, warnOnRedefinition);
                                // TODO: Maybe should output something here if DebugMode flag is on
                                termsParsed++;
                            } else {
                                LambdaExpression expression = Parser.StartParseExpression(tokens, new IntRef(0), false, terms);
                                // TODO: Maybe should output something here if DebugMode flag is on
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
            Console.outputToken(termName);
            Console.outputToken(Character.toString(Constants.SPACE));
            Console.outputToken(Character.toString(Constants.EQUALS));
            Console.outputToken(Character.toString(Constants.SPACE));
            Console.outputToken(termExpression.OutputString());
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

  /*  
    private static void parseAndOutput(String input){
        Console.print(Constants.LAMBDA + Constants.PROMPT, Constants.PROMPT_COLOR);
        String[] tokens = Tokeniser.Tokenise(input);
        for (int i = 0; i < tokens.length; i++) {
            String token = tokens[i];
            Console.outputToken(token);
        }
        Console.println();
    }
*/
}