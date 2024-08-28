package com.finderfeed.fdlib.shunting_yard.functions;

import com.finderfeed.fdlib.shunting_yard.sy_base.SYFunction;

import java.util.List;

public class ExponentFunction extends SYFunction {

    public ExponentFunction() {
        super(1);
    }

    @Override
    public float compute(List<Float> args) {
        return (float) Math.exp(args.get(0));
    }
}
