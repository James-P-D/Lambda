package Lambda;

public class DisplayMessage {
    // Display warning messages
    public static void Warning(String message) {
        Console.print(Constants.WARNING + ": ", Constants.WARNING_COLOR_1);
        Console.println(message, Constants.WARNING_COLOR_2);                
    }
    
    // Display error messages
    public static void Error(String message){
        Console.print(Constants.ERROR + ": ", Constants.ERROR_COLOR_1);
        Console.println(message, Constants.ERROR_COLOR_2);                
    }

    // Display error messages for exceptions
    public static void Error(String message, Exception e) {
        Error(message);
        Error(e.getMessage());                
    }

    // Display error messages for exceptions that appear on certain line in a script file
    public static void Error(String message, String line, Exception e) {
        Error(message);
        Error(line);
        Error(e.getMessage());                
    }
    
    // Display information messages
    public static void Info(String label, String message){
        Console.print(label + ": ", Constants.INFO_COLOR_1);
        Console.println(message, Constants.INFO_COLOR_2);
    }
    
    // Display debug messages
    public static void Debug(String message){
        Console.print(Constants.DEBUG + ": ", Constants.DEBUG_COLOR_1);
        Console.println(message, Constants.DEBUG_COLOR_2);
    }
}
