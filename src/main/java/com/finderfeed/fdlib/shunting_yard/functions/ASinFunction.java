package com.finderfeed.fdlib.shunting_yard.functions;

import com.finderfeed.fdlib.shunting_yard.sy_base.SYFunction;

import java.util.List;

public class ASinFunction extends SYFunction {

    public ASinFunction() {
        super(1);
    }

    @Override
    public float compute(List<Float> args) {
        return (float) Math.asin(Math.toRadians(args.get(0)));
    }
}
