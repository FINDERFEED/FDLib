package com.finderfeed.fdlib.to_other_mod.entities.chesed_boss;

import com.finderfeed.fdlib.FDLib;
import com.finderfeed.fdlib.systems.hud.bossbars.FDBossBarInterpolated;
import com.finderfeed.fdlib.init.FDCoreShaders;
import com.finderfeed.fdlib.util.rendering.FDRenderUtil;
import com.finderfeed.fdlib.util.rendering.FDShaderRenderer;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

import java.util.UUID;

public class ChesedBossBar extends FDBossBarInterpolated {

    public static final ResourceLocation TEXTURE = ResourceLocation.tryBuild(FDLib.MOD_ID,"textures/boss_bars/chesed_boss_bar.png");

    private int time = 0;


    public ChesedBossBar(UUID uuid, int entityId) {
        super(uuid, entityId,10);
    }

    //198 (192) 45 (50)
    @Override
    public float renderInterpolatedBossBar(GuiGraphics graphics, float partialTicks,float interpolatedPercentage) {

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
                .setUpColor(0.1f,0.4f,0.4f,0f)
                .setDownColor(0.1f,0.8f,0.8f,0.8f)
                .setShaderUniform("size",hpW,5)
                .setShaderUniform("xyOffset",0,t)
                .setShaderUniform("sections",100)
                .setShaderUniform("octaves",4)
                .setShaderUniform("time",t)
                .end();
        FDRenderUtil.Scissor.popScissors();

        return 50;
    }

    @Override
    public void tick() {
        super.tick();
        time++;
    }

    @Override
    public void hanldeBarEvent(int eventId, int data) {

    }
}
