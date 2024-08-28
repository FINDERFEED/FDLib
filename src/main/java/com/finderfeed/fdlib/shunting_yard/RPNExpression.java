package com.finderfeed.fdlib.shunting_yard;

import com.finderfeed.fdlib.shunting_yard.sy_base.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class RPNExpression {

    private List<SYNode> nodes;


    public RPNExpression(List<SYNode> nodes){
        this.nodes = nodes;
    }

    public float compute(ExpressionContext ctx){

        Stack<SYNode> computation = new Stack<>();
        for (int i = 0; i < nodes.size();i++){
            SYNode node = nodes.get(i);
            if (node instanceof SYVariable variable){
                computation.push(variable);
            }else if (node instanceof SYFunction function){
                int args = function.getArgumentCount();
                List<Float> values = new ArrayList<>();
                for (int g = 0; g < args;g++){
                    SYVariable variable = (SYVariable) computation.pop();
                    values.add(0,variable.getValue(ctx));
                }
                float value = function.compute(values);
                computation.add(new SYStaticValue(value));
            }else{
                throw new RuntimeException("Wtf is this doing here.");
            }
        }
        if (computation.size() == 1){
            return ((SYStaticValue)computation.pop()).getValue(ctx);
        }else{
            throw new RuntimeException("Expression is incorrect");
        }
    }

}
