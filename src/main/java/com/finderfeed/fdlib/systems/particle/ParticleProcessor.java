package com.finderfeed.fdlib.systems.particle;

import com.finderfeed.fdlib.systems.stream_codecs.NetworkCodec;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import net.minecraft.client.particle.Particle;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public interface ParticleProcessor<T extends ParticleProcessor<T>> {

    static final Codec<ResourceLocation> RL_CODEC = ResourceLocation.CODEC.fieldOf("type").codec();

    public static final Codec<ParticleProcessor<?>> CODEC = new Codec<ParticleProcessor<?>>() {
        @Override
        public <D> DataResult<Pair<ParticleProcessor<?>, D>> decode(DynamicOps<D> ops, D input) {

            var result = RL_CODEC.decode(ops,input);
            var location = result.getOrThrow(false,(s)->{}).getFirst();
            var type = FDParticleProcessors.getType(location);
            Codec<ParticleProcessor<?>> particleProcessorCodec = (Codec<ParticleProcessor<?>>) type.codec();
            var processorResult = particleProcessorCodec.decode(ops,input);

            return processorResult;
        }

        @Override
        public <D> DataResult<D> encode(ParticleProcessor<?> value, DynamicOps<D> ops, D prefix) {

            ParticleProcessorType<?> type = value.type();
            Codec<ParticleProcessor<?>> codec = (Codec<ParticleProcessor<?>>) type.codec();
            var result = RL_CODEC.encode(type.id(),ops,prefix).flatMap(f->codec.encode(value,ops,f));

            return result;
        }
    };




    public static final NetworkCodec<ParticleProcessor<?>> STREAM_CODEC = new NetworkCodec<ParticleProcessor<?>>() {
        @Override
        public ParticleProcessor<?> fromNetwork(FriendlyByteBuf buf) {
            var location = buf.readResourceLocation();
            var type =  FDParticleProcessors.getType(location);
            var codec = type.networkCodec();
            return codec.fromNetwork(buf);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, ParticleProcessor<?> processor) {
            buf.writeResourceLocation(processor.type().id());
            hackyEncode(buf,processor,processor.type().networkCodec());
        }
    };




    ParticleProcessorType<T> type();

    void processParticle(Particle particle);

    void init(Particle particle);

    private static <T extends ParticleProcessor<T>> void hackyEncode(FriendlyByteBuf buf,ParticleProcessor<?> processor,NetworkCodec<T> encoder){
        encoder.toNetwork(buf,(T)processor);
    }

}
