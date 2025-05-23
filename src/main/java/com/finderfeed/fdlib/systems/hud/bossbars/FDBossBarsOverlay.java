package com.finderfeed.fdlib.systems.hud.bossbars;

import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.LayeredDraw;

public class FDBossBarsOverlay implements LayeredDraw.Layer {

    public static final float BASE_BOSS_BAR_TOP_OFFSET = 3;

    @Override
    public void render(GuiGraphics graphics, DeltaTracker deltraTracker) {

        float pticks = deltraTracker.getGameTimeDeltaPartialTick(false);

        PoseStack matrices = graphics.pose();

        Window window = Minecraft.getInstance().getWindow();
        float width = window.getGuiScaledWidth();


        matrices.pushPose();

        float baseOffset = calculateBossBarsOffset();
        matrices.translate(width/2,baseOffset,0);

        for (FDBossBar bossBar : FDBossbars.BOSS_BARS.values()){
            float height = bossBar.height();
            bossBar.render(graphics,pticks);
            matrices.translate(0,height,0);

        }

        matrices.popPose();
    }

    public static float calculateBossBarsOffset(){

        var bossOverlay = Minecraft.getInstance().gui.getBossOverlay();

        var size = bossOverlay.events.size();

        return BASE_BOSS_BAR_TOP_OFFSET + size * 25;
    }

}
