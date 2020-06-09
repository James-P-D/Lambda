package Lambda;

public class ParseException extends Exception {

    // TODO: Do we need this?
    //public ParseException() {}

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public ParseException(String message)
    {
       super(message);
    }
}
