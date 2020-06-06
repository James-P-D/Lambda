package Lambda;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {
    
    public static void main(String[] args) {
        parseArguments(args);
        
        Console.println(Constants.ALPHA + "> ", Console.Color.BLACK_BOLD);
        Console.println(Constants.BETA + "> ", Console.Color.BLACK_BOLD);
        Console.println(Constants.ETA + "> ", Console.Color.BLACK_BOLD);
        Console.println(Constants.LAMBDA + "> ", Console.Color.BLACK_BOLD);
    }

    public static boolean isSame(String s, char c) {
        if (s != null && s.length() == 1) { 
            return s.charAt(0) == c;
        }
        return false;
    }
    
    private static void parseAndOutput(String input){
        Console.print(Constants.LAMBDA + "> ", Console.Color.BLACK_BOLD);
        String[] tokens = Tokeniser.Tokenise(input);
        for (int i = 0; i < tokens.length; i++) {
            String token = tokens[i];
            if (isSame(token, Constants.LAMBDA)) {
                Console.print(token, Console.Color.YELLOW_BOLD);                
            } else if ((isSame(token, Constants.OPEN_PARENTHESES)) ||
                       (isSame(token, Constants.CLOSE_PARENTHESES)) ||
                       (isSame(token, Constants.EQUALS)) ||
                       (isSame(token, Constants.PERIOD))) {
                Console.print(token, Console.Color.GREEN);                        
            } else {
                Console.print(token + "", Console.Color.WHITE_BOLD);              
            }
        }
        Console.println("");
    }
    
    private static void displayError(String message){
        Console.print("ERROR: ", Console.Color.RED);
        Console.println(message, Console.Color.RED_BRIGHT);                
    }

    private static void displayInfo(String label, String message){
        Console.print(label + ": ", Console.Color.BLUE);
        Console.println(message, Console.Color.BLUE_BRIGHT);
    }
    
    private static void parseArguments(String[] args) {        
        for (int i = 0; i < args.length; i++){
            String filename = args[i];
            
            displayInfo("LOADING", filename);
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
