package com.finderfeed.fdlib.shunting_yard.functions;

import com.finderfeed.fdlib.shunting_yard.sy_base.SYFunction;

import java.util.List;
import java.util.Random;

public class RandomFunction extends SYFunction {

    private static final Random rng = new Random();

    public RandomFunction() {
        super(2);
    }

    @Override
    public float compute(List<Float> args) {
        float between = args.get(1) - args.get(0);
        return args.get(0) + rng.nextFloat() * between;
    }
}
