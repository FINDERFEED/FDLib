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
    public VertexConsumer addVertex(float px, float py, float pz) {
        return original.addVertex(px,py,pz);
    }

    @Override
    public VertexConsumer setColor(int r, int g, int b, int a) {
        return original.setColor(this.r,this.g,this.b,this.a);
    }

    @Override
    public VertexConsumer setUv(float u, float v) {
        return original.setUv(u,v);
    }

    @Override
    public VertexConsumer setUv1(int u, int v) {
        return original.setUv1(u,v);
    }

    @Override
    public VertexConsumer setUv2(int u, int v) {
        return original.setUv2(u,v);
    }

    @Override
    public VertexConsumer setNormal(float px, float py, float pz) {
        return original.setNormal(px,py,pz);
    }

    public static MultiBufferSource wrapBufferSource(MultiBufferSource source, int r, int g, int b, int a){
        return renderType->{
            return new ColoredVertexConsumer(source.getBuffer(renderType),r,g,b,a);
        };
    }

}
