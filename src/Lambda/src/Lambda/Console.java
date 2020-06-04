package Lambda;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.win32.StdCallLibrary;

/** For unicode output on windows platform
 * @author Sandy_Yin
 * https://stackoverflow.com/users/1157731/yin-shan
 */
public class Console {
    private static Kernel32 INSTANCE = null;

    public interface Kernel32 extends StdCallLibrary {
        public Pointer GetStdHandle(int nStdHandle);

        public boolean WriteConsoleW(Pointer hConsoleOutput, char[] lpBuffer,
            int nNumberOfCharsToWrite, IntByReference lpNumberOfCharsWritten, Pointer lpReserved);
    }
        
    static {
        String os = System.getProperty("os.name").toLowerCase();
        if (os.startsWith("win")) {         
            INSTANCE = (Kernel32) Native.load("kernel32", Kernel32.class);
        }
    }

    public static void print(String message) {
        print_(message, false);
    }
    
    public static void println(String message) {
        print_(message, true);
    }
    
    private static void print_(String message, Boolean newLine) {
        boolean successful = false;
        if (INSTANCE != null) {
            Pointer handle = INSTANCE.GetStdHandle(-11);
            char[] buffer = message.toCharArray();
            IntByReference lpNumberOfCharsWritten = new IntByReference();
            successful = INSTANCE.WriteConsoleW(handle, buffer, buffer.length, lpNumberOfCharsWritten, null);
            if(newLine && successful){
                System.out.println();
            }
        }
        if (!successful) {
            System.out.print(message);
            if (newLine) {
                System.out.println();
            }
        }
    }
}