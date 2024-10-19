package com.finderfeed.fdlib.systems.particle;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.Lifecycle;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.particle.Particle;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;

public interface ParticleProcessor<T extends ParticleProcessor<T>> {

    static final Codec<ResourceLocation> RL_CODEC = ResourceLocation.CODEC.fieldOf("type").codec();

    public static final Codec<ParticleProcessor<?>> CODEC = new Codec<ParticleProcessor<?>>() {
        @Override
        public <D> DataResult<Pair<ParticleProcessor<?>, D>> decode(DynamicOps<D> ops, D input) {

            var result = RL_CODEC.decode(ops,input);
            var location = result.getOrThrow().getFirst();
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




    public static final StreamCodec<FriendlyByteBuf,ParticleProcessor<?>> STREAM_CODEC = new StreamCodec<FriendlyByteBuf, ParticleProcessor<?>>() {
        @Override
        public ParticleProcessor<?> decode(FriendlyByteBuf buf) {
            var location = buf.readResourceLocation();
            var type =  FDParticleProcessors.getType(location);
            var codec = type.streamCodec();
            return codec.decode(buf);
        }

        @Override
        public void encode(FriendlyByteBuf buf, ParticleProcessor<?> processor) {
            buf.writeResourceLocation(processor.type().id());
            hackyEncode(buf,processor,processor.type().streamCodec());
        }
    };




    ParticleProcessorType<T> type();

    void processParticle(Particle particle);

    private static <T extends ParticleProcessor<T>> void hackyEncode(FriendlyByteBuf buf,ParticleProcessor<?> processor,StreamCodec<FriendlyByteBuf,T> encoder){
        encoder.encode(buf,(T)processor);
    }

}
