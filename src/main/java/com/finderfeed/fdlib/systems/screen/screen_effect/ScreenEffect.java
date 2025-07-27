package com.finderfeed.fdlib.systems.screen.screen_effect;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.util.Mth;

public abstract class ScreenEffect<T extends ScreenEffectData> {

    private int inTime;
    private int stayTime;
    private int outTime;
    private T data;

    public ScreenEffect(T data, int inTime, int stayTime, int outTime){
        this.inTime = inTime;
        this.stayTime = stayTime;
        this.outTime = outTime;
        this.data = data;
    }

    public abstract void render(GuiGraphics graphics, DeltaTracker deltaTracker, int currentTick, float screenWidth, float screenHeight);

    public void setData(T data) {
        this.data = data;
    }

    public T getData() {
        return data;
    }

    public boolean isInTime(int currentTick){
        if (inTime == 0) return false;
        return currentTick <= inTime;
    }

    public boolean isStayTime(int currentTick) {
        if (stayTime == 0) return false;
        int time = currentTick - inTime;
        if (inTime == 0) {
            return time > 0 && time <= stayTime;
        } else {
            return time > 0 && time <= stayTime;
        }
    }

    public boolean isOutTime(int currentTick) {
        if (outTime == 0) return false;
        int time = currentTick - inTime - stayTime;
        if (stayTime == 0 && inTime == 0){
            return time >= 0 && time <= outTime;
        }else {
            return time > 0 && time <= outTime;
        }
    }

    public float getInTimePercent(int currentTick, float pticks){
        if (inTime == 0) return 0;
        float time = Mth.clamp(currentTick + pticks,0,inTime);
        return time / inTime;
    }

    public float getStayTimePercent(int currentTick, float pticks){
        if (stayTime == 0) return 0;
        float time = Mth.clamp(currentTick + pticks - inTime,0,stayTime);
        return time / stayTime;
    }

    public float getOutTimePercent(int currentTick, float pticks){
        if (outTime == 0) return 0;
        float time = Mth.clamp(currentTick + pticks - inTime - stayTime,0,outTime);
        return time / outTime;
    }


    public void setOutTime(int outTime) {
        this.outTime = outTime;
    }

    public void setInTime(int inTime) {
        this.inTime = inTime;
    }

    public void setStayTime(int stayTime) {
        this.stayTime = stayTime;
    }

    public int getOutTime() {
        return outTime;
    }

    public int getInTime() {
        return inTime;
    }

    public int getStayTime() {
        return stayTime;
    }

    public int getLifetime(){
        return stayTime + outTime + inTime;
    }
}
