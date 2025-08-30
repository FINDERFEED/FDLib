package com.finderfeed.fdlib.systems.music.data;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.sounds.SoundEvent;

public class FDMusicPartData {

    public static final StreamCodec<RegistryFriendlyByteBuf, FDMusicPartData> STREAM_CODEC = StreamCodec.composite(
            SoundEvent.DIRECT_STREAM_CODEC, v->v.soundEvent,
            ByteBufCodecs.FLOAT, v->v.playDuration,
            ByteBufCodecs.BOOL, v->v.isLooped,
            (event,duration,loop)->{
                return new FDMusicPartData(event, duration)
                        .setLooping(loop);
            }
    );

    private SoundEvent soundEvent;
    private float playDuration;
    private boolean isLooped;

    public FDMusicPartData(SoundEvent soundEvent, float playDuration){
        this.soundEvent = soundEvent;
        this.playDuration = playDuration;
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

}
