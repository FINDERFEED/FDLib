package com.finderfeed.fdlib.systems.particle.particle_emitter;

import com.finderfeed.fdlib.systems.stream_codecs.NetworkCodec;
import net.minecraft.client.particle.Particle;
import net.minecraft.network.FriendlyByteBuf;

public interface EmitterProcessor<T extends EmitterProcessor<T>> {

    public static NetworkCodec<EmitterProcessor<?>> STREAM_CODEC = new NetworkCodec<EmitterProcessor<?>>() {

        @Override
        public void toNetwork(FriendlyByteBuf buf, EmitterProcessor<?> emitter) {
            buf.writeResourceLocation(emitter.type().id());
            hackyEncode(buf,emitter);
        }

        @Override
        public EmitterProcessor<?> fromNetwork(FriendlyByteBuf buf) {

            var location = buf.readResourceLocation();

            EmitterProcessorType<?> type = FDEmitterProcessorTypes.get(location);
            if (type == null) throw new RuntimeException("Emitter processor type unknown: " + location);

            EmitterProcessor<?> processor = type.codec().fromNetwork(buf);

            return processor;
        }

    };

    void initEmitter(ParticleEmitter emitter);

    void tickEmitter(ParticleEmitter emitter);

    void tickParticle(Particle particle);

    void initParticle(Particle particle);

    EmitterProcessorType<T> type();


    static <T extends EmitterProcessor<T>> void hackyEncode(FriendlyByteBuf buf, EmitterProcessor<T> emitterProcessor){
        emitterProcessor.type().codec().toNetwork(buf,(T)emitterProcessor);
    }
}

