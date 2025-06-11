package com.finderfeed.fdlib.util.rendering.renderers;

import com.finderfeed.fdlib.util.rendering.FDRenderUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.gui.screens.Overlay;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class QuadRenderer {

    private Vec3 direction = new Vec3(0,1,0);
    private Vector3f translation = new Vector3f(0,0,0);
    private VertexConsumer vertexConsumer;
    private PoseStack poseStack = new PoseStack();
    private float directionOffset = 0;
    private float rotation = 0;
    private float sizeX = 0.5f;
    private float sizeY = 0.5f;

    private int animationFrames = 0;
    private int currentAnimationFrame = 0;

    private int light = LightTexture.FULL_BRIGHT;
    private int overlay = OverlayTexture.NO_OVERLAY;

    private Vector4f color1 = new Vector4f(1,1,1,1);
    private Vector4f color2 = new Vector4f(1,1,1,1);
    private Vector4f color3 = new Vector4f(1,1,1,1);
    private Vector4f color4 = new Vector4f(1,1,1,1);

    private QuadRenderer(VertexConsumer vertexConsumer){
        this.vertexConsumer = vertexConsumer;
    }

    public static QuadRenderer start(VertexConsumer vertexConsumer){
        return new QuadRenderer(vertexConsumer);
    }

    public void render(){
        poseStack.pushPose();

        poseStack.translate(translation.x,translation.y,translation.z);
        FDRenderUtil.applyMovementMatrixRotations(poseStack, direction);
        poseStack.mulPose(Axis.YP.rotationDegrees(rotation));
        poseStack.translate(0,directionOffset,0);


        float u1 = 0;
        float v1 = 0;

        float u2 = 1;
        float v2 = 1;

        if (animationFrames != 0){

            float voffset = 1 / (float) animationFrames;

            float vstart = currentAnimationFrame / (float) animationFrames;
            float vend = vstart + voffset;

            v1 = vstart;
            v2 = vend;

        }


        Matrix4f matrix4f = poseStack.last().pose();
        vertexConsumer.addVertex(matrix4f,-sizeX,0,sizeY).setColor(color4.x,color4.y,color4.z,color4.w).setUv(u1,v2).setLight(light).setOverlay(OverlayTexture.NO_OVERLAY).setNormal((float)direction.x,(float)direction.y,(float)direction.z);
        vertexConsumer.addVertex(matrix4f,sizeX,0,sizeY).setColor(color3.x,color3.y,color3.z,color3.w).setUv(u2,v2).setLight(light).setOverlay(OverlayTexture.NO_OVERLAY).setNormal((float)direction.x,(float)direction.y,(float)direction.z);
        vertexConsumer.addVertex(matrix4f,sizeX,0,-sizeY).setColor(color2.x,color2.y,color2.z,color2.w).setUv(u2,v1).setLight(light).setOverlay(OverlayTexture.NO_OVERLAY).setNormal((float)direction.x,(float)direction.y,(float)direction.z);
        vertexConsumer.addVertex(matrix4f,-sizeX,0,-sizeY).setColor(color1.x,color1.y,color1.z,color1.w).setUv(u1,v1).setLight(light).setOverlay(OverlayTexture.NO_OVERLAY).setNormal((float)direction.x,(float)direction.y,(float)direction.z);



        poseStack.popPose();
    }


    public QuadRenderer setAnimated(int currentAnimationFrame, int animationFrameCount){
        if (animationFrameCount < 2){
            System.out.println("Cannot have less than 2 animation frames! It won't even be an animation...");
        }
        this.animationFrames = animationFrameCount;
        this.currentAnimationFrame = currentAnimationFrame;
        return this;
    }

    public QuadRenderer direction(Vec3 direction){
        this.direction = direction;
        return this;
    }

    public QuadRenderer size(float size){
        this.sizeX = size;
        this.sizeY = size;
        return this;
    }

    public QuadRenderer sizeX(float size){
        this.sizeX = size;
        return this;
    }

    public QuadRenderer sizeY(float size){
        this.sizeY = size;
        return this;
    }

    public QuadRenderer light(int light){
        this.light = light;
        return this;
    }

    public QuadRenderer overlay(int overlay){
        this.overlay = overlay;
        return this;
    }

    public QuadRenderer translate(float x, float y, float z){
        this.translation = new Vector3f(x,y,z);
        return this;
    }

    public QuadRenderer pose(PoseStack poseStack){
        this.poseStack = poseStack;
        return this;
    }

    public QuadRenderer rotationDegrees(float rotation){
        this.rotation = rotation;
        return this;
    }

    public QuadRenderer offsetOnDirection(float offset){
        this.directionOffset = offset;
        return this;
    }

    public QuadRenderer color1(float r,float g,float b,float a){
        this.color1.set(r,g,b,a);
        return this;
    }

    public QuadRenderer color2(float r,float g,float b,float a){
        this.color2.set(r,g,b,a);
        return this;
    }

    public QuadRenderer color3(float r,float g,float b,float a){
        this.color3.set(r,g,b,a);
        return this;
    }

    public QuadRenderer color4(float r,float g,float b,float a){
        this.color4.set(r,g,b,a);
        return this;
    }

    public QuadRenderer color(float r,float g,float b,float a){
        return this
                .color1(r,g,b,a)
                .color2(r,g,b,a)
                .color3(r,g,b,a)
                .color4(r,g,b,a);
    }
}
