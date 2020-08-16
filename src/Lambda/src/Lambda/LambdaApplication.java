package Lambda;

import java.util.Map;

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
    
    @Override
    public String OutputIDString() {
        return ((firstExpression instanceof LambdaName) ? firstExpression.OutputIDString()
                                                        : (Constants.OPEN_PARENTHESES + firstExpression.OutputIDString() + Constants.CLOSE_PARENTHESES)) +
               Constants.SPACE +
               ((secondExpression instanceof LambdaName) ? secondExpression.OutputIDString()
                                                         : (Constants.OPEN_PARENTHESES + secondExpression.OutputIDString() + Constants.CLOSE_PARENTHESES));
    }
    
    @Override
    public LambdaExpression Substitute(LambdaFunction replaceThis, LambdaExpression withThis) {
        return new LambdaApplication(this.firstExpression.Substitute(replaceThis, withThis),
                                     this.secondExpression.Substitute(replaceThis, withThis));        
    }
    
    @Override
    public LambdaExpression DeepClone() {
        return new LambdaApplication(this.firstExpression.DeepClone(), this.secondExpression.DeepClone());
    }
    
    @Override
    public LambdaExpression DeepCloneAndExpand(Map<String, LambdaExpression> terms) {
        return new LambdaApplication(this.firstExpression.DeepCloneAndExpand(terms),
                                     this.secondExpression.DeepCloneAndExpand(terms));
    }
}
