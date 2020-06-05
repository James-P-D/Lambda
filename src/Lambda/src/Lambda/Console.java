package Lambda;

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
        RESET("\033[0m"),

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
        System.out.print(fontColor);
        System.out.print(backColor);
        
        boolean successful = false;
        if (INSTANCE != null) {
            Pointer handle = INSTANCE.GetStdHandle(-11);
            char[] buffer = message.toCharArray();
            IntByReference lpNumberOfCharsWritten = new IntByReference();
            successful = INSTANCE.WriteConsoleW(handle, buffer, buffer.length, lpNumberOfCharsWritten, null);
            if(newLine && successful){
                System.out.print(Color.RESET);                
                System.out.println();
            }
        }
        if (!successful) {
            System.out.print(message);
            System.out.print(Color.RESET);                
            if (newLine) {
                System.out.println();
            }
        }
    }
}