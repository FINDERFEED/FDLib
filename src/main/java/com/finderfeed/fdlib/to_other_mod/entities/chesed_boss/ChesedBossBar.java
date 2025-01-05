package com.finderfeed.fdlib.to_other_mod.entities.chesed_boss;

import com.finderfeed.fdlib.FDLib;
import com.finderfeed.fdlib.systems.hud.bossbars.FDBossBarInterpolated;
import com.finderfeed.fdlib.init.FDCoreShaders;
import com.finderfeed.fdlib.systems.screen.screen_particles.FDTexturedSParticle;
import com.finderfeed.fdlib.to_other_mod.client.particles.ball_particle.BallParticle;
import com.finderfeed.fdlib.util.rendering.FDRenderUtil;
import com.finderfeed.fdlib.util.rendering.FDShaderRenderer;
import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

import java.util.Random;
import java.util.UUID;

public class ChesedBossBar extends FDBossBarInterpolated {

    private static final Random r = new Random();

    public static final ResourceLocation TEXTURE = ResourceLocation.tryBuild(FDLib.MOD_ID,"textures/boss_bars/chesed_boss_bar.png");

    private int time = 0;


    public ChesedBossBar(UUID uuid, int entityId) {
        super(uuid, entityId,10);
    }

    //198 (192) 45 (50)
    @Override
    public void renderInterpolatedBossBar(GuiGraphics graphics, float partialTicks,float interpolatedPercentage) {

        PoseStack matrices = graphics.pose();

        FDRenderUtil.bindTexture(TEXTURE);
        FDRenderUtil.blitWithBlend(matrices,-198/2f,0,198,45,0,0,
                198,45,198,50,0,1f);

        float hpPosX = -198/2f + 3;
        float hpPosY = 20;
        float hpW = 192f;

        FDRenderUtil.blitWithBlend(matrices,hpPosX,hpPosY,hpW * interpolatedPercentage,5,0,45,
                hpW * interpolatedPercentage,5,198,50,0,1f);




        float t = (time + partialTicks) / 4000f;


        FDRenderUtil.Scissor.pushScissors(matrices,hpPosX,hpPosY,hpW * interpolatedPercentage,5);
        FDShaderRenderer.start(graphics,FDCoreShaders.NOISE)
                .position(hpPosX,hpPosY,0)
                .setResolution(hpW,5)
                .setUVSpan(0.5f,1)
                .setUpColor(0.1f,0.5f,0.5f,0f)
                .setDownColor(0.1f,0.8f,0.8f,0.8f)
                .setShaderUniform("size",hpW,5)
                .setShaderUniform("xyOffset",0,t)
                .setShaderUniform("sections",100)
                .setShaderUniform("octaves",4)
                .setShaderUniform("time",t)
                .end();
        FDRenderUtil.Scissor.popScissors();

    }

    @Override
    public void tick(float topOffset) {
        super.tick(topOffset);
        time++;

        Window window = Minecraft.getInstance().getWindow();

        float w = window.getGuiScaledWidth();
        float h = window.getGuiScaledHeight();


        if (time % 4 == 0) {
            float randomOffset = r.nextFloat() * 200 - 100;
            float randomOffset2 = r.nextFloat() * 200 - 100;
            float x = w / 2 + randomOffset;
            float x2 = w / 2 + randomOffset2;
            float y = 22;
            FDTexturedSParticle.create(FDRenderUtil.ParticleRenderTypesS.TEXTURES_BLUR_ADDITIVE, BallParticle.LOCATION)
                    .setPos(x, y, true)
                    .setMaxQuadSize(3.5f)
                    .setSpeed(0, -0.1)
                    .setFriction(0.99f)
                    .setColor(
                            0.1f + r.nextFloat() * 0.1f - 0.05f,
                            0.8f + r.nextFloat() * 0.1f - 0.05f,
                            0.8f + r.nextFloat() * 0.1f - 0.05f,
                            0.8f
                    )
                    .setLifetime(30)
                    .setDefaultScaleInOut()
                    .sendToOverlay();
            FDTexturedSParticle.create(FDRenderUtil.ParticleRenderTypesS.TEXTURES_BLUR_ADDITIVE, BallParticle.LOCATION)
                    .setPos(x2, y + 7, true)
                    .setMaxQuadSize(3.5f)
                    .setSpeed(0, 0.1)
                    .setFriction(0.99f)
                    .setColor(
                            0.1f + r.nextFloat() * 0.1f - 0.05f,
                            0.8f + r.nextFloat() * 0.1f - 0.05f,
                            0.8f + r.nextFloat() * 0.1f - 0.05f,
                            0.8f
                    )
                    .setLifetime(30)
                    .setDefaultScaleInOut()
                    .sendToOverlay();
        }


    }

    @Override
    public float height() {
        return 50;
    }

    @Override
    public void hanldeBarEvent(int eventId, int data) {

    }
}
