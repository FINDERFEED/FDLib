package com.finderfeed.fdlib.systems.particle.particle_emitter;

import com.finderfeed.fdlib.FDLib;
import com.finderfeed.fdlib.systems.stream_codecs.NetworkCodec;
import net.minecraft.client.particle.Particle;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public class EmptyEmitterProcessor implements EmitterProcessor<EmptyEmitterProcessor> {


    @Override
    public void initEmitter(ParticleEmitter emitter) {

    }

    @Override
    public void tickEmitter(ParticleEmitter emitter) {

    }

    @Override
    public void tickParticle(Particle particle) {

    }

    @Override
    public void initParticle(Particle particle) {

    }

    @Override
    public EmitterProcessorType<EmptyEmitterProcessor> type() {
        return FDEmitterProcessorTypes.EMPTY;
    }

    public static class Type implements EmitterProcessorType<EmptyEmitterProcessor>{



        public static NetworkCodec<EmptyEmitterProcessor> STREAM_CODEC = new NetworkCodec<EmptyEmitterProcessor>() {
            @Override
            public EmptyEmitterProcessor fromNetwork(FriendlyByteBuf p_320376_) {
                return null;
            }

            @Override
            public void toNetwork(FriendlyByteBuf p_320158_, EmptyEmitterProcessor p_320396_) {

            }
        };

        @Override
        public NetworkCodec<EmptyEmitterProcessor> codec() {
            return STREAM_CODEC;
        }

        @Override
        public ResourceLocation id() {
            return ResourceLocation.tryBuild(FDLib.MOD_ID,"empty");
        }
    }
}
