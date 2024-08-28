package com.finderfeed.fdlib.shunting_yard.functions;

import com.finderfeed.fdlib.shunting_yard.sy_base.SYFunction;

import java.util.List;

public class CeilFunction extends SYFunction {

    public CeilFunction() {
        super(1);
    }

    @Override
    public float compute(List<Float> args) {
        return (float) Math.ceil(args.get(0));
    }
}
