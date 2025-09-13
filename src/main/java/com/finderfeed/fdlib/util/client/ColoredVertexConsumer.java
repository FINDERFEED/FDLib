package com.finderfeed.fdlib.util.client;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;

public class ColoredVertexConsumer implements VertexConsumer {

    public int r;
    public int g;
    public int b;
    public int a;

    public VertexConsumer original;

    public ColoredVertexConsumer(VertexConsumer original, int r,int g,int b,int a){
        this.original = original;
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }



    @Override
    public VertexConsumer vertex(double px, double py, double pz) {
        return original.vertex(px,py,pz);
    }

    @Override
    public VertexConsumer color(int r, int g, int b, int a) {
        return original.color(this.r,this.g,this.b,this.a);
    }

    @Override
    public VertexConsumer uv(float u, float v) {
        return original.uv(u,v);
    }

    @Override
    public VertexConsumer overlayCoords(int i, int i1) {
        return original.overlayCoords(i,i1);
    }

    @Override
    public VertexConsumer overlayCoords(int p_86009_) {
        return original.overlayCoords(p_86009_);
    }

    @Override
    public VertexConsumer normal(float v, float v1, float v2) {
        return original.normal(v,v1,v2);
    }

    @Override
    public void endVertex() {
        original.endVertex();
    }

    @Override
    public void defaultColor(int i, int i1, int i2, int i3) {
        original.defaultColor(i,i1,i2,i3);
    }

    @Override
    public void unsetDefaultColor() {
        original.unsetDefaultColor();
    }

    @Override
    public VertexConsumer uv2(int p_85970_) {
        return original.uv2(p_85970_);
    }

    @Override
    public VertexConsumer uv2(int i, int i1) {
        return original.uv2(i,i1);
    }


    public static MultiBufferSource wrapBufferSource(MultiBufferSource source, int r, int g, int b, int a){
        return renderType->{
            return new ColoredVertexConsumer(source.getBuffer(renderType),r,g,b,a);
        };
    }

}
