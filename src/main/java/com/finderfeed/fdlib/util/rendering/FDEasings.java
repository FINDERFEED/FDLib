package com.finderfeed.fdlib.util.rendering;

public class FDEasings {

    public static float squareHill(float p){
        return 1 - 4 * (float) Math.pow(p - 0.5,2);
    }

    public static float quadroHill(float p){
        return 1 - 16 * (float) Math.pow(p - 0.5,4);
    }

    public static float easeInOut(float p){
        if (p <= 0.5){
            return 2 * p * p;
        }else{
            return -2 * (p - 1) * (p - 1) + 1;
        }
    }

    public static float reversedEaseInOut(float p){
        return easeInOut(1 - p);
    }

    public static float easeIn(float p){
        return p * p;
    }


    public static float easeOut(float p){
        return 1 - (p - 1) * (p - 1);
    }

    public static float reversedEaseOut(float p){
         p = 1 - p;
        return 1 - (p - 1) * (p - 1);
    }



}
