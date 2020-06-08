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

    private static Map<String, String> terms;
    
    public static void main(String[] args) {                
        parseArguments(args);

        terms = new HashMap<String, String>();
        
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
                parse(input);
            }
        } while ((!input.equals(Constants.QUIT_COMMAND)) && (!input.equals(Constants.EXIT_COMMAND)));
    }

    private static void displayAllTerms(){
        displayInfo(Constants.TERMS, Integer.toString(0) + Constants.TERMS_MESSAGE);
    }
    
    public static boolean stringIsSingleChar(String str, char ch) {
        if ((str != null) && (str.length() == 1)) { 
            return str.charAt(0) == ch;
        }
        return false;
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
        displayInfo(Constants.LOADING_FILE, filename);
        try {
            File file = new File(filename);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF8"));
            String line;
            
            while((line = bufferedReader.readLine()) != null)
            {
                line = line.trim();
                if (!line.isEmpty()) {
                    parseAndOutput(line);
                    termsParsed++;
                }
            }
            bufferedReader.close();
            displayInfo(Constants.LOADING_FILE, Integer.toString(termsParsed) + Constants.TERMS_PARSED);
        } catch (IOException e) {
            displayError(Constants.ERROR_UNABLE_OPEN_FILE + filename, e);
        }
    }
    
    private static void parseAndOutput(String input){
        Console.print(Constants.LAMBDA + Constants.PROMPT, Constants.PROMPT_COLOR);
        String[] tokens = Tokeniser.Tokenise(input);
        for (int i = 0; i < tokens.length; i++) {
            String token = tokens[i];
            if (stringIsSingleChar(token, Constants.LAMBDA)) {
                Console.print(token, Constants.LAMBDA_COLOR);                
            } else if ((stringIsSingleChar(token, Constants.OPEN_PARENTHESES)) ||
                       (stringIsSingleChar(token, Constants.CLOSE_PARENTHESES)) ||
                       (stringIsSingleChar(token, Constants.EQUALS)) ||
                       (stringIsSingleChar(token, Constants.PERIOD))) {
                Console.print(token, Constants.OPERATOR_COLOR);                        
            } else {
                Console.print(token, Constants.IDENTIFIER_COLOR);              
            }
        }
        Console.println();
    }

    private static void parse(String input) {
        String[] tokens = Tokeniser.Tokenise(input);
        for (int i = 0; i < tokens.length; i++) {
            String token = tokens[i];
            parse(token);
        }
    }
    
    private static void parse(String[] token) {
        
    }
    }
}
