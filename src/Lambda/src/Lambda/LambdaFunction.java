package Lambda;

public class LambdaFunction extends LambdaExpression {
    private LambdaName name;
    private LambdaExpression expression;
    
    public LambdaFunction(LambdaName name, LambdaExpression expression) {
        this.name = name;
        this.expression = expression;
    }
    
    public LambdaName GetName() {
        return this.name;
    }
    
    public LambdaExpression GetExpression() {
        return this.expression;
    }
    
    @Override
    public String OutputString() {
        return Character.toString(Constants.LAMBDA) + this.name.OutputString() + Character.toString(Constants.PERIOD) + this.expression.OutputString();
    }

}
