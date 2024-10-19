package com.finderfeed.fdlib.systems.particle;

import com.finderfeed.fdlib.FDLib;
import com.mojang.serialization.Codec;
import net.minecraft.client.particle.Particle;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;

public class CompositeParticleProcessor implements ParticleProcessor<CompositeParticleProcessor> {

    private ParticleProcessor[] processors;

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

    public static class Type implements ParticleProcessorType<CompositeParticleProcessor> {

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
            return null;
        }

        @Override
        public ResourceLocation id() {
            return ResourceLocation.tryBuild(FDLib.MOD_ID,"composite");
        }
    }
}
