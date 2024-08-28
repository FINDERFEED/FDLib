package com.finderfeed.fdlib.shunting_yard;


import com.finderfeed.fdlib.shunting_yard.functions.*;
import com.finderfeed.fdlib.shunting_yard.sy_base.SYFunction;

import java.util.HashMap;

public class FunctionRegistry {


    public static final HashMap<String, SYFunction> FUNCTIONS = new HashMap<>();


    public static final SYFunction MAX = register("math.max", new MaxFunction());
    public static final SYFunction ABS = register("math.abs", new AbsFunction());
    public static final SYFunction ACOS = register("math.acos", new ACosFunction());
    public static final SYFunction ASIN = register("math.asin", new ASinFunction());
    public static final SYFunction ATAN2 = register("math.atan2", new Atan2Function());
    public static final SYFunction ATAN = register("math.atan", new AtanFunction());
    public static final SYFunction CEIL = register("math.ceil", new CeilFunction());
    public static final SYFunction CLAMP = register("math.clamp", new ClampFunction());
    public static final SYFunction COS = register("math.cos", new CosFunction());
    public static final SYFunction EXPONENT = register("math.exp", new ExponentFunction());
    public static final SYFunction FLOOR = register("math.floor", new FloorFunction());
    public static final SYFunction LERP = register("math.lerp", new LerpFunction());
    public static final SYFunction NATURAL_LOG = register("math.ln", new LnFunction());
    public static final SYFunction MIN = register("math.min", new MinFunction());
    public static final SYFunction POW = register("math.pow", new PowFunction());
    public static final SYFunction RANDOM = register("math.random", new RandomFunction());
    public static final SYFunction ROUND = register("math.round", new RoundFunction());
    public static final SYFunction SIN = register("math.sin", new SinFunction());
    public static final SYFunction SQRT = register("math.sqrt", new SQRTFunction());
    public static final SYFunction TRUNC = register("math.trunc", new TruncFunction());

    public static SYFunction getFunction(String name){
        return FUNCTIONS.get(name);
    }

    private static SYFunction register(String name,SYFunction functionSupplier){
        FUNCTIONS.put(name,functionSupplier);
        return functionSupplier;
    }

}
