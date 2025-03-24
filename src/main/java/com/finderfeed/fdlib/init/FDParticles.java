package com.finderfeed.fdlib.init;

import com.finderfeed.fdlib.FDLib;
import com.finderfeed.fdlib.util.client.particles.FDBlockParticleOptions;
import com.finderfeed.fdlib.util.client.particles.ball_particle.BallParticleOptions;
import com.finderfeed.fdlib.util.client.particles.lightning_particle.LightningParticleOptions;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class FDParticles {

    public static final DeferredRegister<ParticleType<?>> PARTICLES = DeferredRegister.create(Registries.PARTICLE_TYPE, FDLib.MOD_ID);

    public static final Supplier<SimpleParticleType> INVISIBLE = PARTICLES.register("invisible",()->new SimpleParticleType(true));
    public static final Supplier<ParticleType<FDBlockParticleOptions>> TERRAIN_PARTICLE = PARTICLES.register("terrain_particle",()-> new ParticleType<FDBlockParticleOptions>(true) {
        @Override
        public MapCodec<FDBlockParticleOptions> codec() {
            return FDBlockParticleOptions.MAP_CODEC;
        }

        @Override
        public StreamCodec<? super RegistryFriendlyByteBuf, FDBlockParticleOptions> streamCodec() {
            return FDBlockParticleOptions.STREAM_CODEC;
        }
    });

    public static final Supplier<ParticleType<BallParticleOptions>> BALL_PARTICLE = PARTICLES.register("ball_particle",()-> new ParticleType<>(true) {
        @Override
        public MapCodec<BallParticleOptions> codec() {
            return BallParticleOptions.MAP_CODEC;
        }

        @Override
        public StreamCodec<? super RegistryFriendlyByteBuf, BallParticleOptions> streamCodec() {
            return BallParticleOptions.STREAM_CODEC;
        }
    });


    public static final Supplier<ParticleType<LightningParticleOptions>> LIGHTNING_PARTICLE = PARTICLES.register("lightning_particle",()-> new ParticleType<>(true) {
        @Override
        public MapCodec<LightningParticleOptions> codec() {
            return LightningParticleOptions.MAP_CODEC;
        }

        @Override
        public StreamCodec<? super RegistryFriendlyByteBuf, LightningParticleOptions> streamCodec() {
            return LightningParticleOptions.STREAM_CODEC;
        }
    });


}
