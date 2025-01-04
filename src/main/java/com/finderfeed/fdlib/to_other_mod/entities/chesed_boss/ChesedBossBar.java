package com.finderfeed.fdlib.to_other_mod.entities.chesed_boss;

import com.finderfeed.fdlib.systems.hud.bossbars.FDBossBarInterpolated;
import com.finderfeed.fdlib.init.FDCoreShaders;
import com.finderfeed.fdlib.util.rendering.FDRenderUtil;
import com.finderfeed.fdlib.util.rendering.FDShaderRenderer;
import net.minecraft.client.gui.GuiGraphics;

import java.util.UUID;

public class ChesedBossBar extends FDBossBarInterpolated {

    private int time = 0;


    public ChesedBossBar(UUID uuid, int entityId) {
        super(uuid, entityId,10);
    }

    @Override
    public float renderInterpolatedBossBar(GuiGraphics graphics, float partialTicks,float interpolatedPercentage) {

        float xw = 400;
        float yw = 10;
        FDRenderUtil.fill(graphics.pose(),-200,0,xw * interpolatedPercentage,yw,1f,0f,0f,1f);

        float t = (time + partialTicks) / 1000f;


        float offs = 20;

        FDRenderUtil.Scissor.pushScissors(graphics.pose(),-200,0,xw * interpolatedPercentage,yw);
        FDShaderRenderer.start(graphics,FDCoreShaders.NOISE)
                .position(-200,0,0)
                .setResolution(xw,yw)
                .setUVSpan(0.5f,1)
                .setRightColor(1,1,1,0.3f)
                .setLeftColor(1,1,1,0.3f)
                .setShaderUniform("size",xw,yw)
                .setShaderUniform("xyOffset",0,0)
                .setShaderUniform("sections",100)
                .setShaderUniform("octaves",4)
                .setShaderUniform("time",t)
                .end();
        FDRenderUtil.Scissor.popScissors();



        return 40;
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
