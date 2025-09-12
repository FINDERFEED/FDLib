package com.finderfeed.fdlib.systems.screen.screen_effect.instances.datas;

import com.finderfeed.fdlib.systems.screen.screen_effect.ScreenEffectData;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.NetworkCodec;

public class EmptyScreenEffectData extends ScreenEffectData {

    public static final NetworkCodec<EmptyScreenEffectData> STREAM_CODEC = NetworkCodec.unit(new EmptyScreenEffectData());

    public EmptyScreenEffectData(){

    }

}
