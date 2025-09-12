package com.finderfeed.fdlib.systems.particle;

import com.finderfeed.fdlib.FDLib;
import com.finderfeed.fdlib.systems.stream_codecs.NetworkCodec;
import com.mojang.serialization.Codec;
import net.minecraft.client.particle.Particle;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public class EmptyParticleProcessor implements ParticleProcessor<EmptyParticleProcessor> {

    public static final EmptyParticleProcessor INSTANCE = new EmptyParticleProcessor();

    public EmptyParticleProcessor(){}

    @Override
    public ParticleProcessorType<EmptyParticleProcessor> type() {
        return FDParticleProcessors.EMPTY_PROCESSOR;
    }

    @Override
    public void processParticle(Particle particle) {


    }

    @Override
    public void init(Particle particle) {

    }

    public static class Type implements ParticleProcessorType<EmptyParticleProcessor>{

        public static final NetworkCodec<EmptyParticleProcessor> STREAM_CODEC = new NetworkCodec<EmptyParticleProcessor>() {
            @Override
            public EmptyParticleProcessor fromNetwork(FriendlyByteBuf buf) {
                return new EmptyParticleProcessor();
            }

            @Override
            public void toNetwork(FriendlyByteBuf p_320158_, EmptyParticleProcessor p_320396_) {

            }
        };

        public static final Codec<EmptyParticleProcessor> CODEC = Codec.unit(INSTANCE);

        @Override
        public NetworkCodec<EmptyParticleProcessor> networkCodec() {
            return STREAM_CODEC;
        }

        @Override
        public Codec<EmptyParticleProcessor> codec() {
            return CODEC;
        }

        @Override
        public ResourceLocation id() {
            return ResourceLocation.tryBuild(FDLib.MOD_ID,"empty_processor");
        }
    }

}
