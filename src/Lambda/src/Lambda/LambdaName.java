package Lambda;

import java.util.List;
import java.util.Map;

public class LambdaName extends LambdaExpression {
    private String name;
    private int id;
    
    public LambdaName(String name, int id) {        
        this.name = name;
        this.id = id;
    }
    
    public String GetName() {
        return this.name;
    }

    public int GetID() {
        return this.id;
    }

    @Override
    public String OutputString() {
        return this.name;
    }
    
    @Override
    public String OutputIDString() {
        return Integer.toString(this.id);
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
    public LambdaExpression Substitute(LambdaFunction replaceThis, LambdaExpression withThis) {
        if(replaceThis.GetName().GetID() == this.id) {
            return withThis.DeepClone();
        } else {
            return this.DeepClone();
        }
    }
    
    @Override
    public LambdaExpression DeepClone() {
        // TODO: Check if need a new ID! I suspect we might...
        return new LambdaName(this.name, this.id);
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
