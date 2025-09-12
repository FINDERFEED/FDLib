package com.finderfeed.fdlib.systems.particle;

import com.finderfeed.fdlib.systems.stream_codecs.NetworkCodec;
import com.mojang.serialization.Codec;
import net.minecraft.resources.ResourceLocation;

public interface ParticleProcessorType<T extends ParticleProcessor<T>> {



    NetworkCodec<T> networkCodec();

    Codec<T> codec();

    ResourceLocation id();

}
