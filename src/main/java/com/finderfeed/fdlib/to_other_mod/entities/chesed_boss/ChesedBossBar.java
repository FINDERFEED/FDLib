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

        FDRenderUtil.fill(graphics.pose(),-200,0,400 * interpolatedPercentage,40,1f,0f,0f,1f);

        float t = (time + partialTicks) / 200f;


        float xw = 400 * interpolatedPercentage;
        float yw = 40;

        FDShaderRenderer.start(graphics,FDCoreShaders.NOISE)
                .position(-200,0,0)
                .setResolution(xw,yw)
                .setUVSpan(0.1f,1)
                .setRightColor(1,1,1,0.75f)
                .setLeftColor(1,1,1,0f)
                .setShaderUniform("size",400,yw)
                .setShaderUniform("xyOffset",-t,0)
                .setShaderUniform("sections",20)
                .setShaderUniform("octaves",4)
                .setShaderUniform("time",t)
                .end();


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
