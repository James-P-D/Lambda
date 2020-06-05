package Lambda;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class Main {

    private static String LAMBDA_CHAR = "\u03BB"; 
    private static String ALPHA_CHAR = "\u03B1"; 
    private static String BETA_CHAR = "\u03B2";
    
    public static void main(String[] args) {
        parseArguments(args);
        
        /*        
        Console.println("u039B = \u039B", Console.Color.GREEN);
        Console.println("u03BB = \u03BB", Console.Color.RED, Console.Color.YELLOW_BACKGROUND);
        Console.println("u1D27 = \u1D27");
        Console.println("");

        Console.print("X", Console.Color.BLACK_BOLD);
        Console.print("X", Console.Color.WHITE);
        Console.print("X", Console.Color.WHITE_BOLD);
        Console.println("");

        Console.print(LAMBDA_CHAR + "> ", Console.Color.BLACK_BOLD);
        Console.println("");
        
        Console.print(ALPHA_CHAR + "> ", Console.Color.BLACK_BOLD);
        Console.println("");

        Console.print(BETA_CHAR + "> ", Console.Color.BLACK_BOLD);
        Console.println("");
        Console.println("");
             
        Console.print(LAMBDA_CHAR + "> ", Console.Color.BLACK_BOLD);
        String[] tokens = Tokeniser.Tokenise("mult = λm.λn.λf.m (n f)");
        for (int i = 0; i < tokens.length; i++) {
            String token = tokens[i];
            if (token.equals(LAMBDA_CHAR)) {
                Console.print(token, Console.Color.YELLOW_BOLD);                        
            } else if ((token.equals("(")) ||
                       (token.equals(")")) ||
                       (token.equals(".")) ||
                       (token.equals("="))) {
                Console.print(token, Console.Color.GREEN);                        
            } else {
                Console.print(token, Console.Color.WHITE_BOLD);              
            }
        }
        Console.println("");
        Console.println("");
        
        displayError("There was a problem!");
        */
    }

    private static void parseAndOutput(String input){
        Console.print(LAMBDA_CHAR + "> ", Console.Color.BLACK_BOLD);
        String[] tokens = Tokeniser.Tokenise(input);
        for (int i = 0; i < tokens.length; i++) {
            String token = tokens[i];
            if (token.equals(LAMBDA_CHAR)) {
                Console.print(token, Console.Color.YELLOW_BOLD);                        
            } else if (token.equals("(")) {
                Console.print(" " + token, Console.Color.GREEN);                        
            } else if (token.equals(")")) {
                Console.print(token, Console.Color.GREEN);                        
            } else if (token.equals("=")) {
                Console.print(" " + token + " ", Console.Color.GREEN);                        
            } else if (token.equals(".")) {
                Console.print(token, Console.Color.GREEN);                        
            } else {
                Console.print(token + "", Console.Color.WHITE_BOLD);              
            }
        }
        Console.println("");
    }
    
    private static void displayError(String message){
        Console.print("ERROR: ", Console.Color.RED);
        Console.print(message, Console.Color.RED_BRIGHT);
        Console.println();        
    }
    
    private static void parseArguments(String[] args) {        
        for (int i = 0; i < args.length; i++){
            String filename = args[i];
            
            Console.print("LOADING: ", Console.Color.BLUE);    
            Console.print(filename, Console.Color.BLUE_BRIGHT);
            Console.println();        
            try {
                File file = new File(filename);
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF8"));
                String line;
                while((line = bufferedReader.readLine()) != null)
                {
                    line = line.trim();
                    if (!line.isEmpty()) {
                        parseAndOutput(line);
                    }
                }
                bufferedReader.close();
            } catch (IOException e) {
                displayError("Unable to read from file: " + filename + "\n" + e.getMessage());
            }
        }
    }
}
