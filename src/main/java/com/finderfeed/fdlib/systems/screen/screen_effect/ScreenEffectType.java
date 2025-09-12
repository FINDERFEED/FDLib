package com.finderfeed.fdlib.systems.screen.screen_effect;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.NetworkCodec;

public class ScreenEffectType<T extends ScreenEffectData, S extends ScreenEffect<T>> {

    public NetworkCodec<FriendlyByteBuf, T> dataCodec;
    public ScreenEffectFactory<T,S> factory;

    private ScreenEffectType(NetworkCodec<FriendlyByteBuf, T> dataCodec, ScreenEffectFactory<T,S> factory){
        this.dataCodec = dataCodec;
        this.factory = factory;
    }

    public static <T extends ScreenEffectData, S extends ScreenEffect<T>> ScreenEffectType<T,S> create(ScreenEffectFactory<T,S> factory, NetworkCodec<FriendlyByteBuf, T> dataCodec){
        return new ScreenEffectType<>(dataCodec,factory);
    }

}
