package com.finderfeed.fdlib.shunting_yard.sy_base;

public class SYNamedVariable extends SYVariable {

    private String name;

    public SYNamedVariable(String name){
        this.name = name;
    }

    @Override
    public float getValue(ExpressionContext context) {
        if (context.variables.containsKey(name)){
            return context.variables.get(name);
        }else{
            return 0;
        }
    }

    @Override
    public String toString() {
        return name;
    }
}
