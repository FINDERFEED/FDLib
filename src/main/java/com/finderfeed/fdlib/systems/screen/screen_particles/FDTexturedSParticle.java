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
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;

import java.util.function.Function;

public class FDTexturedSParticle extends FDScreenParticle<FDTexturedSParticle>{

    private float maxQuadSize = 1f;
    private float currentQuadSize;
    private float quadSizeO;
    private ComplexEasingFunction scalingFunction = ComplexEasingFunction.builder().build();
    private ParticleRenderType renderType;

    public FDTexturedSParticle(ParticleRenderType renderType){
        this.renderType = renderType;
    }

    public static FDTexturedSParticle create(ParticleRenderType renderType){
        return new FDTexturedSParticle(renderType);
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

        vertex.addVertex(m,-halfQuadSize,-halfQuadSize,0).setUv(this.getU0(),this.getV0()).setColor(1f,1f,1f,1f);
        vertex.addVertex(m,-halfQuadSize,halfQuadSize,0).setUv(this.getU0(),this.getV1()).setColor(1f,1f,1f,1f);
        vertex.addVertex(m,halfQuadSize,halfQuadSize,0).setUv(this.getU1(),this.getV1()).setColor(1f,1f,1f,1f);
        vertex.addVertex(m,halfQuadSize,-halfQuadSize,0).setUv(this.getU1(),this.getV0()).setColor(1f,1f,1f,1f);

        vertex.addVertex(m,halfQuadSize,-halfQuadSize,0).setUv(this.getU1(),this.getV0()).setColor(1f,1f,1f,1f);
        vertex.addVertex(m,halfQuadSize,halfQuadSize,0).setUv(this.getU1(),this.getV1()).setColor(1f,1f,1f,1f);
        vertex.addVertex(m,-halfQuadSize,halfQuadSize,0).setUv(this.getU0(),this.getV1()).setColor(1f,1f,1f,1f);
        vertex.addVertex(m,-halfQuadSize,-halfQuadSize,0).setUv(this.getU0(),this.getV0()).setColor(1f,1f,1f,1f);



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
        return Mth.lerp(partialTicks,quadSizeO, currentQuadSize);
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

    private static final Function<ResourceLocation, FDParticleRenderType> TEXTURES_BLUR_ADDITIVE = Util.memoize((location)->{
        return new FDParticleRenderType() {
            @Override
            public void end() {
                Minecraft.getInstance().getTextureManager().getTexture(location).restoreLastBlurMipmap();
            }

            @Nullable
            @Override
            public BufferBuilder begin(Tesselator tesselator, TextureManager textureManager) {


                RenderSystem.enableBlend();
                RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);

                RenderSystem.setShader(GameRenderer::getPositionTexColorShader);
                RenderSystem.setShaderTexture(0, location);
                Minecraft.getInstance().getTextureManager().getTexture(location).setBlurMipmap(true,true);

                return tesselator.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
            }
        };
    });

    private static final Function<ResourceLocation, FDParticleRenderType> TEXTURES_DEFAULT = Util.memoize((location)->{
        return new FDParticleRenderType() {
            @Override
            public void end() {
            }

            @Nullable
            @Override
            public BufferBuilder begin(Tesselator tesselator, TextureManager textureManager) {
                RenderSystem.enableBlend();
                RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                RenderSystem.setShader(GameRenderer::getPositionTexColorShader);
                RenderSystem.setShaderTexture(0, location);
                return tesselator.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
            }
        };
    });

    public static FDParticleRenderType createSimpleRenderType(ResourceLocation texture){
        return TEXTURES_DEFAULT.apply(texture);
    }

    public static FDParticleRenderType createBlurRenderType(ResourceLocation texture){
        return TEXTURES_BLUR_ADDITIVE.apply(texture);
    }
}
