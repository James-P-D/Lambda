package Lambda;

public class LambdaName extends LambdaExpression {
    private String name;
    
    public LambdaName(String name) {
        
        this.name = name;
    }
    
    public String GetName() {
        return this.name;
    }
    
    @Override
    public String OutputString() {
        return this.name;
    }
}
