package com.finderfeed.fdlib.shunting_yard.functions;

import com.finderfeed.fdlib.shunting_yard.sy_base.SYFunction;
import com.finderfeed.fdlib.util.math.FDMathUtil;

import java.util.List;

public class LerpFunction extends SYFunction {

    public LerpFunction() {
        super(3);
    }

    @Override
    public float compute(List<Float> args) {
        return FDMathUtil.lerp(args.get(0),args.get(1),args.get(2));
    }
}
