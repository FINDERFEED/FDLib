package com.finderfeed.fdlib.systems.cutscenes;

import com.finderfeed.fdlib.util.rendering.FDEasings;

import java.util.function.Function;

public enum EasingType {

    LINEAR(i->i),
    EASE_IN(FDEasings::easeIn),
    EASE_OUT(FDEasings::easeOut),
    EASE_IN_OUT(FDEasings::easeInOut)

    ;


    Function<Float,Float> func;

    EasingType(Function<Float,Float> func){
        this.func = func;
    }

    public float apply(float p){
        return func.apply(p);
    }

}
