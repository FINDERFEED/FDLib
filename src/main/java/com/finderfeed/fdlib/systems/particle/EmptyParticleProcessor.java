package com.finderfeed.fdlib.systems.particle;

import com.finderfeed.fdlib.FDLib;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
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

        public static final StreamCodec<FriendlyByteBuf,EmptyParticleProcessor> STREAM_CODEC = new StreamCodec<FriendlyByteBuf, EmptyParticleProcessor>() {
            @Override
            public EmptyParticleProcessor decode(FriendlyByteBuf buf) {
                return new EmptyParticleProcessor();
            }

            @Override
            public void encode(FriendlyByteBuf p_320158_, EmptyParticleProcessor p_320396_) {

            }
        };

        public static final Codec<EmptyParticleProcessor> CODEC = Codec.unit(INSTANCE);

        @Override
        public StreamCodec<FriendlyByteBuf, EmptyParticleProcessor> streamCodec() {
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
