package com.finderfeed.fdlib.systems.music.data;

import com.finderfeed.fdlib.systems.stream_codecs.NetworkCodec;
import net.minecraft.sounds.SoundEvent;

public class FDMusicPartData {

    public static final NetworkCodec<FDMusicPartData> STREAM_CODEC = NetworkCodec.composite(
            NetworkCodec.SOUND_EVENT, v->v.soundEvent,
            NetworkCodec.FLOAT, v->v.playDuration,
            NetworkCodec.BOOL, v->v.isLooped,
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
