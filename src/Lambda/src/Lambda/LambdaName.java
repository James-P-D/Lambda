package Lambda;

import java.util.List;
import java.util.Map;

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
    /*
    @Override
    public LambdaExpression Substitute(LambdaName replaceThis, LambdaExpression withThis) {
        if (this.name.equals(replaceThis.GetName())) {
            return withThis.DeepClone();
        } else {
            return this.DeepClone();
        }
    }
    */
    
    @Override
    public LambdaExpression Substitute(List<LambdaFunction> replaceThis, List<LambdaExpression> withThis) {
        int index = -1;
        for(int i=0; i< replaceThis.size(); i++) {
            if (replaceThis.get(i).GetName().GetName().equals(this.name)) {
                if (i < withThis.size()) {
                    index = i;
                }
                break;
            }
        }
        
        if (index == -1) {
            return this.DeepClone();
        } else {
            return withThis.get(index).DeepClone();
        }        
    }

    
    @Override
    public LambdaExpression DeepClone() {
        return new LambdaName(this.name);
    }
    
    @Override
    public LambdaExpression DeepCloneAndExpand(Map<String, LambdaExpression> terms) {
        if (terms.containsKey(this.name)) {
            return terms.get(this.name).DeepCloneAndExpand(terms);
        } else {
            return this.DeepClone();
        }
    }
}
