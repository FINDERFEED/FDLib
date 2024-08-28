package com.finderfeed.fdlib.shunting_yard.functions;

import com.finderfeed.fdlib.shunting_yard.sy_base.SYFunction;

import java.util.List;

public class FloorFunction extends SYFunction {

    public FloorFunction() {
        super(1);
    }

    @Override
    public float compute(List<Float> args) {
        return (float) Math.floor(args.get(0));
    }
}
