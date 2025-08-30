package com.finderfeed.fdlib.systems.music.data;

import net.minecraft.sounds.SoundEvent;

public class FDMusicPartData {

    private SoundEvent soundEvent;
    private float playDuration;
    private int defaultFadeIn = 0;
    private boolean isLooped;

    public FDMusicPartData(SoundEvent soundEvent, float playDuration){
        this.soundEvent = soundEvent;
        this.playDuration = playDuration;
    }

    public FDMusicPartData setDefaultFadeIn(int defaultFadeIn){
        this.defaultFadeIn = defaultFadeIn;
        return this;
    }

    public FDMusicPartData setLooping(boolean looping){
        this.isLooped = looping;
        return this;
    }

    public boolean shouldLoop() {
        return isLooped;
    }

    public float getPlayDuration() {
        return playDuration;
    }

    public SoundEvent getSoundEvent() {
        return soundEvent;
    }

    public int getDefaultFadeInTime() {
        return defaultFadeIn;
    }

}
