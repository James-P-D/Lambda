package Lambda;

import java.util.ArrayList;

public class Tokeniser {
    // Main tokeniser method
    public static String[] Tokenise(String input){
        ArrayList<String> tokens = new ArrayList<String>();
        
        // Begin by removing comments (Anything after a '#' character until new line)
        String commentsRemoved = RemoveComments(input);
                
        String token = "";
        for (char ch: commentsRemoved.toCharArray()) {
            // If we've found a character we split on..
            if ((ch == Constants.LAMBDA) ||
                (ch == Constants.LAMBDA_SUBSTITUTE) ||
                (ch == Constants.PERIOD) ||
                (ch == Constants.EQUALS) ||
                (ch == Constants.SPACE) ||
                (ch == Constants.OPEN_PARENTHESES) ||
                (ch == Constants.CLOSE_PARENTHESES)) {
                
                // ..if the current token is not empty, then add it..
                token = token.trim();
                if (!token.isEmpty()) {
                    tokens.add(token);
                    token = "";
                }
                
                // ..and then if the character we split on is NOT a space,
                // then add the character
                if (ch != Constants.SPACE) {
                    // Remember to substitue '\' for a proper lambda character
                    if (ch == Constants.LAMBDA_SUBSTITUTE) {
                        tokens.add(Character.toString(Constants.LAMBDA));
                    } else {
                        tokens.add(Character.toString(ch));
                    }
                }
            } else {
                // In all other cases, just add the character to the current token
                token += ch;
            }            
        }
        
        // When there's nothing else left to process, check to see if the token
        // string is non-empty, and add it to the list if needed
        token = token.trim();
        if (!token.isEmpty()) {
            tokens.add(token);
        }
        
        // Finally, convert our list into an array and return it
        return tokens.toArray(new String[tokens.size()]);
    }
    
    // Remove comments from a line (anything following a '#' character)
    private static String RemoveComments(String input){
        String commentsRemoved = "";
        for (char ch: input.toCharArray()) {
            if (ch == Constants.COMMENT) break;
            commentsRemoved += ch;
        }
        return commentsRemoved;
    }    
}
