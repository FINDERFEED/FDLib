package com.finderfeed.fdlib.shunting_yard.functions;

import com.finderfeed.fdlib.shunting_yard.sy_base.SYFunction;

import java.util.List;

public class AbsFunction extends SYFunction {
    public AbsFunction() {
        super(1);
    }

    @Override
    public float compute(List<Float> args) {
        return Math.abs(args.get(0));
    }
}
