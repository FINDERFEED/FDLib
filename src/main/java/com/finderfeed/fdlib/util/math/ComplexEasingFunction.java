package com.finderfeed.fdlib.util.math;

import net.minecraft.util.Mth;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;


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
        EasingArea lastArea = areas.getFirst();
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
