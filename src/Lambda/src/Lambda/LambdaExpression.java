package Lambda;

import java.util.Map;

// Base class for all Lambda classes
abstract class LambdaExpression {
    public abstract String OutputString();

    public abstract String OutputIDString();

    public abstract LambdaExpression Substitute(LambdaFunction replaceThis, LambdaExpression withThis);
    
    public abstract LambdaExpression DeepClone();
    
    public abstract LambdaExpression DeepCloneAndExpand(Map<String, LambdaExpression> terms);
}
