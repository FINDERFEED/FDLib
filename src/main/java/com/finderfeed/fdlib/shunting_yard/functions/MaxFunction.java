package com.finderfeed.fdlib.shunting_yard.functions;


import com.finderfeed.fdlib.shunting_yard.sy_base.SYFunction;

import java.util.List;

public class MaxFunction extends SYFunction {
    public MaxFunction() {
        super(2);
    }

    @Override
    public float compute(List<Float> args) {
        return Math.max(args.get(0),args.get(1));
    }

    @Override
    public String toString() {
        return "max(x,y)";
    }
}
