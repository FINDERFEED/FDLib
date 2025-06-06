package com.finderfeed.fdlib.systems.particle;

import com.finderfeed.fdlib.FDLib;
import com.finderfeed.fdlib.util.FDByteBufCodecs;
import com.finderfeed.fdlib.util.FDCodecs;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.particle.Particle;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;

public class SetParticleSpeedProcessor implements ParticleProcessor<SetParticleSpeedProcessor> {

    private Vec3 speed;

    public SetParticleSpeedProcessor(Vec3 speed){
        this.speed = speed;
    }

    @Override
    public ParticleProcessorType<SetParticleSpeedProcessor> type() {
        return FDParticleProcessors.SET_PARTICLE_SPEED;
    }

    @Override
    public void processParticle(Particle particle) {

    }

    @Override
    public void init(Particle particle) {
        particle.xd = speed.x;
        particle.yd = speed.y;
        particle.zd = speed.z;
    }

    public static class Type implements ParticleProcessorType<SetParticleSpeedProcessor>{

        public static final StreamCodec<FriendlyByteBuf,SetParticleSpeedProcessor> STREAM_CODEC = StreamCodec.composite(
                FDByteBufCodecs.VEC3,v->v.speed,
                SetParticleSpeedProcessor::new
        );

        public static final Codec<SetParticleSpeedProcessor> CODEC = RecordCodecBuilder.create(p->p.group(
                FDCodecs.VEC3.fieldOf("speed").forGetter(v->v.speed)
        ).apply(p,SetParticleSpeedProcessor::new));

        @Override
        public StreamCodec<FriendlyByteBuf, SetParticleSpeedProcessor> streamCodec() {
            return STREAM_CODEC;
        }

        @Override
        public Codec<SetParticleSpeedProcessor> codec() {
            return CODEC;
        }

        @Override
        public ResourceLocation id() {
            return ResourceLocation.tryBuild(FDLib.MOD_ID,"set_particle_speed");
        }
    }

}
