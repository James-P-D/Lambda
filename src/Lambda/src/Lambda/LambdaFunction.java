package Lambda;

import java.util.List;
import java.util.Map;

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

    /*
    @Override
    public LambdaExpression Substitute(LambdaName replaceThis, LambdaExpression withThis) {
        if (this.name.GetName().equals(replaceThis.GetName())) {
            return expression.Substitute(replaceThis, withThis);
        } else {
            return new LambdaFunction((LambdaName)this.name.DeepClone(), this.expression.Substitute(replaceThis, withThis));
        }
    }*/
    
    @Override
    public LambdaExpression Substitute(List<LambdaFunction> replaceThis, List<LambdaExpression> withThis) {
        int index = -1;
        for(int i = 0; i < replaceThis.size(); i++) {
            if (replaceThis.get(i).GetName().GetName().equals(this.name.GetName())) {
                index = i;
                break;
            }
        }
        
        if (index == -1) {
            return new LambdaFunction((LambdaName)this.name.DeepClone(), this.expression.Substitute(replaceThis, withThis));
        } else {
            return expression.Substitute(replaceThis, withThis);
        }
    }
     
    @Override
    public LambdaExpression DeepClone() {
        return new LambdaFunction((LambdaName)this.name.DeepClone(), this.expression.DeepClone());
    }
    
    @Override
    public LambdaExpression DeepCloneAndExpand(Map<String, LambdaExpression> terms) {
        return new LambdaFunction((LambdaName)this.name.DeepCloneAndExpand(terms),
                                  this.expression.DeepCloneAndExpand(terms));
    }

}
