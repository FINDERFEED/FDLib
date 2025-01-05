package com.finderfeed.fdlib.systems.hud.bossbars;

import com.finderfeed.fdlib.util.math.FDMathUtil;
import com.finderfeed.fdlib.util.rendering.FDEasings;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.util.Mth;

import java.util.UUID;

public abstract class FDBossBarInterpolated extends FDBossBar {

    private float oldPercentage = 1;
    private float interpolatedPercentage = 1;

    private int interpolationTime;
    private int currentInterpolationTime = 0;


    public FDBossBarInterpolated(UUID uuid, int entityId, int interpolationTime) {
        super(uuid, entityId);
        this.interpolationTime = interpolationTime;
    }

    @Override
    public void render(GuiGraphics graphics, float partialTicks) {
        if (currentInterpolationTime > 0){
            float p = FDEasings.easeIn(Mth.clamp(currentInterpolationTime - partialTicks,0,interpolationTime) / (float) interpolationTime);
            this.interpolatedPercentage = FDMathUtil.lerp(this.getPercentage(),this.oldPercentage,p);
        }else{
            oldPercentage = this.getPercentage();
            interpolatedPercentage = this.getPercentage();
        }
        this.renderInterpolatedBossBar(graphics,partialTicks,interpolatedPercentage);
    }

    public abstract void renderInterpolatedBossBar(GuiGraphics graphics, float partialTicks,float interpolatedPercentage);

    @Override
    public void tick(float topOffset) {
        currentInterpolationTime = Mth.clamp(currentInterpolationTime - 1,0,interpolationTime);
    }


    @Override
    public void setPercentage(float percentage) {
        if (percentage != this.getPercentage()) {
            if (currentInterpolationTime > 0) {
                oldPercentage = this.getInterpolatedPercentage();
            } else {
                oldPercentage = this.getPercentage();
            }
            currentInterpolationTime = interpolationTime;
        }
        super.setPercentage(percentage);
    }


    public float getInterpolatedPercentage() {
        return interpolatedPercentage;
    }

    public int getCurrentInterpolationTime() {
        return currentInterpolationTime;
    }

    @Override
    public float getPercentage() {
        return super.getPercentage();
    }

    public float getOldPercentage() {
        return oldPercentage;
    }

    public int getInterpolationTime() {
        return interpolationTime;
    }
}

