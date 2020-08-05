package Lambda;

import java.util.Map;

public class Parser {
    // Check if string is a valid identifier name
    private static boolean isValidIdentifierName(String name) {
        String validFirstCharacter = "abcdefghijklmnopqrstuvwxyz_";
        String validRestCharacters = validFirstCharacter + "0123456789"; 
        
        // Check identifier is not empty
        if (name.length() == 0) {
            return false;
        }
        
        // Check it's not a reserved word ('help', 'terms', etc.)
        if (Constants.reservedWords.contains(name)) {
            return false;
        }
        
        // Check the first character is a letter or underscore
        if (!validFirstCharacter.contains(Character.toString(name.charAt(0)))) {
            return false;
        }

        // Check all remaining characters are letters, underscores or numbers
        for (int i = 1; i < name.length(); i++) {
            if (!validRestCharacters.contains(Character.toString(name.charAt(i)))) {
                return false;
            }
        }
        
        // If we get here, identifier is fine
        return true;
    }
 
    // Check if term name is already taken (redefining terms isn't a problem, but
    // we will display warning messages in case user accidently overwrites a term
    private static boolean termAlreadyExists(String termName, Map<String, LambdaExpression> terms) {
        return terms.containsKey(termName);            
    }
    
    // Check if tokens consist of a term declaration ('name = something')
    // Note this function is always called as the result of user input, not
    // as the result of loading files
    public static boolean IsTermDeclaration(String[] tokens) throws ParseException {
        // Call IsTermDeclaration passing 'lineNumber' as -1 to specify that
        // we are *not* calling from loading a file
        return IsTermDeclaration(tokens, -1);
    }
    
    // Check if we have a term. Note we include a 'lineNumber' field for error
    // messages if we are loading from a file
    public static boolean IsTermDeclaration(String[] tokens, int lineNumber) throws ParseException {
        // Check we have at least 2 tokens, and that the second is '='
        if ((tokens.length >= 2) && (tokens[1].equals(Character.toString(Constants.EQUALS)))) {
            // Valid term declaration will include at least 3 tokens ('name = something')
            if (tokens.length >= 3) {
                return true;
            }
            
            // If we found an equals sign, but not enough tokens (i.e. 'name =') then
            // throw and error. If 'lineNumber' is -1 then we are being called from
            // the console, so no need to specify a line number
            if (lineNumber == -1) {
                throw new ParseException(String.format(Constants.ERROR_INCOMPLETE_TERM_DECLARATION));
            } else {
                throw new ParseException(String.format(Constants.ERROR_INCOMPLETE_TERM_DECLARATION_ON_LINE, lineNumber));
            }
        }
        
        return false;
    }
    
    // Parse a term ('name = something')
    public static String ParseTermDeclaration(String[] tokens, Map<String, LambdaExpression> terms, boolean warnOnRedefinition) throws ParseException {
        // Get the term-name first 
        String termName = tokens[0];
        
        // Check the name is valid (not 'help', etc.)
        if (!isValidIdentifierName(termName)) {
            throw new ParseException(String.format(Constants.ERROR_INVALID_IDENTIFIER_NAME, termName));
        }
        
        // Sometimes we don't want to warn about overwriting terms (like when loading terms
        // from files within files.)
        if (warnOnRedefinition) {
            if (termAlreadyExists(termName, terms)) {
                DisplayMessage.Warning(String.format(Constants.WARNING_TERM_ALREADY_DEFINED, termName));
            }            
        }
        
        // Parse the expression after the equals sign
        LambdaExpression expression = StartParseExpression(tokens, new IntRef(2));

        // Add the expression to the hash-table of all terms
        terms.put(termName, expression);
        
        // Finally, return the term name
        return termName;
    }
    
    public static LambdaExpression StartParseExpression(String[] tokens) throws ParseException {
        IntRef parenthesesDepth = new IntRef(0);
        LambdaExpression expression = ParseExpression(tokens, new IntRef(0), parenthesesDepth);
        if (parenthesesDepth.value != 0) {
            throw new ParseException(Constants.ERROR_UNBALANCED_PARENTHESES);
        }
        return expression;
    }

    public static LambdaExpression StartParseExpression(String[] tokens, IntRef index) throws ParseException {
        IntRef parenthesesDepth = new IntRef(0);
        LambdaExpression expression = ParseExpression(tokens, index, parenthesesDepth);
        if (parenthesesDepth.value != 0) {
            throw new ParseException(Constants.ERROR_UNBALANCED_PARENTHESES);
        }
        return expression;
    }

    private static LambdaExpression ParseExpression(String[] tokens, IntRef index, IntRef parenthesesDepth) throws ParseException {
        LambdaExpression currentExpression = null;
        while (index.value < tokens.length) {
            
            String token = tokens[index.value]; 
            
            if ((token.equals(Character.toString(Constants.LAMBDA))) || (token.equals(Character.toString(Constants.LAMBDA_SUBSTITUTE)))) {
                index.value++;
                
                if (tokens.length < (index.value + 3)) {
                    throw new ParseException(Constants.ERROR_BADLY_FORMATTED_FUNCTION);
                }

                LambdaName name = parseLambdaName(tokens, index);

                index.value++;
                if (!tokens[index.value].equals(Character.toString(Constants.PERIOD))) {
                    throw new ParseException(Constants.ERROR_EXPECTED_PERIOD_IN_FUNCTION);
                }
                        
                index.value++;
                currentExpression = new LambdaFunction(name, ParseExpression(tokens, index, parenthesesDepth));
            } else if (token.equals(Character.toString(Constants.OPEN_PARENTHESES))) {
                parenthesesDepth.value++;
                index.value++;
                LambdaExpression nextExpression = ParseExpression(tokens, index, parenthesesDepth);
                if (nextExpression != null) {
                    if (currentExpression == null) {
                        currentExpression = nextExpression;
                    } else {
                        LambdaApplication tempExpression = new LambdaApplication(currentExpression, nextExpression); 
                        currentExpression = tempExpression;
                    }                    
                }
            } else if (token.equals(Character.toString(Constants.CLOSE_PARENTHESES))) {
                parenthesesDepth.value--;
                index.value++;
                return currentExpression;
            } else {
                LambdaName name = parseLambdaName(tokens, index);
                index.value++;
                if (currentExpression == null) {
                    currentExpression = name;
                } else {
                    LambdaApplication tempExpression = new LambdaApplication(currentExpression, name); 
                    currentExpression = tempExpression;
                }
            }
        }

        return currentExpression;
    }
    
    
    // Parse a lambda name
    private static LambdaName parseLambdaName(String[] tokens, IntRef index) throws ParseException {
        String name = tokens[index.value];
        if (!isValidIdentifierName(name)) {
            throw new ParseException(String.format(Constants.ERROR_INVALID_IDENTIFIER_NAME, name));
        }        

        return new LambdaName(name);
    }
}
