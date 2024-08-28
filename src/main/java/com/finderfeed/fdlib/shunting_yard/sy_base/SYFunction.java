package com.finderfeed.fdlib.shunting_yard.sy_base;

import java.util.List;

public abstract class SYFunction extends SYNode {

    private int argumentCount;

    public SYFunction(int argumentCount){
        this.argumentCount = argumentCount;
    }

    public abstract float compute(List<Float> args);


    public int getArgumentCount() {
        return argumentCount;
    }
}
