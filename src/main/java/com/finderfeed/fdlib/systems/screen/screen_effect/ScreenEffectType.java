package com.finderfeed.fdlib.systems.screen.screen_effect;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public class ScreenEffectType<T extends ScreenEffectData, S extends ScreenEffect<T>> {

    public StreamCodec<RegistryFriendlyByteBuf, T> dataCodec;
    public ScreenEffectFactory<T,S> factory;

    private ScreenEffectType(StreamCodec<RegistryFriendlyByteBuf, T> dataCodec, ScreenEffectFactory<T,S> factory){
        this.dataCodec = dataCodec;
        this.factory = factory;
    }

    public static <T extends ScreenEffectData, S extends ScreenEffect<T>> ScreenEffectType<T,S> create(ScreenEffectFactory<T,S> factory, StreamCodec<RegistryFriendlyByteBuf, T> dataCodec){
        return new ScreenEffectType<>(dataCodec,factory);
    }

}
