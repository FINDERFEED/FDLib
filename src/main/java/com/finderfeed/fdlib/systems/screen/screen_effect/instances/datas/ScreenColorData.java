package com.finderfeed.fdlib.systems.screen.screen_effect.instances.datas;

import com.finderfeed.fdlib.systems.screen.screen_effect.ScreenEffectData;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public class ScreenColorData extends ScreenEffectData {

    public static final StreamCodec<RegistryFriendlyByteBuf, ScreenColorData> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.FLOAT,v->v.r,
            ByteBufCodecs.FLOAT,v->v.g,
            ByteBufCodecs.FLOAT,v->v.b,
            ByteBufCodecs.FLOAT,v->v.a,
            ScreenColorData::new
    );

    public float r;
    public float g;
    public float b;
    public float a;

    public ScreenColorData(float r,float g,float b,float a){
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }

}
