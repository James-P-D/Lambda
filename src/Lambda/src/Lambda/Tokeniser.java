package Lambda;

import java.util.ArrayList;

public class Tokeniser {
    public static String[] Tokenise(String input){
        ArrayList<String> tokens = new ArrayList<String>();
        String commentsRemoved = RemoveComments(input);
        
        String token = "";
        for (char ch: commentsRemoved.toCharArray()) {
            if ((ch == Constants.LAMBDA) ||
                (ch == Constants.LAMBDA_SUBSTITUTE) ||
                (ch == Constants.PERIOD) ||
                (ch == Constants.EQUALS) ||
                (ch == Constants.SPACE) ||
                (ch == Constants.OPEN_PARENTHESES) ||
                (ch == Constants.CLOSE_PARENTHESES)) {
                token = token.trim();
                if (!token.isEmpty()) {
                    tokens.add(token);
                    token = "";
                }
                
                if (ch != Constants.SPACE) {
                    if (ch == Constants.LAMBDA_SUBSTITUTE) {
                        tokens.add(Character.toString(Constants.LAMBDA));
                    } else {
                        tokens.add(Character.toString(ch));
                    }
                }
            } else {
                token += ch;
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
            if (ch == Constants.COMMENT) break;
            commentsRemoved += ch;
        }
        return commentsRemoved;
    }    
}
