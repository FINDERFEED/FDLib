package com.finderfeed.fdlib.shunting_yard.functions;

import com.finderfeed.fdlib.shunting_yard.sy_base.SYFunction;

import java.util.List;

public class MinFunction extends SYFunction {

    public MinFunction() {
        super(2);
    }

    @Override
    public float compute(List<Float> args) {
        return Math.min(args.get(0),args.get(1));
    }
}
