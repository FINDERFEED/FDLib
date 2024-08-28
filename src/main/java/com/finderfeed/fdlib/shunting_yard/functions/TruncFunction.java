package com.finderfeed.fdlib.shunting_yard.functions;

import com.finderfeed.fdlib.shunting_yard.sy_base.SYFunction;

import java.util.List;

public class TruncFunction extends SYFunction {

    public TruncFunction() {
        super(1);
    }

    @Override
    public float compute(List<Float> args) {
        return (int) (float) args.get(0);
    }
}
