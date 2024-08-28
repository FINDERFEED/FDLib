package com.finderfeed.fdlib.shunting_yard.sy_base;

import java.util.HashMap;

public class ExpressionContext {

    public HashMap<String,Float> variables;

    public ExpressionContext(boolean init){
        if (init) {
            variables = new HashMap<>();
        }
    }

    public ExpressionContext addVariable(String name,Float v){
        variables.put(name,v);
        return this;
    }



}
