package com.finderfeed.fdlib.util;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

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

}
