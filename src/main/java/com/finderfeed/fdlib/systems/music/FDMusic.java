package com.finderfeed.fdlib.systems.music;

import com.finderfeed.fdlib.systems.music.data.FDMusicData;

import java.util.ArrayList;
import java.util.List;

public class FDMusic {

    private FDMusicData fdMusicData;

    private List<FDMusicPart> musicParts = new ArrayList<>();

    private int currentlyTickingFrom;
    private int currentlyTickingTo;

    public FDMusic(FDMusicData fdMusicData){
        this.fdMusicData = fdMusicData;
        for (var musicPartData : fdMusicData.getMusicPartDatas()){
            this.musicParts.add(new FDMusicPart(musicPartData));
        }
        this.currentlyTickingFrom = fdMusicData.getStartFrom();
        this.currentlyTickingTo = this.currentlyTickingFrom + 1;
    }

    public void tick(){
        for (int i = currentlyTickingFrom; i < currentlyTickingTo; i++){
            FDMusicPart fdMusicPart = musicParts.get(i);
            fdMusicPart.tick();
        }
    }

    public void fadeOut(int fadeOutTime){
        for (int i = currentlyTickingFrom; i < currentlyTickingTo; i++){
            FDMusicPart fdMusicPart = musicParts.get(i);
            fdMusicPart.triggerFadeOut(fadeOutTime,false);
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




    public FDMusicData getFdMusicData() {
        return fdMusicData;
    }

}
