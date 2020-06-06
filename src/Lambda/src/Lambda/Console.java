package Lambda;

import java.io.IOException;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.win32.StdCallLibrary;
import com.sun.jna.*;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinDef.*;
import com.sun.jna.platform.win32.WinNT.HANDLE;
import com.sun.jna.Pointer;

// Much of this code is butched from @author Sandy_Yin - https://stackoverflow.com/users/1157731/yin-shan
// (Especially the outputting of Unicode!)

public class Console {
    private static interface Msvcrt extends Library {
        int _kbhit();
        int _getwch();
        int getwchar(); }
    
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
    
    public static String readInput() {
        String retVal = "";
        try {            
            int val = readWindows(false);
            while(val != 13) {
                if(val > 0) {
                    if (val == 27) {
                        return "";
                    }
                    retVal += (char)val;                        
                }
                val = readWindows(false);
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            return "ERROR";
        }
        
        return retVal;
    }
    

    
    
    
    
    
    private static Msvcrt        msvcrt;
    private static Kernel32      kernel32;
    private static Pointer       consoleHandle;
    private static int           originalConsoleMode;
    private static boolean                 initDone;
    private static boolean                 stdinIsConsole;
    private static boolean                 consoleModeAltered;
    private static final boolean           isWindows     = System.getProperty("os.name").startsWith("Windows");
    private static final int               invalidKey    = 0xFFFE;
    private static final String            invalidKeyStr = String.valueOf((char)invalidKey);
    
    static final int  STD_INPUT_HANDLE       = -10;
    static final long INVALID_HANDLE_VALUE   = -1;
    static final int  ENABLE_PROCESSED_INPUT = 0x0001;
    static final int  ENABLE_LINE_INPUT      = 0x0002;
    static final int  ENABLE_ECHO_INPUT      = 0x0004;
    static final int  ENABLE_WINDOW_INPUT    = 0x0008; 
    
    private static int readWindows (boolean wait) throws IOException {
        initWindows();
        if (!stdinIsConsole) {
           int c = msvcrt.getwchar();
           if (c == 0xFFFF) {
              c = -1; }
           return c; }
        consoleModeAltered = true;
        setConsoleMode(consoleHandle, originalConsoleMode & ~ENABLE_PROCESSED_INPUT);
           // ENABLE_PROCESSED_INPUT must remain off to prevent Ctrl-C from beeing processed by the system
           // while the program is not within getwch().
        if (!wait && msvcrt._kbhit() == 0) {
           return -2; }                                         // no key available
        return getwch(); }

     private static int getwch() {
        int c = msvcrt._getwch();
        if (c == 0 || c == 0xE0) {                              // Function key or arrow key
           c = msvcrt._getwch();
           if (c >= 0 && c <= 0x18FF) {
              return 0xE000 + c; }                              // construct key code in private Unicode range
           return invalidKey; }
        if (c < 0 || c > 0xFFFF) {
           return invalidKey; }
        return c; }                                             // normal key

     private static synchronized void initWindows() throws IOException {
        if (initDone) {
           return; }
        msvcrt = (Msvcrt)Native.loadLibrary("msvcrt", Msvcrt.class);
        kernel32 = (Kernel32)Native.loadLibrary("kernel32", Kernel32.class);
        try {
           consoleHandle = getStdInputHandle();
           originalConsoleMode = getConsoleMode(consoleHandle);
           stdinIsConsole = true; }
         catch (IOException e) {
           stdinIsConsole = false; }
        if (stdinIsConsole) {
           registerShutdownHook(); }
        initDone = true; }

     private static void registerShutdownHook() {
         Runtime.getRuntime().addShutdownHook( new Thread() {
            public void run() {
               shutdownHook(); }}); }

     public static void resetConsoleMode() throws IOException {
         if (isWindows) {
            resetConsoleModeWindows(); }
     }
     
      private static void shutdownHook() {
         try {
            resetConsoleMode(); }
          catch (Exception e) {}}
     
     private static Pointer getStdInputHandle() throws IOException {
        Pointer handle = kernel32.GetStdHandle(STD_INPUT_HANDLE);
        if (Pointer.nativeValue(handle) == 0 || Pointer.nativeValue(handle) == INVALID_HANDLE_VALUE) {
           throw new IOException("GetStdHandle(STD_INPUT_HANDLE) failed."); }
        return handle; }

     private static int getConsoleMode (Pointer handle) {
        IntByReference mode = new IntByReference();
        
        Function GetConsoleModeFunc = Function.getFunction("kernel32", "GetConsoleMode");
        GetConsoleModeFunc.invoke(BOOL.class, new Object[]{handle, mode});
        return mode.getValue();
     }

     private static void setConsoleMode (Pointer handle, int mode)  {
         Function SetConsoleModeFunc = Function.getFunction("kernel32", "SetConsoleMode");
        
         SetConsoleModeFunc.invoke(BOOL.class, new Object[]{handle, mode});
         }

     private static void resetConsoleModeWindows() throws IOException {
        if (!initDone || !stdinIsConsole || !consoleModeAltered) {
           return; }
        setConsoleMode(consoleHandle, originalConsoleMode);
        consoleModeAltered = false; }


    
}