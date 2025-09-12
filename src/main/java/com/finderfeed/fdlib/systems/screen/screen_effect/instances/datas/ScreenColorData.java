package com.finderfeed.fdlib.systems.screen.screen_effect.instances.datas;

import com.finderfeed.fdlib.systems.screen.screen_effect.ScreenEffectData;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.NetworkCodec;
import net.minecraft.network.codec.NetworkCodec;

public class ScreenColorData extends ScreenEffectData {

    public static final NetworkCodec<ScreenColorData> STREAM_CODEC = NetworkCodec.composite(
            NetworkCodec.FLOAT,v->v.r,
            NetworkCodec.FLOAT,v->v.g,
            NetworkCodec.FLOAT,v->v.b,
            NetworkCodec.FLOAT,v->v.a,
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
