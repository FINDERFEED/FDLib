package com.finderfeed.fdlib.util.math;

import net.minecraft.util.Mth;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * <p>
 * This class is for chaining different easing functions. For example if you want an ease in followed by ease out.
 * Though you need to mind that each time the next easing function starts you are given a 0 - 1 range so in example you will
 * need to reverse the ease out function, so it starts at 1 and ends at 0.
 * </p>
 *
 * <p>
 * How does it work:
 * You add areas with lengths, and it places it one after another.
 * On each area you need to specify the function. The given value in Function<Float,Float> is a value from 0 to 1 -
 * a local percentage of area "completion".
 * </p>
 * <p>
 * Example:
 * You have 3 areas of length 1 1 and 3 and call apply(4.5).
 * </p>
 * <p>
 * 0-1-1---5
 * 4.5 is in the range of the third area and it will give your specified function the local percentage of this area completion:
 * (4.5 - 2 [the accumulated value where current area started]) [current area completion length] / 3 [length of current area] = 0.5 [area completion percentage]
 * This area completion percentage is the supplied to your specified function on the area.
 * </p>
 * <p>
 * What you give in "apply" method is the "coordinate" on area axis,
 * it means that if for example you have 3 areas of lengths 1 2 and 3 the whole length will be 6 and you should
 * give apply method a value between 0 and 6.
 * </p>
 * <p>
 * P.S. You can also add an area with a ComplexEasingFunction BUT it will consume not the area completion percent but current area completion length!
 * </p>
 *
 */
public class ComplexEasingFunction implements Function<Float,Float> {

    private List<EasingArea> areas = new ArrayList<>();

    private float length = 0;

    public float apply(float value){
        return this.apply((Float)value);
    }

    @Override
    public Float apply(Float value){
        if (length == 0 || areas.isEmpty()){
            return value;
        }
        value = Mth.clamp(value,0,length);
        EasingArea lastArea = areas.get(0);
        float accumulatedLength = 0;
        int idx = 1;
        while (true){
            float len = accumulatedLength + lastArea.length;
            if (len > value || idx >= areas.size()){
                break;
            }else{
                accumulatedLength = len;
                lastArea = areas.get(idx++);
            }
        }

        float localLength = value - accumulatedLength;

        float v;

        if (lastArea.easingFunction instanceof ComplexEasingFunction complexEasingFunction){
            v = lastArea.easingFunction.apply(localLength);
        }else{
            float localPercent = localLength / lastArea.length();
            v = lastArea.easingFunction.apply(localPercent);
        }

        return v;
    }

    public void addEasingArea(float length,Function<Float,Float> easing){
        this.length += length;
        this.areas.add(new EasingArea(length,easing));
    }

    public float getLength() {
        return length;
    }

    public static Builder builder(){
        return new Builder();
    }

    public static class Builder{

        private ComplexEasingFunction easingFunction = new ComplexEasingFunction();

        public Builder(){
        }

        public Builder addArea(float areaLength,Function<Float,Float> easing){
            easingFunction.addEasingArea(areaLength,easing);
            return this;
        }

        public ComplexEasingFunction build(){
            return easingFunction;
        }

    }


    private record EasingArea(float length, Function<Float,Float> easingFunction){}

}
