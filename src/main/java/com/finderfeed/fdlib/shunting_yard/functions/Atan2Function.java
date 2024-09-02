package com.finderfeed.fdlib.shunting_yard.functions;

import com.finderfeed.fdlib.shunting_yard.sy_base.SYFunction;

import java.util.List;

public class Atan2Function extends SYFunction {

    public Atan2Function() {
        super(2);
    }

    @Override
    public float compute(List<Float> args) {
        return (float) Math.toDegrees(Math.atan2(args.get(0),args.get(1)));
    }
}
