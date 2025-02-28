package com.finderfeed.fdlib.util;

import com.finderfeed.fdlib.util.math.FDMathUtil;
import net.minecraft.util.Mth;

import java.util.function.Function;

public class InterpolatedValue {

    private Function<Float,Float> easing;

    private int tickO = 0;
    private int tick = 0;

    private int time;

    public InterpolatedValue(int time,Function<Float,Float> easingFunction){
        this.easing = easingFunction;
        this.time = time;
    }

    public void tick(){
        tickO = tick;
        tick = Mth.clamp(tick + 1,0,time);
    }

    public float getValue(float pticks){
        return easing.apply(FDMathUtil.lerp(tickO,tick,pticks) / time);
    }

    public void reset(){
        tick = 0;
    }

    public int getTick() {
        return tick;
    }

    public Function<Float, Float> getEasing() {
        return easing;
    }

    public int getTime() {
        return time;
    }
}
