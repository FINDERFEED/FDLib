package com.finderfeed.fdlib.systems.particle;

import com.mojang.serialization.Codec;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.NetworkCodec;
import net.minecraft.resources.ResourceLocation;

public interface ParticleProcessorType<T extends ParticleProcessor<T>> {



    NetworkCodec<FriendlyByteBuf,T> NetworkCodec();

    Codec<T> codec();

    ResourceLocation id();

}
