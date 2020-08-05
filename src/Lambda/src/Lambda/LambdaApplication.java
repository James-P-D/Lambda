package Lambda;

public class LambdaApplication extends LambdaExpression {
    private LambdaExpression firstExpression;
    private LambdaExpression secondExpression;
    
    public LambdaApplication(LambdaExpression firstExpression, LambdaExpression secondExpression) {
        this.firstExpression = firstExpression;
        this.secondExpression = secondExpression;
    }
    
    public LambdaExpression GetFirstExpression() {
        return this.firstExpression;
    }
    
    public LambdaExpression GetSecondExpression() {
        return this.secondExpression;
    }
    
    @Override
    public String OutputString() {
        return ((firstExpression instanceof LambdaName) ? firstExpression.OutputString()
                                                        : (Constants.OPEN_PARENTHESES + firstExpression.OutputString() + Constants.CLOSE_PARENTHESES)) +
               Constants.SPACE +
               ((secondExpression instanceof LambdaName) ? secondExpression.OutputString()
                                                         : (Constants.OPEN_PARENTHESES + secondExpression.OutputString() + Constants.CLOSE_PARENTHESES));
    }
}
