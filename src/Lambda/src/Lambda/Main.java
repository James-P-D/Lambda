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

    private static Map<String, LambdaExpression> terms;
    private static boolean debugMode = false;
    
    public static void main(String[] args) {   
        terms = new HashMap<String, LambdaExpression>();
        parseArguments(args);

        DisplayMessage.Info(Constants.LAMBDA_CALCULUS, Constants.LAMBDA_CALCULUS_INFO);

        String input = "";
        do {            
            Console.print(Constants.LAMBDA + Constants.PROMPT,  Constants.PROMPT_COLOR);        
            try {
                input = Console.readInput().toLowerCase().trim();
            } catch (IOException e) {
                DisplayMessage.Error(Constants.ERROR_READING_FROM_STDIN, e);
                continue;
            }

            if (input.equals(Constants.ALPHA_COMMAND)) {
                DisplayMessage.Info(Constants.ALPHA_EQUIVALENCE, Constants.ALPHA_EQUIVALENCE_INFO);
                ArrayList<String> alphas = new ArrayList<String>();
                int alphaCount = 1;
                boolean commandReceived = false;
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
                            debugCommand();
                        } else if (input.equals(Constants.TERMS_COMMAND)) {
                            commandReceived = true;
                            termsCommand();
                        } else if (input.equals(Constants.HELP_COMMAND)) {
                            commandReceived = true;
                            helpCommand();
                        } else if (input.startsWith(Constants.LOAD_COMMAND)) {
                            commandReceived = true;
                            loadCommand(input.replace(Constants.LOAD_COMMAND, "").trim());
                        }
                    } catch (IOException e) {
                        DisplayMessage.Error(Constants.ERROR_READING_FROM_STDIN, e);
                        continue;
                    }
                    if ((!input.equals("")) && (!commandReceived)) {
                        alphas.add(input);
                        alphaCount++;
                    }
                } while (!input.equals(""));
                if (!((input.equals(Constants.QUIT_COMMAND)) || (input.equals(Constants.EXIT_COMMAND)))) {
                    if (alphas.size() < 2) {
                        DisplayMessage.Error(Constants.ERROR_MUST_PROVIDE_AT_LEAST_TWO_TERMS);
                    } else {
                        //TODO: Alpha equivalence here
                        Console.print(Constants.ALPHA + Constants.PROMPT, Constants.PROMPT_COLOR);
                        Console.println();
                    }
                }
            } else if (input.equals(Constants.DEBUG_COMMAND)) {
                debugCommand();
            } else if (input.equals(Constants.TERMS_COMMAND)) {
                termsCommand();
            } else if (input.equals(Constants.HELP_COMMAND)) {
                helpCommand();
            } else if (input.startsWith(Constants.LOAD_COMMAND)) {
                loadCommand(input.replace(Constants.LOAD_COMMAND, "").trim());
            } else {
                try {
                    String[] tokens = Tokeniser.Tokenise(input);
                    if (tokens.length > 0) {
                        if (Parser.IsTermDeclaration(tokens)) {
                            String termName = Parser.ParseTermDeclaration(tokens, terms);
                            Console.print(Constants.BETA + Constants.PROMPT,  Constants.PROMPT_COLOR);
                            // TODO: Update to color!
                            System.out.println("TERM: "+ termName + " " + terms.get(termName).OutputString());
                        } else {
                            LambdaExpression expression = Parser.ParseExpression(tokens, new IntRef(0));
                            Console.print(Constants.BETA + Constants.PROMPT,  Constants.PROMPT_COLOR);
                            // TODO: Update to color!
                            System.out.println("EXPRESION: " + expression.OutputString());
                        }
                    }
                } catch (ParseException e) {
                    DisplayMessage.Error(Constants.ERROR_PARSE_EXCEPTION, e);
                }
            }
        } while ((!input.equals(Constants.QUIT_COMMAND)) && (!input.equals(Constants.EXIT_COMMAND)));
        DisplayMessage.Info(Constants.QUITTING, Constants.QUIT_MESSAGE);
    }

    private static void debugCommand() {
        debugMode = !debugMode;
        DisplayMessage.Debug(String.format(Constants.DEBUG_MODE, debugMode ? Constants.ON : Constants.OFF));        
    }
    
    private static void helpCommand() {
        DisplayMessage.Info(Constants.HELP, Constants.HELP_INFO_1); 
        DisplayMessage.Info(Constants.HELP, Constants.HELP_INFO_2); 
        DisplayMessage.Info(Constants.HELP, Constants.HELP_INFO_3); 
        DisplayMessage.Info(Constants.HELP, Constants.HELP_INFO_4); 
        DisplayMessage.Info(Constants.HELP, Constants.HELP_INFO_5); 
        DisplayMessage.Info(Constants.HELP, Constants.HELP_INFO_6);         
    }

    private static void loadCommand(String filename) {
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
                        if (Parser.IsTermDeclaration(tokens)) {
                            String termName = Parser.ParseTermDeclaration(tokens, terms);
                            System.out.println(terms.get(termName).OutputString());
                            termsParsed++;
                        } else {
                            LambdaExpression expression = Parser.ParseExpression(tokens, new IntRef(0));
                            System.out.println(expression.OutputString());
                            expressionsParsed++;
                        }
                    }
                } catch (ParseException e) {
                    DisplayMessage.Error(String.format(Constants.ERROR_PARSE_EXCEPTION_ON_LINE, lineNumber), line, e);
                    errors++;
                }
            }
            bufferedReader.close();
            if ((termsParsed == 0) && (expressionsParsed == 0)) {
                DisplayMessage.Warning(String.format(Constants.WARNING_FILE_CONTAINS_NOTHING, filename));
            } else {
                DisplayMessage.Info(Constants.LOADING_FILE, String.format(Constants.TERMS_AND_EXPRESSIONS_PARSED, termsParsed, expressionsParsed));
            }
            if (errors > 0) {
                DisplayMessage.Error(String.format(Constants.ERRORS_FOUND, errors));
            }
        } catch (IOException e) {
            DisplayMessage.Error(String.format(Constants.ERROR_UNABLE_OPEN_FILE, filename), e);
        }
    }

    private static void termsCommand() {
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
    
    private static void parseArguments(String[] args) {        
        for (int i = 0; i < args.length; i++){
            String filename = args[i];
            loadCommand(filename);
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