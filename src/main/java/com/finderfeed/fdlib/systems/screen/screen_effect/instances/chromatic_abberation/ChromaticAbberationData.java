package com.finderfeed.fdlib.systems.screen.screen_effect.instances.chromatic_abberation;

import com.finderfeed.fdlib.systems.screen.screen_effect.ScreenEffectData;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public class ChromaticAbberationData extends ScreenEffectData {

    public static StreamCodec<RegistryFriendlyByteBuf, ChromaticAbberationData> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.FLOAT, v->v.strength,
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
