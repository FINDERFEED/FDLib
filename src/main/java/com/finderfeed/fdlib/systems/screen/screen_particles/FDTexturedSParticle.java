package com.finderfeed.fdlib.systems.screen.screen_particles;

import com.finderfeed.fdlib.FDLib;
import com.finderfeed.fdlib.systems.particle.FDParticleRenderType;
import com.finderfeed.fdlib.util.math.ComplexEasingFunction;
import com.finderfeed.fdlib.util.rendering.FDEasings;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Axis;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;

import java.util.function.Function;

public class FDTexturedSParticle extends FDScreenParticle<FDTexturedSParticle>{




    private float u0 = 0;
    private float v0 = 0;
    private float u1 = 1;
    private float v1 = 1;
    private float maxQuadSize = 10f;
    private float currentQuadSize;
    private float quadSizeO;
    private ComplexEasingFunction scalingFunction = ComplexEasingFunction.builder().build();
    private ParticleRenderType renderType;

    /**
     * Well, not a factory but a cache. Look into FDRenderUtil.ScreenParticleRenderTypes.
     */
    public FDTexturedSParticle(Function<ResourceLocation, ? extends ParticleRenderType> factory, ResourceLocation location){

        ParticleEngine engine = Minecraft.getInstance().particleEngine;
        var atlas = engine.textureAtlas;
        var atlasSprite = atlas.getSprite(location);

        if (atlasSprite != null){

            float width = engine.textureAtlas.width;
            float height = engine.textureAtlas.height;

            this.u0 = atlasSprite.getU0() + 1 / width;
            this.v0 = atlasSprite.getV0() + 1 / height;
            this.u1 = atlasSprite.getU1() - 1 / width;
            this.v1 = atlasSprite.getV1() - 1 / height;
            this.renderType = factory.apply(atlas.location());
        }else{
            this.renderType = factory.apply(location);
        }
    }

    public static FDTexturedSParticle create(Function<ResourceLocation, ? extends ParticleRenderType> factory, ResourceLocation location){
        return new FDTexturedSParticle(factory,location);
    }

    @Override
    public void render(GuiGraphics graphics, BufferBuilder vertex, float partialTicks) {
        PoseStack matrices = graphics.pose();

        float x = (float) this.getX(partialTicks);
        float y = (float) this.getY(partialTicks);
        float roll = this.getRoll(partialTicks);
        float halfQuadSize = this.getCurrentQuadSize(partialTicks) / 2;


        matrices.pushPose();
        matrices.translate(x,y,0);
        matrices.mulPose(Axis.ZN.rotationDegrees(roll));


        Matrix4f m = matrices.last().pose();

        vertex.vertex(m,-halfQuadSize,-halfQuadSize,0).uv(this.getU0(),this.getV0()).color(getR(),getG(),getB(),getA());
        vertex.vertex(m,-halfQuadSize,halfQuadSize,0).uv(this.getU0(),this.getV1()).color(getR(),getG(),getB(),getA());
        vertex.vertex(m,halfQuadSize,halfQuadSize,0).uv(this.getU1(),this.getV1()).color(getR(),getG(),getB(),getA());
        vertex.vertex(m,halfQuadSize,-halfQuadSize,0).uv(this.getU1(),this.getV0()).color(getR(),getG(),getB(),getA());



        matrices.popPose();
    }


    @Override
    public void tick() {
        super.tick();
        this.quadSizeO = currentQuadSize;
        this.currentQuadSize = scalingFunction.apply(this.getAge() / (float) this.getLifetime()) * maxQuadSize;
    }

    @Override
    public ParticleRenderType getParticleRenderType() {
        return renderType;
    }

    //full area is 1 in length
    public FDTexturedSParticle setQuadScaleOptions(ComplexEasingFunction function){
        if (function.getLength() > 1.01){
            throw new RuntimeException("Area of easing function in textured particle should not be bigger than 1. ");
        }
        this.scalingFunction = function;
        return this;
    }

    public FDTexturedSParticle setDefaultScaleOut(){
        return this.setQuadScaleOptions(
                ComplexEasingFunction.builder()
                        .addArea(1, FDEasings::reversedLinear)
                        .build()
        );
    }

    public FDTexturedSParticle setDefaultScaleInOut(){
        return this.setQuadScaleOptions(
                ComplexEasingFunction.builder()
                        .addArea(0.5f,FDEasings::easeOut)
                        .addArea(0.5f,FDEasings::reversedEaseOut)
                        .build()
        );
    }

    public FDTexturedSParticle setDefaultScaleIn(){
        return this.setQuadScaleOptions(
                ComplexEasingFunction.builder()
                        .addArea(1f,FDEasings::linear)
                        .build()
        );
    }


    public FDTexturedSParticle setMaxQuadSize(float size){
        this.maxQuadSize = size;
        return this;
    }

    public float getCurrentQuadSize(float partialTicks){
//        if (quadSizeO < currentQuadSize) {
//            return Mth.lerp(partialTicks, quadSizeO, currentQuadSize);
//        }else{
//            return Mth.lerp(1 - partialTicks, quadSizeO,currentQuadSize);
//        }
        return Mth.lerp(partialTicks,quadSizeO,currentQuadSize);
    }

    @Override
    public void onAddedToEngine() {
        this.currentQuadSize = scalingFunction.apply(0) * maxQuadSize;
        this.quadSizeO = currentQuadSize;
    }

    public float getCurrentQuadSize() {
        return currentQuadSize;
    }

    public float getQuadSizeO() {
        return quadSizeO;
    }

    //override for atlases
    public float getU0(){
        return u0;
    }

    public float getV0(){
        return v0;
    }

    public float getU1(){
        return u1;
    }

    public float getV1(){
        return v1;
    }


}
