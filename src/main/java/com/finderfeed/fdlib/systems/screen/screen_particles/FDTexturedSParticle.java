package com.finderfeed.fdlib.systems.screen.screen_particles;

import com.finderfeed.fdlib.util.math.ComplexEasingFunction;
import com.finderfeed.fdlib.util.rendering.FDEasings;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.util.Mth;
import org.joml.Matrix4f;

public abstract class FDTexturedSParticle<T extends FDTexturedSParticle<T>> extends FDScreenParticle<T>{

    private float maxQuadSize;
    private float currentQuadSize;
    private float quadSizeO;
    private ComplexEasingFunction scalingFunction = ComplexEasingFunction.builder().build();

    @Override
    public void render(GuiGraphics graphics, BufferBuilder vertex, float partialTicks) {
        PoseStack matrices = graphics.pose();

        double x = this.getX(partialTicks);
        double y = this.getY(partialTicks);
        float roll = this.getRoll(partialTicks);
        float halfQuadSize = this.getCurrentQuadSize(partialTicks) / 2;

        matrices.pushPose();
        matrices.translate(x,y,0);
        matrices.mulPose(Axis.ZN.rotationDegrees(roll));
        Matrix4f m = matrices.last().pose();

        vertex.addVertex(m,-halfQuadSize,-halfQuadSize,0).setUv(this.getU0(),this.getV0()).setColor(1f,1f,1f,1f).setLight(LightTexture.FULL_BRIGHT);
        vertex.addVertex(m,-halfQuadSize,halfQuadSize,0).setUv(this.getU0(),this.getV1()).setColor(1f,1f,1f,1f).setLight(LightTexture.FULL_BRIGHT);
        vertex.addVertex(m,halfQuadSize,halfQuadSize,0).setUv(this.getU1(),this.getV1()).setColor(1f,1f,1f,1f).setLight(LightTexture.FULL_BRIGHT);
        vertex.addVertex(m,halfQuadSize,-halfQuadSize,0).setUv(this.getU1(),this.getV0()).setColor(1f,1f,1f,1f).setLight(LightTexture.FULL_BRIGHT);

        matrices.popPose();
    }

    @Override
    public void tick() {
        super.tick();
        this.quadSizeO = currentQuadSize;
        this.currentQuadSize = scalingFunction.apply(this.getAge() / (float) this.getLifetime()) * maxQuadSize;
    }

    //full area is 1 in length
    public T setQuadScaleOptions(ComplexEasingFunction function){
        if (function.getLength() > 1.01){
            throw new RuntimeException("Area of easing function in textured particle should not be bigger than 1. ");
        }
        this.scalingFunction = function;
        return (T) this;
    }

    public T setDefaultScaleOut(){
        return this.setQuadScaleOptions(
                ComplexEasingFunction.builder()
                        .addArea(1, FDEasings::reversedLinear)
                        .build()
        );
    }

    public T setDefaultScaleInOut(){
        return this.setQuadScaleOptions(
                ComplexEasingFunction.builder()
                        .addArea(0.5f,FDEasings::easeOut)
                        .addArea(0.5f,FDEasings::reversedEaseOut)
                        .build()
        );
    }

    public T setDefaultScaleIn(){
        return this.setQuadScaleOptions(
                ComplexEasingFunction.builder()
                        .addArea(1f,FDEasings::linear)
                        .build()
        );
    }


    public T setMaxQuadSize(float size){
        this.maxQuadSize = size;
        return (T) this;
    }

    public float getCurrentQuadSize(float partialTicks){
        return Mth.lerp(partialTicks,quadSizeO, currentQuadSize);
    }

    public float getCurrentQuadSize() {
        return currentQuadSize;
    }

    public float getQuadSizeO() {
        return quadSizeO;
    }

    //override for atlases
    public float getU0(){
        return 0;
    }

    public float getV0(){
        return 0;
    }

    public float getU1(){
        return 1;
    }

    public float getV1(){
        return 1;
    }



}
