package com.finderfeed.fdlib.systems.particle;

import com.finderfeed.fdlib.FDLib;
import com.finderfeed.fdlib.util.FDByteBufCodecs;
import com.finderfeed.fdlib.util.FDCodecs;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.particle.Particle;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;

public class CircleParticleProcessor implements ParticleProcessor<CircleParticleProcessor> {

    private Vec3 point;
    private boolean forward;
    private int circleCount;

    private Vec3 initialPoint = null;

    public CircleParticleProcessor(Vec3 point,boolean forward,int circleCount){
        this.point = point;
        this.forward = forward;
        this.circleCount = circleCount;
    }

    @Override
    public ParticleProcessorType<CircleParticleProcessor> type() {
        return FDParticleProcessors.CIRCLE_PARTICLE_PROCESSOR;
    }

    @Override
    public void processParticle(Particle particle) {
        if (initialPoint == null){
            initialPoint = new Vec3(particle.x,particle.y,particle.z);
        }

        float p = particle.age / (float) particle.lifetime;




    }


    public static class Type implements ParticleProcessorType<CircleParticleProcessor>{

        public static final StreamCodec<FriendlyByteBuf,CircleParticleProcessor> STREAM_CODEC = StreamCodec.composite(
                FDByteBufCodecs.VEC3,v->v.point,
                ByteBufCodecs.BOOL,v->v.forward,
                ByteBufCodecs.INT,v->v.circleCount,
                CircleParticleProcessor::new
        );

        public static final Codec<CircleParticleProcessor> CODEC = RecordCodecBuilder.create(p->p.group(
                FDCodecs.VEC3.fieldOf("point").forGetter(v->v.point),
                Codec.BOOL.fieldOf("forward").forGetter(v->v.forward),
                Codec.INT.fieldOf("circleCount").forGetter(v->v.circleCount)
        ).apply(p,CircleParticleProcessor::new));

        @Override
        public StreamCodec<FriendlyByteBuf, CircleParticleProcessor> streamCodec() {
            return STREAM_CODEC;
        }

        @Override
        public Codec<CircleParticleProcessor> codec() {
            return CODEC;
        }

        @Override
        public ResourceLocation id() {
            return ResourceLocation.tryBuild(FDLib.MOD_ID,"circle_particle_processor");
        }
    }

}
