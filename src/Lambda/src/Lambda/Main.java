package Lambda;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/*
 * No shorthand
    \a.\b.b # This is fine
    \ab.b   # This is not, although in mathematics, its equivalent

 * Check that when declaring new terms (either through console or through
   script file) that it doesn't clash with a reserved word (e.g. 'help', 'quit')
   
 * Check case of input (for both terms and commands)

 * Use alpha-equivalence to check if final value matches an existing term
 
 * Warn on terms with same alpha-equivalence?

 * Search for TODOs!
 */

public class Main {
    public static void main(String[] args) {
        // Dictionary mapping term names to their expressions
        Map<String, LambdaExpression> terms = new HashMap<String, LambdaExpression>();
        
        // Debug flag
        boolean debugMode = false;
        
        DisplayMessage.Info(Constants.LAMBDA_CALCULUS, Constants.LAMBDA_CALCULUS_INFO);

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
                        //TODO: Alpha equivalence here
                        Console.print(Constants.ALPHA + Constants.PROMPT, Constants.PROMPT_COLOR);
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
                            // TODO: Update to color!
                            System.out.println("TERM: "+ termName + " " + terms.get(termName).OutputString());
                        } else {
                            LambdaExpression expression = Parser.ParseExpression(tokens, new IntRef(0));
                            Console.print(Constants.BETA + Constants.PROMPT,  Constants.PROMPT_COLOR);
                            // TODO: Update to color!
                            System.out.println("EXPRESSION: " + expression.OutputString());
                        }
                    }
                } catch (ParseException e) {
                    DisplayMessage.Error(Constants.ERROR_PARSE_EXCEPTION, e);
                }
            }
        } while ((!input.equals(Constants.QUIT_COMMAND)) && (!input.equals(Constants.EXIT_COMMAND)));
    }

    // Toggle the debugMode flag and output state to stdout
    private static boolean debugCommand(boolean debugMode) {
        boolean newDebugMode = !debugMode;
        DisplayMessage.Debug(String.format(Constants.DEBUG_MODE, newDebugMode ? Constants.ON : Constants.OFF));
        return newDebugMode;
    }
    
    // List the commands available
    private static void helpCommand() {
        DisplayMessage.Info(Constants.HELP, Constants.HELP_INFO_1); 
        DisplayMessage.Info(Constants.HELP, Constants.HELP_INFO_2); 
        DisplayMessage.Info(Constants.HELP, Constants.HELP_INFO_3); 
        DisplayMessage.Info(Constants.HELP, Constants.HELP_INFO_4); 
        DisplayMessage.Info(Constants.HELP, Constants.HELP_INFO_5); 
        DisplayMessage.Info(Constants.HELP, Constants.HELP_INFO_6);         
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
                            if (Parser.IsTermDeclaration(tokens)) {
                                String termName = Parser.ParseTermDeclaration(tokens, terms, warnOnRedefinition);
                                System.out.println(terms.get(termName).OutputString());
                                termsParsed++;
                            } else {
                                LambdaExpression expression = Parser.ParseExpression(tokens, new IntRef(0));
                                System.out.println(expression.OutputString());
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