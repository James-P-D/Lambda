package Lambda;

import java.util.Map;

public class Parser {
    private static boolean isValidIdentifierName(String name) {
        String validFirstCharacter = "abcdefghijklmnopqrstuvwxyz_";
        String validRestCharacters = validFirstCharacter + "0123456789"; 
        
        if (name.length() == 0) {
            return false;
        }
        
        if (Constants.allCommands.contains(name)) {
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
    
    public static boolean IsTermDeclaration(String[] tokens) {
        return ((tokens.length >= 3) && (tokens[1].equals(Character.toString(Constants.EQUALS))));
    }
    
    public static String ParseTermDeclaration(String[] tokens, Map<String, LambdaExpression> terms) throws ParseException {
        String termName = tokens[0];
        if (!isValidIdentifierName(termName)) {
            throw new ParseException(String.format(Constants.ERROR_INVALID_IDENTIFIER_NAME, termName));
        }
        
        if (termAlreadyExists(termName, terms)) {
            DisplayMessage.Warning(String.format(Constants.WARNING_TERM_ALREADY_DEFINED, termName));
        }
        
        LambdaExpression expression = ParseExpression(tokens, new IntRef(2));
        terms.put(termName, expression);
        return termName;
    }
    
    private static boolean hasNextExpression(String[] tokens, IntRef index) {
        IntRef tempIndex = new IntRef(index.value);
        
        tempIndex.value++;
        while (tempIndex.value < (tokens.length - 1)) {
            String token = tokens[tempIndex.value];
            if ((!token.equals(Character.toString(Constants.OPEN_PARENTHESES))) && 
                (!token.equals(Character.toString(Constants.CLOSE_PARENTHESES)))) {
                return true;
            }
            tempIndex.value++;
        }
        
        return false;
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
            do {                                            
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
            } while (!token.equals(Character.toString(Constants.CLOSE_PARENTHESES)));
                
            if (rootExpression == null) {
                rootExpression = firstExpression;
            }
                
            if (rootExpression == null) {
                //TODO: Are we at the end of the list?
                throw new ParseException(Constants.ERROR_NOTHING_TO_PARSE);
            }
                
            return rootExpression;            
        } else {
            if (hasNextExpression(tokens, index)) {
                LambdaExpression firstExpression = parseLambdaName(tokens, index);
                index.value++;
                
                LambdaApplication rootExpression = new LambdaApplication(firstExpression, ParseExpression(tokens, index)); 
                
                return rootExpression;
            } else {
                return parseLambdaName(tokens, index);
            }
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
