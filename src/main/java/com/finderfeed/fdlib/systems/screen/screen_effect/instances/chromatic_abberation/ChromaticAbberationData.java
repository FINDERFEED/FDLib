package com.finderfeed.fdlib.systems.screen.screen_effect.instances.chromatic_abberation;

import com.finderfeed.fdlib.systems.screen.screen_effect.ScreenEffectData;
import com.finderfeed.fdlib.systems.stream_codecs.NetworkCodec;

public class ChromaticAbberationData extends ScreenEffectData {

    public static NetworkCodec<ChromaticAbberationData> STREAM_CODEC = NetworkCodec.composite(
            NetworkCodec.FLOAT, v->v.strength,
            ChromaticAbberationData::new
    );

    private float strength;

    public ChromaticAbberationData(float strength){
        this.strength = strength;
    }

    public float getStrength() {
        return strength;
    }
}
