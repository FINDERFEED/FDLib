package com.finderfeed.fdlib.systems.particle.particle_emitter;

import com.finderfeed.fdlib.FDLib;
import net.minecraft.client.particle.Particle;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
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



        public static StreamCodec<FriendlyByteBuf,EmptyEmitterProcessor> STREAM_CODEC = new StreamCodec<FriendlyByteBuf, EmptyEmitterProcessor>() {
            @Override
            public EmptyEmitterProcessor decode(FriendlyByteBuf p_320376_) {
                return null;
            }

            @Override
            public void encode(FriendlyByteBuf p_320158_, EmptyEmitterProcessor p_320396_) {

            }
        };

        @Override
        public StreamCodec<FriendlyByteBuf, EmptyEmitterProcessor> codec() {
            return STREAM_CODEC;
        }

        @Override
        public ResourceLocation id() {
            return ResourceLocation.tryBuild(FDLib.MOD_ID,"empty");
        }
    }
}
