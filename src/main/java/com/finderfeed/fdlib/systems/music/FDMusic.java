package com.finderfeed.fdlib.systems.music;

import com.finderfeed.fdlib.FDClientHelpers;
import com.finderfeed.fdlib.systems.music.data.FDMusicData;
import net.minecraft.client.resources.sounds.SoundInstance;

import java.util.ArrayList;
import java.util.List;

public class FDMusic {

    private FDMusicData fdMusicData;

    private List<FDMusicPart> musicParts = new ArrayList<>();

    private int currentlyTickingFrom;
    private int currentlyTickingTo;

    private boolean finishedPlaying = false;
    private boolean triggeredEnd = false;

    public FDMusic(FDMusicData fdMusicData){
        this.fdMusicData = fdMusicData;
        for (var musicPartData : fdMusicData.getMusicPartDatas()){
            this.musicParts.add(new FDMusicPart(musicPartData));
        }
        this.currentlyTickingFrom = fdMusicData.getStartFrom();
        this.currentlyTickingTo = this.currentlyTickingFrom + 1;
    }

    public void tick(){
        int finishedAmount = 0;
        for (int i = currentlyTickingFrom; i < currentlyTickingTo; i++){
            FDMusicPart fdMusicPart = musicParts.get(i);
            fdMusicPart.tick();
            if (triggeredEnd){
                if (fdMusicPart.hasFinished()){
                    finishedAmount++;
                }
            }else {
                if (i == currentlyTickingTo - 1 && fdMusicPart.hasFinished()) {
                    currentlyTickingTo += 1;
                }
            }
        }

        int endAmount = currentlyTickingTo - currentlyTickingFrom;

        if (finishedAmount == endAmount){
            this.finishedPlaying = true;
        }

    }

    public void renderTick(float pticks){
        for (int i = currentlyTickingFrom; i < currentlyTickingTo; i++){
            FDMusicPart fdMusicPart = musicParts.get(i);
            fdMusicPart.renderTick(pticks);
        }
    }

    public void triggerMusicEnd(int fadeOutTime){
        if (fadeOutTime > 0) {
            this.fadeOut(fadeOutTime, true);
        }else{
            this.immediateShutdown();
        }
        triggeredEnd = true;
    }

    public boolean hasFinishedPlaying(){
        return finishedPlaying;
    }

    public void fadeOut(int fadeOutTime, boolean finishOnFadeOut){
        for (int i = currentlyTickingFrom; i < currentlyTickingTo; i++){
            FDMusicPart fdMusicPart = musicParts.get(i);
            fdMusicPart.triggerFadeOut(fadeOutTime,finishOnFadeOut);
        }
    }

    public void fadeIn(int fadeInTime){
        for (int i = currentlyTickingFrom; i < currentlyTickingTo; i++){
            FDMusicPart fdMusicPart = musicParts.get(i);
            fdMusicPart.triggerFadeIn(fadeInTime);
        }
    }

    public void nextMusicPart(int fadeOutTime){
        if (this.currentlyTickingTo + 1 <= this.musicParts.size()){
            FDMusicPart musicPart = this.musicParts.get(this.currentlyTickingTo - 1);
            musicPart.triggerFadeOut(fadeOutTime, true);
            this.currentlyTickingTo += 1;
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

}
