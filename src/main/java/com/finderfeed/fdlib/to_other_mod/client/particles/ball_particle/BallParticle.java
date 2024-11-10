package com.finderfeed.fdlib.to_other_mod.client.particles.ball_particle;

import com.finderfeed.fdlib.systems.particle.FDParticleRenderType;
import com.finderfeed.fdlib.util.math.ComplexEasingFunction;
import com.finderfeed.fdlib.util.math.FDMathUtil;
import com.finderfeed.fdlib.util.rendering.FDEasings;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureManager;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.opengl.GL11;

public class BallParticle extends TextureSheetParticle {

    private BallParticleOptions options;
    private ComplexEasingFunction scalingFunction;
    private float quadSizeO;
    private float quadSizeC;
    private float maxQuadSize;

    public BallParticle(BallParticleOptions options,ClientLevel level, double x, double y, double z, double xd, double yd, double zd) {
        super(level, x, y, z, xd, yd, zd);
        this.options = options;
        this.x = x;
        this.y = y;
        this.z = z;
        this.xd = xd;
        this.yd = yd;
        this.zd = zd;
        this.lifetime = options.scalingOptions.fullTime();
        this.scalingFunction = ComplexEasingFunction.builder()
                .addArea(options.scalingOptions.inTime, FDEasings::linear)
                .addArea(options.scalingOptions.stayTime,FDEasings::one)
                .addArea(options.scalingOptions.outTime,FDEasings::reversedLinear)
                .build();
        this.friction = options.friction;
        this.hasPhysics = options.hasPhysics;
        this.maxQuadSize = options.size;
        this.quadSizeO = scalingFunction.apply(0) * maxQuadSize;
        this.quadSizeC = quadSizeO;
        this.rCol = options.color.r;
        this.gCol = options.color.g;
        this.bCol = options.color.b;
        this.alpha = options.color.a;
        options.particleProcessor.init(this);
    }

    @Override
    public void render(VertexConsumer vertex, Camera camera, float pticks) {
        this.quadSize = FDMathUtil.lerp(quadSizeO,quadSizeC,pticks);
        super.render(vertex, camera, pticks);
    }

    @Override
    public void tick() {
        options.particleProcessor.processParticle(this);
        super.tick();
        quadSizeO = quadSizeC;
        quadSizeC = scalingFunction.apply(this.age) * maxQuadSize;
    }

    @Override
    protected int getLightColor(float pticks) {
        return LightTexture.FULL_BRIGHT;
    }

    @Override
    protected float getU0() {
        var v = super.getU0();
        ParticleEngine engine = Minecraft.getInstance().particleEngine;
        int w = engine.textureAtlas.width;
        return v + 1 / (float) w;
    }

    @Override
    protected float getU1() {
        var v = super.getU1();
        ParticleEngine engine = Minecraft.getInstance().particleEngine;
        int w = engine.textureAtlas.width;
        return v - 1 / (float) w;
    }

    @Override
    protected float getV0() {
        var v = super.getV0();
        ParticleEngine engine = Minecraft.getInstance().particleEngine;
        int w = engine.textureAtlas.height;
        return v + 1 / (float) w;
    }

    @Override
    protected float getV1() {
        var v = super.getV1();
        ParticleEngine engine = Minecraft.getInstance().particleEngine;
        int w = engine.textureAtlas.height;
        return v - 1 / (float) w;
    }

    @Override
    public ParticleRenderType getRenderType() {
        return RENDER_TYPE;
    }

    public static final ParticleRenderType RENDER_TYPE = new FDParticleRenderType() {


        @Nullable
        @Override
        public BufferBuilder begin(Tesselator bufferBuilder, TextureManager textureManager) {
            RenderSystem.depthMask(false);
            RenderSystem.enableBlend();
            RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);



            RenderSystem.setShader(GameRenderer::getParticleShader);
            RenderSystem.setShaderTexture(0, TextureAtlas.LOCATION_PARTICLES);
            Minecraft.getInstance().getTextureManager().getTexture(TextureAtlas.LOCATION_PARTICLES).setBlurMipmap(true,true);
            return bufferBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.PARTICLE);
        }

        @Override
        public void end() {
            Minecraft.getInstance().getTextureManager().getTexture(TextureAtlas.LOCATION_PARTICLES).restoreLastBlurMipmap();
        }

        @Override
        public boolean isTranslucent() {
            return true;
        }

        @Override
        public String toString() {
            return "fdlib:ball_particle";
        }
    };

    public static class Factory implements ParticleProvider<BallParticleOptions>{

        private SpriteSet spriteSet;

        public Factory(SpriteSet spriteSet){
            this.spriteSet = spriteSet;
        }

        @Nullable
        @Override
        public Particle createParticle(BallParticleOptions options, ClientLevel level, double x, double y, double z, double xd, double yd, double zd) {
            BallParticle particle = new BallParticle(options,level,x,y,z,xd,yd,zd);
            particle.pickSprite(spriteSet);
            return particle;
        }
    }
}
