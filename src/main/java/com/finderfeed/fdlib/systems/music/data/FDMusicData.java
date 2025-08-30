package com.finderfeed.fdlib.systems.music.data;

import com.finderfeed.fdlib.util.FDByteBufCodecs;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FDMusicData {

    public static final StreamCodec<RegistryFriendlyByteBuf, FDMusicData> STREAM_CODEC = StreamCodec.composite(
            FDMusicPartData.STREAM_CODEC.apply(ByteBufCodecs.list()),v->v.musicPartDatas,
            ByteBufCodecs.INT, v->v.startFrom,
            ByteBufCodecs.INT, v->v.inactiveDeleteTime,
            FDByteBufCodecs.UUID,v->v.musicSourceUUID,
            (datas, startFrom, inactiveDeleteTime, sourceUUID)->{
                return new FDMusicData(sourceUUID, datas)
                        .startFromPart(startFrom)
                        .inactiveDeleteTime(inactiveDeleteTime);
            }
    );

    private List<FDMusicPartData> musicPartDatas = new ArrayList<>();

    private int startFrom = 0;

    private int inactiveDeleteTime = 60 * 20 * 5;

    private UUID musicSourceUUID;

    public FDMusicData(UUID uuid, FDMusicPartData musicPart){
        this.musicPartDatas.add(musicPart);
        this.musicSourceUUID = uuid;
    }

    public FDMusicData(UUID uuid, List<FDMusicPartData> musicPart){
        this.musicPartDatas.addAll(musicPart);
        this.musicSourceUUID = uuid;
    }

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
}
