package com.finderfeed.fdlib.systems.particle.particle_emitter;

import net.minecraft.client.particle.Particle;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public interface EmitterProcessor<T extends EmitterProcessor<T>> {

    public static StreamCodec<FriendlyByteBuf,EmitterProcessor<?>> STREAM_CODEC = new StreamCodec<FriendlyByteBuf, EmitterProcessor<?>>() {

        @Override
        public void encode(FriendlyByteBuf buf, EmitterProcessor<?> emitter) {
            buf.writeResourceLocation(emitter.type().id());
            hackyEncode(buf,emitter);
        }

        @Override
        public EmitterProcessor<?> decode(FriendlyByteBuf buf) {

            var location = buf.readResourceLocation();

            EmitterProcessorType<?> type = FDEmitterProcessorTypes.get(location);
            if (type == null) throw new RuntimeException("Emitter processor type unknown: " + location);

            EmitterProcessor<?> processor = type.codec().decode(buf);

            return processor;
        }

    };

    void initEmitter(ParticleEmitter emitter);

    void tickEmitter(ParticleEmitter emitter);

    void tickParticle(Particle particle);

    void initParticle(Particle particle);

    EmitterProcessorType<T> type();


    static <T extends EmitterProcessor<T>> void hackyEncode(FriendlyByteBuf buf, EmitterProcessor<T> emitterProcessor){
        emitterProcessor.type().codec().encode(buf,(T)emitterProcessor);
    }
}

