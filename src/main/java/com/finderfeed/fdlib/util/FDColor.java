package com.finderfeed.fdlib.util;

import com.finderfeed.fdlib.util.math.FDMathUtil;
import com.finderfeed.fdlib.util.rendering.FDRenderUtil;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import org.joml.Math;

public class FDColor {

    public float r;
    public float g;
    public float b;
    public float a;

    public FDColor(float r,float g,float b,float a){
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }

    public int encode(){
        int r = FDMathUtil.clamp(Math.round(this.r * 255),0,255);
        int g = FDMathUtil.clamp(Math.round(this.g * 255),0,255);
        int b = FDMathUtil.clamp(Math.round(this.b * 255),0,255);
        int a = FDMathUtil.clamp(Math.round(this.a * 255),0,255);

        int color = (a << 24) + (r << 16) + (g << 8) + b;

        return color;
    }

    public static FDColor decode(int color){
        return new FDColor(
                ((color >> 16) & 0x00ff) / 255f,
                ((color >> 8) & 0x0000ff) / 255f,
                (color & 0x000000ff) / 255f,
                ((color >> 24) & 0xff) / 255f
        );
    }

}
