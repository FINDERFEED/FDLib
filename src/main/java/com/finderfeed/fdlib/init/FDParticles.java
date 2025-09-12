package com.finderfeed.fdlib.init;

import com.finderfeed.fdlib.FDLib;
import com.finderfeed.fdlib.util.client.particles.FDBlockParticleOptions;
import com.finderfeed.fdlib.util.client.particles.ball_particle.BallParticleOptions;
import com.finderfeed.fdlib.util.client.particles.lightning_particle.LightningParticleOptions;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.Registries;
import net.minecraftforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class FDParticles {

    public static final DeferredRegister<ParticleType<?>> PARTICLES = DeferredRegister.create(Registries.PARTICLE_TYPE, FDLib.MOD_ID);

    public static final Supplier<SimpleParticleType> INVISIBLE = PARTICLES.register("invisible",()->new SimpleParticleType(true));
    public static final Supplier<ParticleType<FDBlockParticleOptions>> TERRAIN_PARTICLE = PARTICLES.register("terrain_particle",()-> new ParticleType<FDBlockParticleOptions>(true, FDBlockParticleOptions.DESERIALIZER) {
        @Override
        public Codec<FDBlockParticleOptions> codec() {
            return FDBlockParticleOptions.CODEC;
        }
    });

    public static final Supplier<ParticleType<BallParticleOptions>> BALL_PARTICLE = PARTICLES.register("ball_particle",()-> new ParticleType<>(true, BallParticleOptions.DESERIALIZER) {
        @Override
        public Codec<BallParticleOptions> codec() {
            return BallParticleOptions.CODEC;
        }
    });


    public static final Supplier<ParticleType<LightningParticleOptions>> LIGHTNING_PARTICLE = PARTICLES.register("lightning_particle",()-> new ParticleType<>(true, LightningParticleOptions.DESERIALIZER) {
        @Override
        public Codec<LightningParticleOptions> codec() {
            return LightningParticleOptions.CODEC;
        }
    });


}
