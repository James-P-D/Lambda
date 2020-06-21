package Lambda;

import java.util.ArrayList;

public class Constants {
    // Symbols we will split on
    public static final char INCLUDE_FILE                                = '$';
    public static final char COMMENT                                     = '#';
    public static final char PERIOD                                      = '.';
    public static final char EQUALS                                      = '=';
    public static final char SPACE                                       = ' ';
    public static final char OPEN_PARENTHESES                            = '(';
    public static final char CLOSE_PARENTHESES                           = ')';

    // Colors for outputting info, warnings, errors etc.
    public static final Console.Color PROMPT_COLOR                       = Console.Color.BLACK_BOLD; 
    public static final Console.Color LAMBDA_COLOR                       = Console.Color.YELLOW_BOLD;
    public static final Console.Color OPERATOR_COLOR                     = Console.Color.GREEN;
    public static final Console.Color IDENTIFIER_COLOR                   = Console.Color.WHITE_BOLD;
    public static final Console.Color INFO_COLOR_1                       = Console.Color.CYAN;
    public static final Console.Color INFO_COLOR_2                       = Console.Color.CYAN_BRIGHT;
    public static final Console.Color WARNING_COLOR_1                    = Console.Color.YELLOW;
    public static final Console.Color WARNING_COLOR_2                    = Console.Color.YELLOW_BRIGHT;
    public static final Console.Color ERROR_COLOR_1                      = Console.Color.RED;
    public static final Console.Color ERROR_COLOR_2                      = Console.Color.RED_BRIGHT;
    public static final Console.Color DEBUG_COLOR_1                      = Console.Color.MAGENTA;
    public static final Console.Color DEBUG_COLOR_2                      = Console.Color.MAGENTA_BRIGHT;
    
    // Character codes for reading keypresses. Need to handle backspace manually
    // when running with 'FANCY_UI = true'
    public static final int CHAR_CODE_MIN_VALID                          = 0;
    public static final int CHAR_CODE_MAX_VALID                          = 122;
    public static final int CHAR_CODE_ESC                                = 27;
    public static final int CHAR_CODE_BACKSPACE                          = 8;
    public static final int CHAR_CODE_TAB                                = 9;
    public static final int CHAR_CODE_ENTER                              = 13;
    
    // Greek letters (and substitutes if we can't display)                           // Uppercase  Lowercase   
    public static final char ALPHA                                       = '\u03B1'; // \u0391     \u03B1
    public static final char ALPHA_UPPER                                 = '\u0391';
    public static final char ALPHA_SUBSTITUTE                            = 'A';
    public static final char BETA                                        = '\u03B2'; // \u0392     \u03B2
    public static final char BETA_UPPER                                  = '\u0392';
    public static final char BETA_SUBSTITUTE                             = 'B';
    public static final char ETA                                         = '\u03B7'; // \u0397     \u03B7
    public static final char ETA_UPPER                                   = '\u0397';
    public static final char ETA_SUBSTITUTE                              = 'E';
    public static final char LAMBDA                                      = '\u03BB'; // \u039B     \u03BB    
    public static final char LAMBDA_UPPER                                = '\u039B';    
    public static final char LAMBDA_SUBSTITUTE                           = '\\';
    
    // Commands
    public static final String QUIT_COMMAND                              = "quit";
    public static final String EXIT_COMMAND                              = "exit";
    public static final String ALPHA_COMMAND                             = "alpha";
    public static final String DEBUG_COMMAND                             = "debug";
    public static final String HELP_COMMAND                              = "help";
    public static final String TERMS_COMMAND                             = "terms";
    public static final String LOAD_COMMAND                              = "load";
    
    // Output strings
    public static final String PROMPT                                    = "> ";
    public static final String ON                                        = "ON";
    public static final String OFF                                       = "OFF";
    public static final String DEBUG_MODE                                = "Debug mode %s";
    public static final String QUITTING                                  = "QUITTING";
    public static final String QUIT_MESSAGE                              = "Bye!";
    public static final String DEBUG                                     = "DEBUG";
    public static final String WARNING                                   = "WARNING";
    public static final String ERROR                                     = "ERROR";
    public static final String LOADING_FILE                              = "LOADING FILE";
    public static final String TERMS_AND_EXPRESSIONS_PARSED              = "%s - %d term(s) and %d expression(s) parsed";
    public static final String HELP                                      = "HELP";
    public static final String TERMS                                     = "TERMS";
    public static final String TERMS_MESSAGE                             = "%d term(s) found";
    public static final String HELP_INFO_1                               = "Help information";
    public static final String HELP_INFO_2                               = "Commands: help            - this screen";
    public static final String HELP_INFO_3                               = "          terms           - display all known terms";
    public static final String HELP_INFO_4                               = "          debug           - toggle debug mode";
    public static final String HELP_INFO_5                               = "          load <filename> - loads file";
    public static final String HELP_INFO_6                               = "          alpha           - alpha-equivalence";
    public static final String HELP_INFO_7                               = "          exit/quit       - leave the application";
    public static final String LAMBDA_CALCULUS                           = LAMBDA + "-CALCULUS";
    public static final String LAMBDA_CALCULUS_INFO                      = "Type 'help' for more information";
    public static final String ALPHA_EQUIVALENCE                         = ALPHA + "-EQUIVALENCE";
    public static final String ALPHA_EQUIVALENCE_INFO                    = "Enter a number of expressions, each on a separate line, and then an empty line to begin comparison";
    
    // Error messages
    public static final String ERRORS_FOUND                              = "%s - %d error found";
    public static final String ERROR_FILE_DOES_NOT_EXIST                 = "%s - Does not exist";
    public static final String ERROR_UNABLE_OPEN_FILE                    = "%s - Unable to read";
    public static final String ERROR_PARSE_EXCEPTION_ON_LINE             = "%s - Parse exception on line %d";
    public static final String ERROR_MUST_PROVIDE_AT_LEAST_TWO_TERMS     = "Must provide at least two terms";
    public static final String ERROR_READING_FROM_STDIN                  = "Unable to read from stdin";
    public static final String ERROR_PARSE_EXCEPTION                     = "Parse exception";
    public static final String ERROR_INVALID_IDENTIFIER_NAME             = "Invalid name: %s";
    public static final String ERROR_BADLY_FORMATTED_FUNCTION            = "Badly formatted function";
    public static final String ERROR_EXPECTED_PERIOD_IN_FUNCTION         = "Expected period in function";
    public static final String ERROR_UNBALANCED_PARENTHESES              = "Unbalanced parentheses";
    public static final String ERROR_NOTHING_TO_PARSE                    = "Nothing to parse";
    public static final String ERROR_INCOMPLETE_TERM_DECLARATION         = "Incomplete term declaration";
    public static final String ERROR_INCOMPLETE_TERM_DECLARATION_ON_LINE = "Incomplete term declaration on line %d";
    
    
    // Warning messages
    public static final String WARNING_TERM_ALREADY_DEFINED              = "Term already defined: %s";
    public static final String WARNING_FILE_CONTAINS_NOTHING             = "%s - Contains no terms or expressions";
    
    // All commands. Users cannot create terms called 'help', 'load', etc.
    public static final ArrayList<String> reservedWords                  = new ArrayList<String>() {{
            add(QUIT_COMMAND);
            add(EXIT_COMMAND);
            add(ALPHA_COMMAND);
            add(DEBUG_COMMAND);
            add(HELP_COMMAND);
            add(TERMS_COMMAND);
            add(LOAD_COMMAND);
        }
        private static final long serialVersionUID = 1L;
    };    
    
    // All lines displayed when user enters 'help'
    public static final ArrayList<String> allHelpLines                   = new ArrayList<String>() {{
            add("HELP_INFO_1");
            add("HELP_INFO_2");
            add("HELP_INFO_3");
            add("HELP_INFO_4");
            add("HELP_INFO_5");
            add("HELP_INFO_6");
            add("HELP_INFO_7");
        }
        private static final long serialVersionUID = 1L;
    };              
}