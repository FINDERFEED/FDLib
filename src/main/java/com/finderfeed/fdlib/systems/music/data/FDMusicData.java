package com.finderfeed.fdlib.systems.music.data;

import com.finderfeed.fdlib.systems.stream_codecs.NetworkCodec;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FDMusicData {

    public static final NetworkCodec<FDMusicData> STREAM_CODEC = NetworkCodec.composite(
            NetworkCodec.listOf(FDMusicPartData.STREAM_CODEC),v->v.musicPartDatas,
            NetworkCodec.INT, v->v.startFrom,
            NetworkCodec.INT, v->v.inactiveDeleteTime,
            NetworkCodec.INT, v->v.defaultFadeInTime,
            NetworkCodec.UUID,v->v.musicSourceUUID,
            (datas, startFrom, inactiveDeleteTime, defaultFadeInTime, sourceUUID)->{
                return new FDMusicData(sourceUUID, datas)
                        .startFromPart(startFrom)
                        .fadeInTime(defaultFadeInTime)
                        .inactiveDeleteTime(inactiveDeleteTime);
            }
    );

    private List<FDMusicPartData> musicPartDatas = new ArrayList<>();

    private int startFrom = 0;

    private int inactiveDeleteTime = 60 * 20 * 5;

    private int defaultFadeInTime = 0;

    private UUID musicSourceUUID;

    public FDMusicData(UUID uuid, FDMusicPartData musicPart){
        this.musicPartDatas.add(musicPart);
        this.musicSourceUUID = uuid;
    }

    public FDMusicData(UUID uuid, List<FDMusicPartData> musicPart){
        this.musicPartDatas.addAll(musicPart);
        this.musicSourceUUID = uuid;
    }

    public FDMusicData fadeInTime(int fadeInTime){
        this.defaultFadeInTime = fadeInTime;
        return this;
    }

    /**
     * If volume is 0 due to fade out, stops the FDMusic after this time (ticks). Required, because while FDMusic is active, other music is muted.
     */
    public FDMusicData inactiveDeleteTime(int inactiveDeleteTime){
        this.inactiveDeleteTime = inactiveDeleteTime;
        return this;
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

    public int getInactiveDeleteTime() {
        return inactiveDeleteTime;
    }

    public int getStartFadeInTime() {
        return defaultFadeInTime;
    }

}
