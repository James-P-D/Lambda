package Lambda;


public class Main {

    public static void main(String[] args) {
        for (int i = 0; i< args.length; i++){
            System.out.println(args[i]);
        }
        
        String[] tokens = Tokeniser.Tokenise("true = λx.λy.x");
        for(int i=0; i<tokens.length; i++){
            System.out.println(tokens[i]);
        }
    }
}
