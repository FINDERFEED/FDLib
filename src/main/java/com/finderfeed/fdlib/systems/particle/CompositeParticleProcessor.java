package com.finderfeed.fdlib.systems.particle;

import com.finderfeed.fdlib.FDLib;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.codecs.ListCodec;
import net.minecraft.client.particle.Particle;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;

import java.util.Arrays;
import java.util.List;

public class CompositeParticleProcessor implements ParticleProcessor<CompositeParticleProcessor> {

    private ParticleProcessor<?>[] processors;

    public CompositeParticleProcessor(ParticleProcessor<?>... processors){
        this.processors = processors;
    }

    @Override
    public ParticleProcessorType<CompositeParticleProcessor> type() {
        return FDParticleProcessors.COMPOSITE;
    }

    @Override
    public void processParticle(Particle particle) {
        for (ParticleProcessor<?> particleProcessor : processors){
            particleProcessor.processParticle(particle);
        }
    }

    @Override
    public void init(Particle particle) {
        for (ParticleProcessor<?> particleProcessor : processors){
            particleProcessor.init(particle);
        }
    }

    public static class Type implements ParticleProcessorType<CompositeParticleProcessor> {

        private static Codec<List<ParticleProcessor<?>>> PARTICLE_PROCESSOR_LIST = Codec.list(ParticleProcessor.CODEC).fieldOf("processors").codec();

        public static final ResourceLocation LOCATION = ResourceLocation.tryBuild(FDLib.MOD_ID,"composite");

        public static Codec<CompositeParticleProcessor> CODEC = PARTICLE_PROCESSOR_LIST.xmap(particleProcessors -> {
            ParticleProcessor<?>[] arr = particleProcessors.toArray(new ParticleProcessor<?>[0]);
            return new CompositeParticleProcessor(arr);
        },compositeParticleProcessor -> {
            return Arrays.asList(compositeParticleProcessor.processors);
        });



        public static StreamCodec<FriendlyByteBuf,CompositeParticleProcessor> STREAM_CODEC = new StreamCodec<FriendlyByteBuf, CompositeParticleProcessor>() {
            @Override
            public CompositeParticleProcessor decode(FriendlyByteBuf buf) {
                int len = buf.readInt();
                ParticleProcessor[] processors = new ParticleProcessor[len];
                for (int i = 0; i < processors.length; i++){
                    processors[i] = ParticleProcessor.STREAM_CODEC.decode(buf);
                }
                CompositeParticleProcessor compositeParticleProcessor = new CompositeParticleProcessor(processors);
                return compositeParticleProcessor;
            }

            @Override
            public void encode(FriendlyByteBuf buf, CompositeParticleProcessor processor) {
                int len = processor.processors.length;
                buf.writeInt(len);
                for (ParticleProcessor<?> particleProcessor : processor.processors){
                    ParticleProcessor.STREAM_CODEC.encode(buf,particleProcessor);
                }
            }
        };

        @Override
        public StreamCodec<FriendlyByteBuf, CompositeParticleProcessor> streamCodec() {
            return STREAM_CODEC;
        }

        @Override
        public Codec<CompositeParticleProcessor> codec() {
            return CODEC;
        }

        @Override
        public ResourceLocation id() {
            return LOCATION;
        }
    }
}
