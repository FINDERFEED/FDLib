package com.finderfeed.fdlib.systems.screen.screen_effect;

import net.minecraft.util.Mth;

public class ScreenEffectInstance {

    public ScreenEffect effect;
    public int currentTime;

    public ScreenEffectInstance(ScreenEffect base){
        this.effect = base;
    }

    public void tick(){
        this.currentTime = Mth.clamp(currentTime + 1, 0,effect.getLifetime());
    }

    public boolean isFinished(){
        return currentTime >= effect.getOutTime() + effect.getInTime() + effect.getStayTime();
    }

    public int getCurrentTime() {
        return currentTime;
    }
}
