package Lambda;

public class Constants {
    public static final char COMMENT            = '#';
    public static final char PERIOD             = '.';
    public static final char EQUALS             = '=';
    public static final char SPACE              = ' ';
    public static final char OPEN_PARENTHESES   = '(';
    public static final char CLOSE_PARENTHESES  = ')';

    // Greek letters (and substitutes if we can't display)  // Upper  Lower   
    public static final char ALPHA              = '\u03B1'; // \u0391 \u03B1
    public static final char ALPHA_SUBSTITUTE   = 'A';
    public static final char BETA               = '\u03B2'; // \u0392 \u03B2
    public static final char BETA_SUBSTITUTE    = 'B';
    public static final char ETA                = '\u03B7'; // \u0397 \u03B7
    public static final char ETA_SUBSTITUTE     = 'E';
    public static final char LAMBDA             = '\u03BB'; // \u039B \u03BB    
    public static final char LAMBDA_SUBSTITUTE  = '\\';
    
    public static final String QUIT_COMMAND     = "quit";
    public static final String EXIT_COMMAND     = "exit";
    public static final String ALPHA_COMMAND    = "alpha";
    public static final String DEBUG_COMMAND    = "debug";
    public static final String HELP_COMMAND     = "help";
    
    public static final String PROMPT                               = "> ";
    public static final String ON                                   = "On";
    public static final String OFF                                  = "Off";
    public static final String DEBUG_MODE                           = "DEBUG MODE";
    public static final String QUITTING                             = "QUITTING";
    public static final String QUIT_MESSAGE                         = "Bye!";
    public static final String ERROR                                = "ERROR";
    public static final String LOADING_FILE                         = "LOADING FILE";
    public static final String HELP                                 = "HELP";
    public static final String HELP_INFO_1                          = "blah blah blah";
    public static final String HELP_INFO_2                          = "Commands: help      - this screen";
    public static final String HELP_INFO_3                          = "          debug     - toggle debug mode";
    public static final String HELP_INFO_4                          = "          alpha     - ";
    public static final String HELP_INFO_5                          = "          exit/quit - leave the application";
    public static final String LAMBDA_CALCULUS                      = LAMBDA + "-CALCULUS";
    public static final String LAMBDA_CALCULUS_INFO                 = "Type 'help' for more information'";
    
    public static final String ERROR_UNABLE_OPEN_FILE               = "Unable to read from file: ";
    public static final String ERROR_MUST_PROVIDE_ATLEAST_TWO_TERMS = "Must provide atleast 2 terms";
    public static final String ERROR_READING_FROM_STDIN             = "Unable to read from from stdin: ";
}