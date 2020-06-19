package Lambda;

import java.util.ArrayList;

public class Constants {
    public static final char COMMENT                                 = '#';
    public static final char PERIOD                                  = '.';
    public static final char EQUALS                                  = '=';
    public static final char SPACE                                   = ' ';
    public static final char OPEN_PARENTHESES                        = '(';
    public static final char CLOSE_PARENTHESES                       = ')';

    public static final Console.Color PROMPT_COLOR                   = Console.Color.BLACK_BOLD; 
    public static final Console.Color LAMBDA_COLOR                   = Console.Color.YELLOW_BOLD;
    public static final Console.Color OPERATOR_COLOR                 = Console.Color.GREEN;
    public static final Console.Color IDENTIFIER_COLOR               = Console.Color.WHITE_BOLD;
    public static final Console.Color INFO_COLOR_1                   = Console.Color.CYAN;
    public static final Console.Color INFO_COLOR_2                   = Console.Color.CYAN_BRIGHT;
    public static final Console.Color WARNING_COLOR_1                = Console.Color.YELLOW;
    public static final Console.Color WARNING_COLOR_2                = Console.Color.YELLOW_BRIGHT;
    public static final Console.Color ERROR_COLOR_1                  = Console.Color.RED;
    public static final Console.Color ERROR_COLOR_2                  = Console.Color.RED_BRIGHT;
    public static final Console.Color DEBUG_COLOR_1                  = Console.Color.MAGENTA;
    public static final Console.Color DEBUG_COLOR_2                  = Console.Color.MAGENTA_BRIGHT;
    
    public static final int CHAR_CODE_ENTER                          = 13;
    public static final int CHAR_CODE_MIN_VALID                      = 0;
    public static final int CHAR_CODE_MAX_VALID                      = 122;
    public static final int CHAR_CODE_ESC                            = 27;
    public static final int CHAR_CODE_BACKSPACE                      = 8;
    
    // Greek letters (and substitutes if we can't display)                       // Uppercase  Lowercase   
    public static final char ALPHA                                   = '\u03B1'; // \u0391     \u03B1
    public static final char ALPHA_SUBSTITUTE                        = 'A';
    public static final char BETA                                    = '\u03B2'; // \u0392     \u03B2
    public static final char BETA_SUBSTITUTE                         = 'B';
    public static final char ETA                                     = '\u03B7'; // \u0397     \u03B7
    public static final char ETA_SUBSTITUTE                          = 'E';
    public static final char LAMBDA                                  = '\u03BB'; // \u039B     \u03BB    
    public static final char LAMBDA_SUBSTITUTE                       = '\\';
    
    public static final String QUIT_COMMAND                          = "quit";
    public static final String EXIT_COMMAND                          = "exit";
    public static final String ALPHA_COMMAND                         = "alpha";
    public static final String DEBUG_COMMAND                         = "debug";
    public static final String HELP_COMMAND                          = "help";
    public static final String TERMS_COMMAND                         = "terms";
    public static final String LOAD_COMMAND                          = "load";
    
    public static final String PROMPT                                = "> ";
    public static final String ON                                    = "On";
    public static final String OFF                                   = "Off";
    public static final String DEBUG_MODE                            = "DEBUG MODE";
    public static final String QUITTING                              = "QUITTING";
    public static final String QUIT_MESSAGE                          = "Bye!";
    public static final String DEBUG                                 = "DEBUG";
    public static final String WARNING                               = "WARNING";
    public static final String ERROR                                 = "ERROR";
    public static final String LOADING_FILE                          = "LOADING FILE";
    public static final String TERMS_AND_EXPRESSIONS_PARSED          = "%d term(s) and %d expression(s) parsed";
    public static final String HELP                                  = "HELP";
    public static final String TERMS                                 = "TERMS";
    public static final String TERMS_MESSAGE                         = "%d term(s) found";
    public static final String HELP_INFO_1                           = "Help information";
    public static final String HELP_INFO_2                           = "Commands: help            - this screen";
    public static final String HELP_INFO_3                           = "          terms           - display all known terms";
    public static final String HELP_INFO_4                           = "          debug           - toggle debug mode";
    public static final String HELP_INFO_5                           = "          load <filename> - loads file";
    public static final String HELP_INFO_6                           = "          alpha           - alpha-equivalence";
    public static final String HELP_INFO_7                           = "          exit/quit       - leave the application";
    public static final String LAMBDA_CALCULUS                       = LAMBDA + "-CALCULUS";
    public static final String LAMBDA_CALCULUS_INFO                  = "Type 'help' for more information";
    
    public static final String ERRORS_FOUND                          = " error found";
    public static final String ERROR_FILE_DOES_NOT_EXIST             = "File '%s' does not exist";
    public static final String ERROR_UNABLE_OPEN_FILE                = "Unable to read from file: %s";
    public static final String ERROR_MUST_PROVIDE_AT_LEAST_TWO_TERMS = "Must provide at least two terms";
    public static final String ERROR_READING_FROM_STDIN              = "Unable to read from stdin";
    public static final String ERROR_PARSE_EXCEPTION                 = "Parse exception";
    public static final String ERROR_PARSE_EXCEPTION_ON_LINE         = "Parse exception on line %d";
    public static final String ERROR_INVALID_IDENTIFIER_NAME         = "Invalid name: %s";
    public static final String ERROR_BADLY_FORMATTED_FUNCTION        = "Badly formatted function";
    public static final String ERROR_EXPECTED_PERIOD_IN_FUNCTION     = "Expected period in function";
    public static final String ERROR_UNBALANCED_PARENTHESES          = "Unbalanced parentheses";
    public static final String ERROR_NOTHING_TO_PARSE                = "Nothing to parse";    
        
    public static final String WARNING_TERM_ALREADY_DEFINED          = "Term already defined: %s";
    public static final String WARNING_FILE_CONTAINS_NOTHING         = "File '%s' contains no terms or expressions";
    
    public static final ArrayList<String> allCommands                = new ArrayList<String>() {{
            add("QUIT_COMMAND");
            add("EXIT_COMMAND");
            add("ALPHA_COMMAND");
            add("QUIT_COMMAND");
            add("DEBUG_COMMAND");
            add("HELP_COMMAND");
            add("TERMS_COMMAND");
            add("LOAD_COMMAND");
        }
        private static final long serialVersionUID = -3995135037673286855L;
    };    
}