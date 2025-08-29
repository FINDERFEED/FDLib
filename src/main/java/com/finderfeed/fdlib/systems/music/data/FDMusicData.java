package com.finderfeed.fdlib.systems.music.data;

import com.finderfeed.fdlib.systems.music.FDMusic;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FDMusicData {

    private List<FDMusicPartData> musicPartDatas = new ArrayList<>();

    private int startFrom = 0;

    private UUID musicSourceUUID;

    public FDMusicData(UUID uuid, FDMusicPartData musicPart){
        this.musicPartDatas.add(musicPart);
        this.musicSourceUUID = uuid;
    }

    public FDMusicData startFromPart(int partId){
        this.startFrom = partId;
        return this;
    }

    public FDMusicData addMusicPart(FDMusicPartData data){
        this.musicPartDatas.add(data);
        return this;
    }

    public UUID getMusicSourceUUID() {
        return musicSourceUUID;
    }

    public List<FDMusicPartData> getMusicPartDatas() {
        return musicPartDatas;
    }

    public int getStartFrom() {
        return startFrom;
    }

}
