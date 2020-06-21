package Lambda;

import java.util.Map;

public class Parser {
    private static boolean isValidIdentifierName(String name) {
        String validFirstCharacter = "abcdefghijklmnopqrstuvwxyz_";
        String validRestCharacters = validFirstCharacter + "0123456789"; 
        
        if (name.length() == 0) {
            return false;
        }
        
        if (Constants.reservedWords.contains(name)) {
            return false;
        }
        
        if (!validFirstCharacter.contains(Character.toString(name.charAt(0)))) {
            return false;
        }

        for (int i = 1; i < name.length(); i++) {
            if (!validRestCharacters.contains(Character.toString(name.charAt(i)))) {
                return false;
            }            
        }
        
        return true;
    }
 
    private static boolean termAlreadyExists(String termName, Map<String, LambdaExpression> terms) {
        return terms.containsKey(termName);            
    }
    
    public static boolean IsTermDeclaration(String[] tokens) throws ParseException {
        return IsTermDeclaration(tokens, -1);
    }
    
    public static boolean IsTermDeclaration(String[] tokens, int lineNumber) throws ParseException {
        if ((tokens.length >= 2) && (tokens[1].equals(Character.toString(Constants.EQUALS)))) {
            if (tokens.length >= 3) {
                return true;
            }
            
            if (lineNumber == -1) {
                throw new ParseException(String.format(Constants.ERROR_INCOMPLETE_TERM_DECLARATION));
            }else{
                throw new ParseException(String.format(Constants.ERROR_INCOMPLETE_TERM_DECLARATION_ON_LINE, lineNumber));
            }
        }
        
        return false;
    }
    
    public static String ParseTermDeclaration(String[] tokens, Map<String, LambdaExpression> terms, boolean warnOnRedefinition) throws ParseException {
        String termName = tokens[0];
        if (!isValidIdentifierName(termName)) {
            throw new ParseException(String.format(Constants.ERROR_INVALID_IDENTIFIER_NAME, termName));
        }
        
        if (warnOnRedefinition) {
            if (termAlreadyExists(termName, terms)) {
                DisplayMessage.Warning(String.format(Constants.WARNING_TERM_ALREADY_DEFINED, termName));
            }            
        }
        
        LambdaExpression expression = ParseExpression(tokens, new IntRef(2));
        terms.put(termName, expression);
        return termName;
    }
    
    public static LambdaExpression ParseExpression(String[] tokens, IntRef index) throws ParseException {
        String token = tokens[index.value];
        //System.out.println(token);
        if ((token.equals(Character.toString(Constants.LAMBDA))) || (token.equals(Character.toString(Constants.LAMBDA_SUBSTITUTE)))) {
            index.value++;
            return parseLambdaFunction(tokens, index);
        } else if (token.equals(Character.toString(Constants.OPEN_PARENTHESES))) {
            LambdaExpression rootExpression = null;
            LambdaExpression firstExpression = null;
                
            index.value++;
            if(index.value == tokens.length) {
                throw new ParseException(Constants.ERROR_UNBALANCED_PARENTHESES);
            }
            token = tokens[index.value];
            while(!token.equals(Character.toString(Constants.CLOSE_PARENTHESES))) {                                            
                LambdaExpression exp = ParseExpression(tokens, index);
                if (firstExpression == null) {
                    firstExpression = exp;
                } else {
                    if (rootExpression == null) {
                        rootExpression = new LambdaApplication(firstExpression, exp);
                        firstExpression = exp;
                    } else {
                        firstExpression = new LambdaApplication(firstExpression, exp);
                        firstExpression = exp;
                    }
                }

                index.value++;
                if(index.value == tokens.length) {
                    throw new ParseException(Constants.ERROR_UNBALANCED_PARENTHESES);
                }
                
                token = tokens[index.value];
            }
                
            if (rootExpression == null) {
                rootExpression = firstExpression;
            }
                
            if (rootExpression == null) {
                //TODO: Are we at the end of the list?
                throw new ParseException(Constants.ERROR_NOTHING_TO_PARSE);
            }

            return rootExpression;            
        } else {
            return new LambdaName(token);
        }
    }
    
    private static LambdaFunction parseLambdaFunction(String[] tokens, IntRef index) throws ParseException {
        if (tokens.length < (index.value + 3)) {
            throw new ParseException(Constants.ERROR_BADLY_FORMATTED_FUNCTION);
        }

        LambdaName name = parseLambdaName(tokens, index);

        index.value++;
        if (!tokens[index.value].equals(Character.toString(Constants.PERIOD))) {
            throw new ParseException(Constants.ERROR_EXPECTED_PERIOD_IN_FUNCTION);
        }
                
        index.value++;
        return new LambdaFunction(name, ParseExpression(tokens, index));
    }
    
    private static LambdaName parseLambdaName(String[] tokens, IntRef index) throws ParseException {
        String name = tokens[index.value];
        if (!isValidIdentifierName(name)) {
            throw new ParseException(String.format(Constants.ERROR_INVALID_IDENTIFIER_NAME, name));
        }        

        return new LambdaName(name);
    }

}
