package Lambda;

import java.util.List;
import java.util.Map;

// Base class for all Lambda classes
abstract class LambdaExpression {
    public abstract String OutputString();
    
    public abstract LambdaExpression Substitute(List<LambdaFunction> replaceThis, List<LambdaExpression> withThis);
    
    public abstract LambdaExpression DeepClone();
    
    public abstract LambdaExpression DeepCloneAndExpand(Map<String, LambdaExpression> terms);
    
    //public abstract String OutputString(int nameCount);
}
