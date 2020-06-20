package Lambda;

// Wrapper for 'int'.
// Because 'int' is a primative type, it is always passed by value, so we cannot expect
// changes to its value to be retained after returning from a function. Since we'll be
// using an 'index' variable to keep track of the token we are processing when we parse
// a lambda-string, we'll need to use this class.
public class IntRef {
    public int value;
    
    public IntRef(int i) {
        this.value = i;
    }
}
