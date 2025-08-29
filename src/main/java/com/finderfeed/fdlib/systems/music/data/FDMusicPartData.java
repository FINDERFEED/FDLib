package com.finderfeed.fdlib.systems.music.data;

import net.minecraft.sounds.SoundEvent;

public class FDMusicPartData {

    private SoundEvent soundEvent;
    private int playDuration;
    private int defaultFadeIn = 0;
    private int defaultFadeOut = 0;
    private boolean isLooped;

    public FDMusicPartData(SoundEvent soundEvent, int playDuration){
        this.soundEvent = soundEvent;
        this.playDuration = playDuration;
    }

    public FDMusicPartData setDefaultFadeIn(int defaultFadeIn){
        this.defaultFadeIn = defaultFadeIn;
        return this;
    }

    public FDMusicPartData setDefaultFadeOut(int defaultFadeOut){
        this.defaultFadeOut = defaultFadeOut;
        return this;
    }

    public FDMusicPartData setLooping(boolean looping){
        this.isLooped = looping;
        return this;
    }

    public boolean shouldLoop() {
        return isLooped;
    }

    public int getPlayDuration() {
        return playDuration;
    }

    public SoundEvent getSoundEvent() {
        return soundEvent;
    }

    public int getDefaultFadeInTime() {
        return defaultFadeIn;
    }

    public int getDefaultFadeOutTime() {
        return defaultFadeOut;
    }

}
