package com.finderfeed.fdlib.shunting_yard.functions;

import com.finderfeed.fdlib.shunting_yard.sy_base.SYFunction;

import java.util.List;

public class SinFunction extends SYFunction {

    public SinFunction() {
        super(1);
    }

    @Override
    public float compute(List<Float> args) {
        return (float) Math.sin(Math.toRadians(args.get(0)));
    }
}
