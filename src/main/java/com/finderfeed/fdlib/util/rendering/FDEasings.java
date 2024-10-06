package com.finderfeed.fdlib.util.rendering;

import com.finderfeed.fdlib.util.math.ComplexEasingFunction;

public class FDEasings {


    public static float linear(float p){
        return p;
    }

    public static float reversedLinear(float p){
        return - p + 1;
    }

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

    //easings.net
    public static float easeOutBack(float x) {
        float c1 = 1.70158f;
        float c3 = c1 + 1;

        return 1 + c3 * (float)Math.pow(x - 1, 3) + c1 * (float)Math.pow(x - 1, 2);
    }
    public static float easeInOutBack(float x){
        float c1 = 1.70158f;
        float c2 = c1 * 1.525f;

        return x < 0.5
                ? ((float)Math.pow(2 * x, 2) * ((c2 + 1) * 2 * x - c2)) / 2
                : ((float)Math.pow(2 * x - 2, 2) * ((c2 + 1) * (x * 2 - 2) + c2) + 2) / 2;
    }


}
