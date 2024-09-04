package com.finderfeed.fdlib.util.rendering;

public class FDEasings {


    public static float easeInOut(float p){
        if (p <= 0.5){
            return 2 * p * p;
        }else{
            return -2 * (p - 1) * (p - 1) + 1;
        }
    }

    public static float easeIn(float p){
        return p * p;
    }


    public static float easeOut(float p){
        return 1 - (p - 1) * (p - 1);
    }



}
