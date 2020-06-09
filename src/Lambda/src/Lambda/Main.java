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

 * Use alpha-eqivalence to check if final value matches an existing term

 */

public class Main {

    private static Map<String, LambdaExpression> terms;
    
    public static void main(String[] args) {   
        terms = new HashMap<String, LambdaExpression>();
        
        parseArguments(args);

        displayInfo(Constants.LAMBDA_CALCULUS, Constants.LAMBDA_CALCULUS_INFO);

        String input = "";
        boolean debugMode = false;

        do {            
            Console.print(Constants.LAMBDA + Constants.PROMPT,  Constants.PROMPT_COLOR);        
            try {
                input = Console.readInput().toLowerCase();
            } catch (IOException e) {
                displayError(Constants.ERROR_READING_FROM_STDIN, e);
                continue;
            }

            if (input.equals(Constants.ALPHA_COMMAND)) {
                ArrayList<String> alphas = new ArrayList<String>();
                int alphaCount = 1;
                do {
                    Console.print(Constants.ALPHA + Integer.toString(alphaCount) + Constants.PROMPT, Constants.PROMPT_COLOR);
                    try {
                        input = Console.readInput().trim();
                    } catch (IOException e) {
                        displayError(Constants.ERROR_READING_FROM_STDIN, e);
                        continue;
                    }
                    if (!input.equals("")) {
                        alphas.add(input);
                        alphaCount++;
                    }
                } while (!input.equals(""));
                if (alphas.size() < 2) {
                    displayError(Constants.ERROR_MUST_PROVIDE_ATLEAST_TWO_TERMS);
                } else {
                    Console.print(Constants.ALPHA + Constants.PROMPT, Constants.PROMPT_COLOR);
                    Console.println();
                }
            } else if (input.equals(Constants.DEBUG_COMMAND)) {
                debugMode = !debugMode;
                displayInfo(Constants.DEBUG_MODE, debugMode ? Constants.ON : Constants.OFF);
            } else if (input.equals(Constants.TERMS_COMMAND)) {
                displayAllTerms();
            } else if (input.equals(Constants.HELP_COMMAND)) {
                displayInfo(Constants.HELP, Constants.HELP_INFO_1); 
                displayInfo(Constants.HELP, Constants.HELP_INFO_2); 
                displayInfo(Constants.HELP, Constants.HELP_INFO_3); 
                displayInfo(Constants.HELP, Constants.HELP_INFO_4); 
                displayInfo(Constants.HELP, Constants.HELP_INFO_5); 
                displayInfo(Constants.HELP, Constants.HELP_INFO_6); 
            } else if (input.startsWith(Constants.LOAD_COMMAND)) {
                String filename = input.replace(Constants.LOAD_COMMAND, "").trim();
                loadFile(filename);
            } else if ((input.equals(Constants.QUIT_COMMAND)) || (input.equals(Constants.EXIT_COMMAND))) {
                displayInfo(Constants.QUITTING, Constants.QUIT_MESSAGE);
            } else {
                try {
                    String[] tokens = Tokeniser.Tokenise(input);
                    if (isTermDeclaration(tokens)) {
                        String termName = parseTermDeclaration(tokens);
                        System.out.println("TERM: "+ termName + " " + terms.get(termName).OutputString());
                    } else {
                        LambdaExpression expression = parseExpression(tokens, 0);
                        System.out.println("EXPRESION: " + expression.OutputString());
                    }
                } catch (ParseException e) {
                    displayError(Constants.ERROR_PARSE_EXCEPTION, e);
                }
            }
        } while ((!input.equals(Constants.QUIT_COMMAND)) && (!input.equals(Constants.EXIT_COMMAND)));
    }

    private static void displayAllTerms() {
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
        displayInfo(Constants.TERMS, Integer.toString(termsFound) + Constants.TERMS_MESSAGE);
    }
    
    private static void displayWarning(String message) {
        Console.print(Constants.WARNING + ": ", Console.Color.YELLOW);
        Console.println(message, Console.Color.YELLOW_BRIGHT);                
    }

    private static void displayError(String message, Exception e) {
        displayError(message);
        displayError(e.getMessage());                
    }

    private static void displayError(String message){
        Console.print(Constants.ERROR + ": ", Console.Color.RED);
        Console.println(message, Console.Color.RED_BRIGHT);                
    }

    private static void displayInfo(String label, String message){
        Console.print(label + ": ", Console.Color.BLUE);
        Console.println(message, Console.Color.BLUE_BRIGHT);
    }
    
    private static void displayDebug(String message){
        Console.print(Constants.DEBUG + ": ", Console.Color.MAGENTA);
        Console.println(message, Console.Color.MAGENTA_BRIGHT);
    }    

    private static void parseArguments(String[] args) {        
        for (int i = 0; i < args.length; i++){
            String filename = args[i];
            loadFile(filename);
        }
    }

    private static void loadFile(String filename){
        int termsParsed = 0;
        int errors = 0;
        displayInfo(Constants.LOADING_FILE, filename);
        try {
            File file = new File(filename);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF8"));
            String line;
            
            while((line = bufferedReader.readLine()) != null)
            {
                line = line.toLowerCase().trim();
                if (!line.isEmpty()) {
                    //parseAndOutput(line);
                    try {
                        String[] tokens = Tokeniser.Tokenise(line);                        
                        if (isTermDeclaration(tokens)) {
                            parseTermDeclaration(tokens);
                            termsParsed++;
                        } else {
                            //TODO: Fix!
                            displayError("WHAT DO WE DO HERE?");
                        }
                    } catch (ParseException e) {
                        displayError(Constants.ERROR_PARSE_EXCEPTION, e);
                        errors++;
                    }
                }
            }
            bufferedReader.close();
            displayInfo(Constants.LOADING_FILE, Integer.toString(termsParsed) + Constants.TERMS_PARSED);
            if (errors > 0) {
                displayError(Integer.toString(errors) + Constants.ERRORS_FOUND);
            }
        } catch (IOException e) {
            displayError(Constants.ERROR_UNABLE_OPEN_FILE + filename, e);
        }
    }
    
    private static void parseAndOutput(String input){
        Console.print(Constants.LAMBDA + Constants.PROMPT, Constants.PROMPT_COLOR);
        String[] tokens = Tokeniser.Tokenise(input);
        for (int i = 0; i < tokens.length; i++) {
            String token = tokens[i];
            Console.outputToken(token);
        }
        Console.println();
    }

    private static boolean isValidIdentifierName(String name) {
        String validFirstCharacter = "abcdefghijklmnopqrstuvwxyz_";
        String validRestCharacters = validFirstCharacter + "0123456789"; 
        
        if (name.length() == 0) {
            return false;
        }
        
        if (Constants.allCommands.contains(name)) {
            return false;
        }
        
        for (int i = 0; i < name.length(); i++) {
            char ch = name.charAt(i);
            if (i == 0) {
                if (!validFirstCharacter.contains(Character.toString(ch))) {
                    return false;
                }
            } else {
                if (!validRestCharacters.contains(Character.toString(ch))) {
                    return false;
                }
            }            
        }
        
        return true;
    }
 
    private static boolean termAlreadyExists(String termName) {
        return terms.containsKey(termName);            
    }
    
    private static boolean isTermDeclaration(String[] tokens) {
        return ((tokens.length >= 3) && (tokens[1].equals(Character.toString(Constants.EQUALS))));
    }
    
    private static String parseTermDeclaration(String[] tokens) throws ParseException {
        String termName = tokens[0];
        if (!isValidIdentifierName(termName)) {
            throw new ParseException(Constants.ERROR_INVALID_IDENTIFIER_NAME + termName);
        }
        if (termAlreadyExists(termName)) {
            displayWarning(Constants.WARNING_TERM_ALREADY_DEFINED + termName);
        }
        LambdaExpression expression = parseExpression(tokens, 2);
        terms.put(termName, expression);
        return termName;
    }
    
    private static LambdaExpression parseExpression(String[] tokens, int index) throws ParseException {
        while(index < tokens.length) {
            String token = tokens[index];
            if ((token.equals(Character.toString(Constants.LAMBDA))) || (token.equals(Character.toString(Constants.LAMBDA_SUBSTITUTE)))) {
                return parseLambdaFunction(tokens, index + 1);
            } else {
                return parseLambdaName(tokens, index);
            }
            //index++;
        }
        
        return new LambdaName("foo");
    }
    
    private static LambdaFunction parseLambdaFunction(String[] tokens, int index) throws ParseException {
        if (tokens.length < (index + 3)) {
            throw new ParseException(Constants.ERROR_BADLY_FORMATTED_FUNCTION);
        }
        if (!tokens[index + 1].equals(Character.toString(Constants.PERIOD))) {
            throw new ParseException(Constants.ERROR_EXPECTED_PERIOD_IN_FUNCTION);
        }
        
        return new LambdaFunction(parseLambdaName(tokens, index), parseExpression(tokens, index + 2));
    }
    
    private static LambdaName parseLambdaName(String[] tokens, int index) throws ParseException {
        String termName = tokens[index];
        if (!isValidIdentifierName(termName)) {
            throw new ParseException(Constants.ERROR_INVALID_IDENTIFIER_NAME + termName);
        }        

        return new LambdaName(termName);
    }
}