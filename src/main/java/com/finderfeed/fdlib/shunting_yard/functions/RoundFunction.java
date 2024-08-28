package com.finderfeed.fdlib.shunting_yard.functions;

import com.finderfeed.fdlib.shunting_yard.sy_base.SYFunction;

import java.util.List;

public class RoundFunction extends SYFunction {

    public RoundFunction() {
        super(1);
    }

    @Override
    public float compute(List<Float> args) {
        return Math.round(args.get(0));
    }
}
