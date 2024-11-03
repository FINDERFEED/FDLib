package com.finderfeed.fdlib.systems.particle.particle_emitter;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;

public interface EmitterProcessorType<T extends EmitterProcessor<T>> {

    StreamCodec<FriendlyByteBuf,T> codec();

    ResourceLocation id();

}
