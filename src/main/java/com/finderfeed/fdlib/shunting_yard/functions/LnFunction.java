package com.finderfeed.fdlib.shunting_yard.functions;

import com.finderfeed.fdlib.shunting_yard.sy_base.SYFunction;

import java.util.List;

public class LnFunction extends SYFunction {

    public LnFunction() {
        super(1);
    }

    @Override
    public float compute(List<Float> args) {
        return (float) Math.log(args.get(0));
    }
}
