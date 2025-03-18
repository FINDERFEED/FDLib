package com.finderfeed.fdlib.systems.screen.screen_effect;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphics;

public abstract class ScreenEffect {

    private int inTime;
    private int stayTime;
    private int endTime;

    public ScreenEffect(int inTime, int stayTime, int endTime){
        this.inTime = inTime;
        this.stayTime = stayTime;
        this.endTime = endTime;
    }

    public abstract void render(GuiGraphics graphics, DeltaTracker deltaTracker, float screenWidth, float screenHeight);

    public void setEndTime(int endTime) {
        this.endTime = endTime;
    }

    public void setInTime(int inTime) {
        this.inTime = inTime;
    }

    public void setStayTime(int stayTime) {
        this.stayTime = stayTime;
    }

    public int getEndTime() {
        return endTime;
    }

    public int getInTime() {
        return inTime;
    }

    public int getStayTime() {
        return stayTime;
    }
}
