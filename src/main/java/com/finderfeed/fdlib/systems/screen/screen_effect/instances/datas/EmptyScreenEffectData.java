package com.finderfeed.fdlib.systems.screen.screen_effect.instances.datas;

import com.finderfeed.fdlib.systems.screen.screen_effect.ScreenEffectData;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public class EmptyScreenEffectData extends ScreenEffectData {

    public static final StreamCodec<RegistryFriendlyByteBuf, EmptyScreenEffectData> STREAM_CODEC = StreamCodec.unit(new EmptyScreenEffectData());

    public EmptyScreenEffectData(){

    }

}
