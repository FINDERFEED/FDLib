package com.finderfeed.fdlib.systems.music;

import com.finderfeed.fdlib.FDClientHelpers;
import com.finderfeed.fdlib.systems.music.data.FDMusicData;
import com.finderfeed.fdlib.util.math.FDMathUtil;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.SoundManager;

import java.util.ArrayList;
import java.util.List;

public class FDMusic {

    private FDMusicData fdMusicData;

    private List<FDMusicPart> musicParts = new ArrayList<>();

    private int currentlyTickingFrom;
    private int currentlyTickingTo;

    private boolean finishedPlaying = false;

    private int inactiveTime = 0;


    private int currentFadeTime = -1;
    private int fadeInTicker = -1;
    private int fadeOutTicker = -1;
    private float oldVolume = 1;
    private float currentVolume = 1;
    private float volumeStamp = 1;

    private boolean finishOnFadeOut;


    public FDMusic(FDMusicData fdMusicData){
        this.fdMusicData = fdMusicData;
        for (var musicPartData : fdMusicData.getMusicPartDatas()){
            this.musicParts.add(new FDMusicPart(musicPartData, this));
        }
        this.currentlyTickingFrom = fdMusicData.getStartFrom();
        this.currentlyTickingTo = this.currentlyTickingFrom + 1;

        if (fdMusicData.getStartFadeInTime() != 0){
            this.volumeStamp = 0;
            this.oldVolume = 0;
            this.currentVolume = 0;
            this.fadeIn(fdMusicData.getStartFadeInTime());
        }

    }

    public void tick(){

        for (int i = currentlyTickingFrom; i < currentlyTickingTo; i++){
            FDMusicPart fdMusicPart = musicParts.get(i);
            fdMusicPart.tick();
        }

        if (this.currentVolume == 0 && this.oldVolume == 0){
            inactiveTime++;
        }else{
            inactiveTime = 0;
        }

        if (inactiveTime >= this.fdMusicData.getInactiveDeleteTime()){
            this.finishedPlaying = true;
        }

        this.tickFades();

    }

    public void renderTick(float pticks){
        for (int i = currentlyTickingFrom; i < currentlyTickingTo; i++){

            FDMusicPart fdMusicPart = musicParts.get(i);
            fdMusicPart.setAllSoundInstancesVolume(this.getVolume(pticks) * FDClientHelpers.getCurrentMusicVolume());
            fdMusicPart.renderTick(pticks);

            if (!finishOnFadeOut){
                if (i == currentlyTickingTo - 1 && fdMusicPart.hasFinished()){
                    if (currentlyTickingTo < musicParts.size()){
                        currentlyTickingTo++;
                    }else{
                        this.finishedPlaying = true;
                    }
                }
            }

        }
    }



    private void tickFades(){
        this.oldVolume = currentVolume;
        if (fadeInTicker != -1){
            float p = (float) fadeInTicker / currentFadeTime;
            this.currentVolume = FDMathUtil.lerp(this.volumeStamp, 1, 1 - p);
            this.fadeInTicker--;
        }else if (fadeOutTicker != -1){
            float p = (float) fadeOutTicker / currentFadeTime;
            this.currentVolume = FDMathUtil.lerp(this.volumeStamp, 0, 1 - p);
            this.fadeOutTicker--;
            if (this.fadeOutTicker == -1 && this.finishOnFadeOut){
                this.finishedPlaying = true;
            }
        }
    }



    public void triggerMusicEnd(int fadeOutTime){
        if (fadeOutTime > 0) {
            this.fadeOut(fadeOutTime, true);
        }else{
            this.immediateShutdown();
        }
        finishOnFadeOut = true;
    }

    public boolean hasFinishedPlaying(){
        return finishedPlaying;
    }

    public void fadeOut(int fadeOutTime, boolean finishOnFadeOut){
        volumeStamp = currentVolume;
        fadeOutTicker = fadeOutTime;
        fadeInTicker = -1;
        currentFadeTime = fadeOutTime;
        this.finishOnFadeOut = finishOnFadeOut;
    }

    public void fadeIn(int fadeInTime){
        if (!finishOnFadeOut) {
            volumeStamp = currentVolume;
            fadeOutTicker = -1;
            fadeInTicker = fadeInTime;
            currentFadeTime = fadeInTime;
        }
    }


    public List<SoundInstance> allSoundInstances(){
        List<SoundInstance> soundInstances = new ArrayList<>();
        for (var musicPart : this.musicParts){
            soundInstances.addAll(musicPart.getSoundInstances());
        }
        return soundInstances;
    }

    public void immediateShutdown(){
        for (var soundInstance : this.allSoundInstances()){
            FDClientHelpers.getSoundManager().stop(soundInstance);
        }
        this.finishedPlaying = true;
    }

    public FDMusicData getFdMusicData() {
        return fdMusicData;
    }


    public float getCurrentVolume() {
        return currentVolume;
    }

    public float getVolume(float pticks){
        return FDMathUtil.lerp(oldVolume, currentVolume, pticks);
    }

    public void onFinishPlaying(){
        for (var instance : this.allSoundInstances()){
            SoundManager soundManager = FDClientHelpers.getSoundManager();
            soundManager.stop(instance);
        }
    }

}
