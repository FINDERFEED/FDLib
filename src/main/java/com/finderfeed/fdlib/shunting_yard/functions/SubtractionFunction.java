package com.finderfeed.fdlib.shunting_yard.functions;


import com.finderfeed.fdlib.shunting_yard.sy_base.SYFunction;

import java.util.List;

public class SubtractionFunction extends SYFunction {

    public SubtractionFunction() {
        super(2);
    }

    @Override
    public float compute(List<Float> args) {
        return args.get(0) - args.get(1);
    }

    @Override
    public String toString() {
        return "-";
    }
}
