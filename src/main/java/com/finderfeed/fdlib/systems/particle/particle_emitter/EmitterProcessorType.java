package com.finderfeed.fdlib.systems.particle.particle_emitter;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.NetworkCodec;
import net.minecraft.resources.ResourceLocation;

public interface EmitterProcessorType<T extends EmitterProcessor<T>> {

    NetworkCodec<T> codec();

    ResourceLocation id();

}
