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
    
    public static void main(String[] args) {   
        terms = new HashMap<String, LambdaExpression>();
        boolean debugMode = false;
        parseArguments(args);

        displayInfo(Constants.LAMBDA_CALCULUS, Constants.LAMBDA_CALCULUS_INFO);

        String input = "";
        do {            
            Console.print(Constants.LAMBDA + Constants.PROMPT,  Constants.PROMPT_COLOR);        
            try {
                input = Console.readInput().toLowerCase().trim();
            } catch (IOException e) {
                displayError(Constants.ERROR_READING_FROM_STDIN, e);
                continue;
            }

            if (input.equals(Constants.ALPHA_COMMAND)) {
                displayInfo(Constants.ALPHA_EQUIVALENCE, Constants.ALPHA_EQUIVALENCE_INFO);
                ArrayList<String> alphas = new ArrayList<String>();
                int alphaCount = 1;
                do {
                    Console.print(Constants.ALPHA + Integer.toString(alphaCount) + Constants.PROMPT, Constants.PROMPT_COLOR);
                    try {
                        input = Console.readInput().trim();
                        // Just incase user tries to quit from alpha-equivalence loop..
                        if ((input.equals(Constants.QUIT_COMMAND)) || (input.equals(Constants.EXIT_COMMAND))) {
                            break;                        
                        }
                    } catch (IOException e) {
                        displayError(Constants.ERROR_READING_FROM_STDIN, e);
                        continue;
                    }
                    if (!input.equals("")) {
                        alphas.add(input);
                        alphaCount++;
                    }
                } while (!input.equals(""));
                if ((!input.equals(Constants.QUIT_COMMAND)) && (!input.equals(Constants.EXIT_COMMAND))) {
                    if (alphas.size() < 2) {
                        displayError(Constants.ERROR_MUST_PROVIDE_AT_LEAST_TWO_TERMS);
                    } else {
                        //TODO: Alpha equivalence here
                        Console.print(Constants.ALPHA + Constants.PROMPT, Constants.PROMPT_COLOR);
                        Console.println();
                    }
                }
            } else if (input.equals(Constants.DEBUG_COMMAND)) {
                debugMode = !debugMode;
                displayDebug(String.format(Constants.DEBUG_MODE, debugMode ? Constants.ON : Constants.OFF));
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
            } else {
                try {
                    String[] tokens = Tokeniser.Tokenise(input);
                    if (tokens.length > 0) {
                        if (isTermDeclaration(tokens)) {
                            String termName = parseTermDeclaration(tokens);
                            Console.print(Constants.BETA + Constants.PROMPT,  Constants.PROMPT_COLOR);
                            // TODO: Update to color!
                            System.out.println("TERM: "+ termName + " " + terms.get(termName).OutputString());
                        } else {
                            LambdaExpression expression = parseExpression(tokens, new IntRef(0));
                            Console.print(Constants.BETA + Constants.PROMPT,  Constants.PROMPT_COLOR);
                            // TODO: Update to color!
                            System.out.println("EXPRESION: " + expression.OutputString());
                        }
                    }
                } catch (ParseException e) {
                    displayError(Constants.ERROR_PARSE_EXCEPTION, e);
                }
            }
        } while ((!input.equals(Constants.QUIT_COMMAND)) && (!input.equals(Constants.EXIT_COMMAND)));
        displayInfo(Constants.QUITTING, Constants.QUIT_MESSAGE);
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
        displayInfo(Constants.TERMS, String.format(Constants.TERMS_MESSAGE, termsFound));
    }
    
    private static void displayWarning(String message) {
        Console.print(Constants.WARNING + ": ", Constants.WARNING_COLOR_1);
        Console.println(message, Constants.WARNING_COLOR_2);                
    }
    
    private static void displayError(String message){
        Console.print(Constants.ERROR + ": ", Constants.ERROR_COLOR_1);
        Console.println(message, Constants.ERROR_COLOR_2);                
    }

    private static void displayError(String message, Exception e) {
        displayError(message);
        displayError(e.getMessage());                
    }

    private static void displayError(String message, String line, Exception e) {
        displayError(message);
        displayError(line);
        displayError(e.getMessage());                
    }
    
    private static void displayInfo(String label, String message){
        Console.print(label + ": ", Constants.INFO_COLOR_1);
        Console.println(message, Constants.INFO_COLOR_2);
    }
    
    private static void displayDebug(String message){
        Console.print(Constants.DEBUG + ": ", Constants.DEBUG_COLOR_1);
        Console.println(message, Constants.DEBUG_COLOR_2);
    }    

    private static void parseArguments(String[] args) {        
        for (int i = 0; i < args.length; i++){
            String filename = args[i];
            loadFile(filename);
        }
    }

    private static void loadFile(String filename){
        int termsParsed = 0;
        int expressionsParsed = 0;        
        int errors = 0;
        int lineNumber = 0;
        
        displayInfo(Constants.LOADING_FILE, filename);
        try {
            File file = new File(filename);
            if (!file.exists()) {
                displayError(String.format(Constants.ERROR_FILE_DOES_NOT_EXIST, filename));
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
                        if (isTermDeclaration(tokens)) {
                            String termName = parseTermDeclaration(tokens);
                            System.out.println(terms.get(termName).OutputString());
                            termsParsed++;
                        } else {
                            LambdaExpression expression = parseExpression(tokens, new IntRef(0));
                            System.out.println(expression.OutputString());
                            expressionsParsed++;
                        }
                    }
                } catch (ParseException e) {
                    displayError(String.format(Constants.ERROR_PARSE_EXCEPTION_ON_LINE, lineNumber), line, e);
                    errors++;
                }
            }
            bufferedReader.close();
            if ((termsParsed == 0) && (expressionsParsed == 0)) {
                displayWarning(String.format(Constants.WARNING_FILE_CONTAINS_NOTHING, filename));
            } else {
                displayInfo(Constants.LOADING_FILE, String.format(Constants.TERMS_AND_EXPRESSIONS_PARSED, termsParsed, expressionsParsed));
            }
            if (errors > 0) {
                displayError(String.format(Constants.ERRORS_FOUND, errors));
            }
        } catch (IOException e) {
            displayError(String.format(Constants.ERROR_UNABLE_OPEN_FILE, filename), e);
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
        
        if (!validFirstCharacter.contains(Character.toString(name.charAt(0)))) {
            return false;
        }

        for (int i = 1; i < name.length(); i++) {
            if (!validRestCharacters.contains(Character.toString(name.charAt(i)))) {
                return false;
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
            throw new ParseException(String.format(Constants.ERROR_INVALID_IDENTIFIER_NAME, termName));
        }
        
        if (termAlreadyExists(termName)) {
            displayWarning(String.format(Constants.WARNING_TERM_ALREADY_DEFINED, termName));
        }
        
        LambdaExpression expression = parseExpression(tokens, new IntRef(2));
        terms.put(termName, expression);
        return termName;
    }
    
    private static boolean hasNextExpression(String[] tokens, IntRef index) {
        IntRef tempIndex = new IntRef(index.value);
        
        tempIndex.value++;
        while (tempIndex.value < (tokens.length - 1)) {
            String token = tokens[tempIndex.value];
            if ((!token.equals(Character.toString(Constants.OPEN_PARENTHESES))) && 
                (!token.equals(Character.toString(Constants.CLOSE_PARENTHESES)))) {
                return true;
            }
            tempIndex.value++;
        }
        
        return false;
    }
    
    private static LambdaExpression parseExpression(String[] tokens, IntRef index) throws ParseException {
        String token = tokens[index.value];
        //System.out.println(token);
        if ((token.equals(Character.toString(Constants.LAMBDA))) || (token.equals(Character.toString(Constants.LAMBDA_SUBSTITUTE)))) {
            index.value++;
            return parseLambdaFunction(tokens, index);
        } else if (token.equals(Character.toString(Constants.OPEN_PARENTHESES))) {
            LambdaExpression rootExpression = null;
            LambdaExpression firstExpression = null;
                
            index.value++;
            do {                                            
                LambdaExpression exp = parseExpression(tokens, index);
                if (firstExpression == null) {
                    firstExpression = exp;
                } else {
                    if (rootExpression == null) {
                        rootExpression = new LambdaApplication(firstExpression, exp);
                        firstExpression = exp;
                    } else {
                        firstExpression = new LambdaApplication(firstExpression, exp);
                        firstExpression = exp;
                    }
                }

                index.value++;
                if(index.value == tokens.length) {
                    throw new ParseException(Constants.ERROR_UNBALANCED_PARENTHESES);
                }
                
                token = tokens[index.value];
            } while (!token.equals(Character.toString(Constants.CLOSE_PARENTHESES)));
                
            if (rootExpression == null) {
                rootExpression = firstExpression;
            }
                
            if (rootExpression == null) {
                //TODO: Are we at the end of the list?
                throw new ParseException(Constants.ERROR_NOTHING_TO_PARSE);
            }
                
            return rootExpression;            
        } else {
            if (hasNextExpression(tokens, index)) {
                LambdaExpression firstExpression = parseLambdaName(tokens, index);
                index.value++;
                
                LambdaApplication rootExpression = new LambdaApplication(firstExpression, parseExpression(tokens, index)); 
                
                return rootExpression;
            } else {
                return parseLambdaName(tokens, index);
            }
        }
    }
    
    private static LambdaFunction parseLambdaFunction(String[] tokens, IntRef index) throws ParseException {
        if (tokens.length < (index.value + 3)) {
            throw new ParseException(Constants.ERROR_BADLY_FORMATTED_FUNCTION);
        }

        LambdaName name = parseLambdaName(tokens, index);

        index.value++;
        if (!tokens[index.value].equals(Character.toString(Constants.PERIOD))) {
            throw new ParseException(Constants.ERROR_EXPECTED_PERIOD_IN_FUNCTION);
        }
                
        index.value++;
        return new LambdaFunction(name, parseExpression(tokens, index));
    }
    
    private static LambdaName parseLambdaName(String[] tokens, IntRef index) throws ParseException {
        String name = tokens[index.value];
        if (!isValidIdentifierName(name)) {
            throw new ParseException(String.format(Constants.ERROR_INVALID_IDENTIFIER_NAME, name));
        }        

        return new LambdaName(name);
    }
}