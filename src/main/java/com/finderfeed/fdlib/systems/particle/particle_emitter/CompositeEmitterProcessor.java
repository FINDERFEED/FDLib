package com.finderfeed.fdlib.systems.particle.particle_emitter;

import com.finderfeed.fdlib.FDLib;
import com.finderfeed.fdlib.systems.particle.ParticleProcessor;
import net.minecraft.client.particle.Particle;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.NetworkCodec;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CompositeEmitterProcessor implements EmitterProcessor<CompositeEmitterProcessor> {

    public static CompositeEmitterProcessor create(EmitterProcessor<?>... processors){
        CompositeEmitterProcessor processor = new CompositeEmitterProcessor();
        processor.processors = Arrays.stream(processors).toList();
        return processor;
    }

    private List<EmitterProcessor<?>> processors = new ArrayList<>();

    @Override
    public void initEmitter(ParticleEmitter emitter) {
        for (var processor : processors){
            processor.initEmitter(emitter);
        }
    }

    @Override
    public void tickEmitter(ParticleEmitter emitter) {
        for (var processor : processors){
            processor.tickEmitter(emitter);
        }
    }

    @Override
    public void tickParticle(Particle particle) {
        for (var processor : processors){
            processor.tickParticle(particle);
        }
    }

    @Override
    public void initParticle(Particle particle) {
        for (var processor : processors){
            processor.initParticle(particle);
        }
    }

    @Override
    public EmitterProcessorType<CompositeEmitterProcessor> type() {
        return FDEmitterProcessorTypes.COMPOSITE;
    }

    public static class Type implements EmitterProcessorType<CompositeEmitterProcessor>{

        public static NetworkCodec<CompositeEmitterProcessor> STREAM_CODEC = new NetworkCodec<CompositeEmitterProcessor>() {
            @Override
            public CompositeEmitterProcessor decode(FriendlyByteBuf buf) {

                CompositeEmitterProcessor processor = new CompositeEmitterProcessor();
                int len = buf.readInt();
                for (int i = 0; i < len;i++){
                    processor.processors.add(EmitterProcessor.STREAM_CODEC.fromNetwork(buf));
                }

                return processor;
            }

            @Override
            public void encode(FriendlyByteBuf buf, CompositeEmitterProcessor processor) {

                buf.writeInt(processor.processors.size());
                for (EmitterProcessor<?> p : processor.processors){
                    EmitterProcessor.STREAM_CODEC.toNetwork(buf,p);
                }

            }
        };

        @Override
        public NetworkCodec<CompositeEmitterProcessor> codec() {
            return STREAM_CODEC;
        }

        @Override
        public ResourceLocation id() {
            return ResourceLocation.tryBuild(FDLib.MOD_ID,"composite");
        }
    }
}
