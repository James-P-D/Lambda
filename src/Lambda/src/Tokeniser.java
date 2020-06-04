import java.util.ArrayList;

public class Tokeniser {
    private static final char COMMENT = '#';
    private static final char LAMBDA = 'λ';
    private static final char PERIOD = '.';
    private static final char EQUALS = '=';
    private static final char SPACE = ' ';
    //private static final 
    
    public static String[] Tokenise(String input){
        ArrayList<String> tokens = new ArrayList<String>();
        String commentsRemoved = RemoveComments(input);
        
        String token = "";
        for (char ch: commentsRemoved.toCharArray()) {
            if ((ch == LAMBDA) || (ch == PERIOD) || (ch == EQUALS) || (ch == SPACE)) {
                token = token.trim();
                if (!token.isEmpty()) {
                    tokens.add(token);
                    token = "";
                }
                
                tokens.add(Character.toString(ch));                
            }
            
        }
        token = token.trim();
        if (!token.isEmpty()) {
            tokens.add(token);
        }
        
        return tokens.toArray(new String[tokens.size()]);
    }
    
    private static String RemoveComments(String input){
        String commentsRemoved = "";
        for (char ch: input.toCharArray()) {
            if (ch == COMMENT) break;
            commentsRemoved += ch;
        }
        return commentsRemoved;
    }
    
}
