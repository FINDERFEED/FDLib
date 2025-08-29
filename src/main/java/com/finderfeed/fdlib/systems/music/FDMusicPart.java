package com.finderfeed.fdlib.systems.music;

import com.finderfeed.fdlib.FDClientHelpers;
import com.finderfeed.fdlib.systems.music.data.FDMusicPartData;
import com.finderfeed.fdlib.util.math.FDMathUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.ChannelAccess;
import net.minecraft.client.sounds.SoundEngine;
import net.minecraft.client.sounds.SoundManager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class FDMusicPart {

    private FDMusicPartData data;

    private List<SoundInstance> soundInstances = new ArrayList<>();

    private int ticker = -1;
    private int currentFadeTime = -1;
    private int fadeInTicker = -1;
    private int fadeOutTicker = -1;
    private float currentVolume = 1;
    private float volumeStamp = 1;

    private boolean finishOnFadeOut;

    private boolean finishedPlaying = false;

    public FDMusicPart(FDMusicPartData data){
        this.data = data;
        if (data.getDefaultFadeInTime() == 0){
            currentVolume = 1;
            volumeStamp = 1;
        }
    }

    public void tick(){
        this.deleteEndedSoundInstances();
        if (!finishedPlaying) {
            this.manageFades();
            this.manageLoopOrFinishPlaying();
            this.setSoundInstancesVolume(this.currentVolume);
        }
    }

    private void manageFades(){
        if (fadeInTicker != -1){
            float p = (float) fadeInTicker / currentFadeTime;
            this.currentVolume = FDMathUtil.lerp(this.volumeStamp, 1, 1 - p);
            this.fadeInTicker--;
        }else{
            float p = (float) fadeOutTicker / currentFadeTime;
            this.currentVolume = FDMathUtil.lerp(this.volumeStamp, 0, p);
            this.fadeOutTicker--;
            if (this.fadeOutTicker == -1 && this.finishOnFadeOut){
                this.finishedPlaying = true;
                SoundManager soundManager = FDClientHelpers.getSoundManager();
                for (var inst : this.soundInstances){
                    soundManager.stop(inst);
                }
                this.soundInstances.clear();
            }
        }
    }

    private void manageLoopOrFinishPlaying(){
        if (this.ticker >= this.data.getPlayDuration()) {
            if (this.data.shouldLoop()) {
                this.ticker = 0;
                this.startNewSound();
            } else {
                this.finishedPlaying = true;
            }
        }else{
            if (ticker == -1){
                ticker = 0;
                this.startNewSound();
            }
            ticker++;
        }
    }

    private void startNewSound(){
        SoundInstance soundInstance = SimpleSoundInstance.forMusic(this.data.getSoundEvent());
        this.soundInstances.add(soundInstance);
        SoundManager soundManager = Minecraft.getInstance().getSoundManager();
        soundManager.play(soundInstance);
    }

    private void deleteEndedSoundInstances(){
        Iterator<SoundInstance> iterator = this.soundInstances.iterator();
        while (iterator.hasNext()){
            SoundInstance soundInstance = iterator.next();
            SoundManager soundManager = FDClientHelpers.getSoundManager();
            if (!soundManager.isActive(soundInstance)){
                iterator.remove();
            }
        }
    }

    private void setSoundInstancesVolume(float volume){
        SoundEngine soundEngine = FDClientHelpers.getSoundEngine();
        for (var instances : soundEngine.instanceToChannel.entrySet()){
            SoundInstance key = instances.getKey();
            if (this.soundInstances.contains(key)){
                ChannelAccess.ChannelHandle channelHandle = instances.getValue();
                channelHandle.execute(channel -> {
                    channel.setVolume(volume);
                });
            }
        }
    }

    private boolean hasFinished(){
        return finishedPlaying;
    }

    public void triggerFadeOut(int fadeOutTime, boolean finishOnFadeOut){
        volumeStamp = currentVolume;
        fadeOutTicker = fadeOutTime;
        fadeInTicker = -1;
        currentFadeTime = fadeOutTime;
        this.finishOnFadeOut = finishOnFadeOut;
    }

    public void triggerFadeIn(int fadeInTime) {
        if (!finishOnFadeOut) {
            volumeStamp = currentVolume;
            fadeOutTicker = -1;
            fadeInTicker = fadeInTime;
            currentFadeTime = fadeInTime;
        }
    }

    public void end(){
    }

    public FDMusicPartData getData() {
        return data;
    }

    public List<SoundInstance> getSoundInstances() {
        return soundInstances;
    }
}
