package com.finderfeed.fdlib.shunting_yard.functions;

import com.finderfeed.fdlib.shunting_yard.sy_base.SYFunction;

import java.util.List;

public class PowFunction extends SYFunction {

    public PowFunction() {
        super(2);
    }

    @Override
    public float compute(List<Float> args) {
        return (float) Math.pow(args.get(0),args.get(1));
    }
}
