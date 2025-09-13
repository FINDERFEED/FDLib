package com.finderfeed.fdlib.systems.particle.particle_emitter;

import com.finderfeed.fdlib.systems.stream_codecs.NetworkCodec;
import net.minecraft.resources.ResourceLocation;

public interface EmitterProcessorType<T extends EmitterProcessor<T>> {

    NetworkCodec<T> codec();

    ResourceLocation id();

}
