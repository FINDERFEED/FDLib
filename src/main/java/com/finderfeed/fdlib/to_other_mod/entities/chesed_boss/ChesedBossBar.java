package com.finderfeed.fdlib.to_other_mod.entities.chesed_boss;

import com.finderfeed.fdlib.systems.hud.bossbars.FDBossBar;
import com.finderfeed.fdlib.systems.hud.bossbars.FDBossBarInterpolated;
import com.finderfeed.fdlib.util.rendering.FDRenderUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphics;

import java.util.UUID;

public class ChesedBossBar extends FDBossBarInterpolated {

    public ChesedBossBar(UUID uuid, int entityId) {
        super(uuid, entityId,300);
    }

    @Override
    public float render(GuiGraphics graphics, float partialTicks) {

        FDRenderUtil.fill(graphics.pose(),-200,0,400 * this.getInterpolatedPercentage(),40,1f,0f,0f,1f);

        return 40;
    }


    @Override
    public void hanldeBarEvent(int eventId, int data) {

    }
}
