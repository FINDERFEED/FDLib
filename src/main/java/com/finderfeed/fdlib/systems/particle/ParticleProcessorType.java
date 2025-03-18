package com.finderfeed.fdlib.systems.particle;

import com.mojang.serialization.Codec;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;

public interface ParticleProcessorType<T extends ParticleProcessor<T>> {



    StreamCodec<FriendlyByteBuf,T> streamCodec();

    Codec<T> codec();

    ResourceLocation id();

}
