package Lambda;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class Main {
    // Greek letters                               // Upper  Lower   
    private static String ALPHA_CHAR   = "\u03B1"; // \u0391 \u03B1 
    private static String BETA_CHAR    = "\u03B2"; // \u0392 \u03B2
    private static String ETA_CHAR     = "\u03B7"; // \u0397 \u03B7
    private static String LAMBDA_CHAR  = "\u03BB"; // \u039B \u03BB    
    
    public static void main(String[] args) {
        parseArguments(args);
        
        Console.print(ALPHA_CHAR + "> ", Console.Color.BLACK_BOLD); Console.println("");
        Console.print(BETA_CHAR + "> ", Console.Color.BLACK_BOLD); Console.println("");
        Console.print(ETA_CHAR + "> ", Console.Color.BLACK_BOLD); Console.println("");
        Console.print(LAMBDA_CHAR + "> ", Console.Color.BLACK_BOLD); Console.println("");
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
