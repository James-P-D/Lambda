package Lambda;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.win32.StdCallLibrary;
import com.sun.jna.*;
import com.sun.jna.platform.win32.WinDef.*;
import com.sun.jna.platform.win32.WinNT.HANDLE;

// Much of this code is butched from @author Sandy_Yin - https://stackoverflow.com/users/1157731/yin-shan
// (Especially the outputting of Unicode!)

public class Console {
    enum Color {
        //Color end string, color reset
        RESET                       ("\033[0m"),    // Reset to default

        // Regular Colors. Normal color, no bold, background color etc.
        BLACK                       ("\033[0;30m"), // BLACK
        RED                         ("\033[0;31m"), // RED
        GREEN                       ("\033[0;32m"), // GREEN
        YELLOW                      ("\033[0;33m"), // YELLOW
        BLUE                        ("\033[0;34m"), // BLUE
        MAGENTA                     ("\033[0;35m"), // MAGENTA
        CYAN                        ("\033[0;36m"), // CYAN
        WHITE                       ("\033[0;37m"), // WHITE

        // Bold
        BLACK_BOLD                  ("\033[1;30m"), // BLACK
        RED_BOLD                    ("\033[1;31m"), // RED
        GREEN_BOLD                  ("\033[1;32m"), // GREEN
        YELLOW_BOLD                 ("\033[1;33m"), // YELLOW
        BLUE_BOLD                   ("\033[1;34m"), // BLUE
        MAGENTA_BOLD                ("\033[1;35m"), // MAGENTA
        CYAN_BOLD                   ("\033[1;36m"), // CYAN
        WHITE_BOLD                  ("\033[1;37m"), // WHITE

        // Underline
        BLACK_UNDERLINED            ("\033[4;30m"), // BLACK
        RED_UNDERLINED              ("\033[4;31m"), // RED
        GREEN_UNDERLINED            ("\033[4;32m"), // GREEN
        YELLOW_UNDERLINED           ("\033[4;33m"), // YELLOW
        BLUE_UNDERLINED             ("\033[4;34m"), // BLUE
        MAGENTA_UNDERLINED          ("\033[4;35m"), // MAGENTA
        CYAN_UNDERLINED             ("\033[4;36m"), // CYAN
        WHITE_UNDERLINED            ("\033[4;37m"), // WHITE

        // Background
        BLACK_BACKGROUND            ("\033[40m"), // BLACK
        RED_BACKGROUND              ("\033[41m"), // RED
        GREEN_BACKGROUND            ("\033[42m"), // GREEN
        YELLOW_BACKGROUND           ("\033[43m"), // YELLOW
        BLUE_BACKGROUND             ("\033[44m"), // BLUE
        MAGENTA_BACKGROUND          ("\033[45m"), // MAGENTA
        CYAN_BACKGROUND             ("\033[46m"), // CYAN
        WHITE_BACKGROUND            ("\033[47m"), // WHITE

        // High Intensity
        BLACK_BRIGHT                ("\033[0;90m"), // BLACK
        RED_BRIGHT                  ("\033[0;91m"), // RED
        GREEN_BRIGHT                ("\033[0;92m"), // GREEN
        YELLOW_BRIGHT               ("\033[0;93m"), // YELLOW
        BLUE_BRIGHT                 ("\033[0;94m"), // BLUE
        MAGENTA_BRIGHT              ("\033[0;95m"), // MAGENTA
        CYAN_BRIGHT                 ("\033[0;96m"), // CYAN
        WHITE_BRIGHT                ("\033[0;97m"), // WHITE

        // Bold High Intensity
        BLACK_BOLD_BRIGHT           ("\033[1;90m"), // BLACK
        RED_BOLD_BRIGHT             ("\033[1;91m"), // RED
        GREEN_BOLD_BRIGHT           ("\033[1;92m"), // GREEN
        YELLOW_BOLD_BRIGHT          ("\033[1;93m"), // YELLOW
        BLUE_BOLD_BRIGHT            ("\033[1;94m"), // BLUE
        MAGENTA_BOLD_BRIGHT         ("\033[1;95m"), // MAGENTA
        CYAN_BOLD_BRIGHT            ("\033[1;96m"), // CYAN
        WHITE_BOLD_BRIGHT           ("\033[1;97m"), // WHITE

        // High Intensity backgrounds
        BLACK_BACKGROUND_BRIGHT     ("\033[0;100m"), // BLACK
        RED_BACKGROUND_BRIGHT       ("\033[0;101m"), // RED
        GREEN_BACKGROUND_BRIGHT     ("\033[0;102m"), // GREEN
        YELLOW_BACKGROUND_BRIGHT    ("\033[0;103m"), // YELLOW
        BLUE_BACKGROUND_BRIGHT      ("\033[0;104m"), // BLUE
        MAGENTA_BACKGROUND_BRIGHT   ("\033[0;105m"), // MAGENTA
        CYAN_BACKGROUND_BRIGHT      ("\033[0;106m"), // CYAN
        WHITE_BACKGROUND_BRIGHT     ("\033[0;107m"); // WHITE

        private final String code;

        Color(String code) {
            this.code = code;
        }

        @Override
        public String toString() {
            return code;
        }
    }
    
    private static Kernel32 INSTANCE = null;

    public interface Kernel32 extends StdCallLibrary {
        public Pointer GetStdHandle(int nStdHandle);
        public boolean WriteConsoleW(Pointer hConsoleOutput, char[] lpBuffer, int nNumberOfCharsToWrite, IntByReference lpNumberOfCharsWritten, Pointer lpReserved);
    }
        
    static {
        String os = System.getProperty("os.name").toLowerCase();
        if (os.startsWith("win")) {         
            INSTANCE = (Kernel32) Native.load("kernel32", Kernel32.class);
            
            Function GetStdHandleFunc = Function.getFunction("kernel32", "GetStdHandle");
            DWORD STD_OUTPUT_HANDLE = new DWORD(-11);
            HANDLE hOut = (HANDLE)GetStdHandleFunc.invoke(HANDLE.class, new Object[]{STD_OUTPUT_HANDLE});

            DWORDByReference p_dwMode = new DWORDByReference(new DWORD(0));
            Function GetConsoleModeFunc = Function.getFunction("kernel32", "GetConsoleMode");
            GetConsoleModeFunc.invoke(BOOL.class, new Object[]{hOut, p_dwMode});

            int ENABLE_VIRTUAL_TERMINAL_PROCESSING = 4;
            DWORD dwMode = p_dwMode.getValue();
            dwMode.setValue(dwMode.intValue() | ENABLE_VIRTUAL_TERMINAL_PROCESSING);
            Function SetConsoleModeFunc = Function.getFunction("kernel32", "SetConsoleMode");
            SetConsoleModeFunc.invoke(BOOL.class, new Object[]{hOut, dwMode});
        }
    }

    public static void println() {
        print_("", true, Color.RESET, Color.BLACK_BACKGROUND);
    }

    public static void print(String message) {
        print_(message, false, Color.RESET, Color.BLACK_BACKGROUND);
    }
    
    public static void println(String message) {
        print_(message, true, Color.RESET, Color.BLACK_BACKGROUND);
    }

    
    public static void print(String message, Color fontColor) {
        print_(message, false, fontColor, Color.BLACK_BACKGROUND);
    }
    
    public static void println(String message, Color fontColor) {
        print_(message, true, fontColor, Color.BLACK_BACKGROUND);
    }

    
    public static void print(String message, Color fontColor, Color backColor) {
        print_(message, false, fontColor, backColor);
    }
    
    public static void println(String message, Color fontColor, Color backColor) {
        print_(message, true, fontColor, backColor);
    }
    
    private static void print_(String message, Boolean newLine, Color fontColor, Color backColor) {
        boolean successful = false;
        if (INSTANCE != null) {
            System.out.print(fontColor);
            System.out.print(backColor);
            
            Pointer handle = INSTANCE.GetStdHandle(-11);
            char[] buffer = message.toCharArray();
            IntByReference lpNumberOfCharsWritten = new IntByReference();
            successful = INSTANCE.WriteConsoleW(handle, buffer, buffer.length, lpNumberOfCharsWritten, null);
            if(newLine && successful){
                System.out.println();
            }
            System.out.print(Color.RESET);                
        }
        if (!successful) {
            message = sanitise(message);
            System.out.print(message);                
            if (newLine) {
                System.out.println();
            }
        }
    }
    
    private static String sanitise(String message) {
        String sanitisedMessage = "";
        for (char ch: message.toCharArray()) {
            if (ch == Constants.ALPHA) {
                sanitisedMessage += Constants.ALPHA_SUBSTITUTE;
            } else if (ch == Constants.BETA) {
                sanitisedMessage += Constants.BETA_SUBSTITUTE;
            } else if (ch == Constants.ETA) {
                sanitisedMessage += Constants.ETA_SUBSTITUTE;
            } else if (ch == Constants.LAMBDA) {
                sanitisedMessage += Constants.LAMBDA_SUBSTITUTE;
            } else {
                sanitisedMessage += ch;
            }
        }
        return sanitisedMessage;
    }
    
    public static String readInput() throws IOException {        
        if (INSTANCE != null) {
            String retVal = "";
            
            int val = RawConsoleInput.read(false);
            // While not <ENTER>
            while(val != 13) {
                // If we read a valid character...
                if (val > 0 && val <= 122) {
                    //System.out.print(" "+val+" ");
                    // If <ESC> pressed..
                    if (val == 27) {
                        return Constants.QUIT_COMMAND;
                    } else if (val == 8) { // If <BACKSPACE> pressed
                        if (retVal.length() > 0) {
                            print(Character.toString((char)val));
                            print(Character.toString(' '));
                            print(Character.toString((char)val));
                            retVal = retVal.substring(0, retVal.length() - 1);
                        }
                    } else {
                        char ch = (char)val;
                        retVal += ch;
                        // If user enters a forward-slash, substitute it for Greek lambda character
                        if ((ch ==  Constants.LAMBDA_SUBSTITUTE) || (ch ==  Constants.LAMBDA)) {
                            // Display lambda in yellow
                            print(Character.toString(Constants.LAMBDA), Console.Color.YELLOW_BOLD);
                        } else if ((ch ==  Constants.PERIOD) ||
                                   (ch ==  Constants.EQUALS) ||
                                   (ch ==  Constants.OPEN_PARENTHESES) ||
                                   (ch ==  Constants.CLOSE_PARENTHESES)) {
                            // Display all other symbols in green
                            print(Character.toString(ch), Console.Color.GREEN);
                        } else {
                            // Display all variables, terms, etc. in white
                            print(Character.toString(ch), Console.Color.WHITE_BOLD);
                        }
                    }
                }
                val = RawConsoleInput.read(false);
            }
            println();
            
            return retVal;
        } else {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));            
            String input = "";
            input = br.readLine();
            return input;
        }
    }       
}