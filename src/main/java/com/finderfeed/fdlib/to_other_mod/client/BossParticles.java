package com.finderfeed.fdlib.to_other_mod.client;

import com.finderfeed.fdlib.FDLib;
import com.finderfeed.fdlib.to_other_mod.client.particles.arc_lightning.ArcLightningOptions;
import com.finderfeed.fdlib.to_other_mod.client.particles.sonic_particle.SonicParticleOptions;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class BossParticles {

    public static final DeferredRegister<ParticleType<?>> PARTICLES = DeferredRegister.create(Registries.PARTICLE_TYPE, FDLib.MOD_ID);


    public static final Supplier<SimpleParticleType> CHESED_RAY_ATTACK = PARTICLES.register("chesed_ray",()->new SimpleParticleType(true));


    public static final Supplier<ParticleType<ArcLightningOptions>> ARC_LIGHTNING = PARTICLES.register("arc",()-> new ParticleType<>(true) {
        @Override
        public MapCodec<ArcLightningOptions> codec() {
            return ArcLightningOptions.createCodec(this);
        }

        @Override
        public StreamCodec<? super RegistryFriendlyByteBuf, ArcLightningOptions> streamCodec() {
            return ArcLightningOptions.createStreamCodec(this);
        }
    });

    public static final Supplier<ParticleType<SonicParticleOptions>> SONIC_PARTICLE = PARTICLES.register("sonic",()-> new ParticleType<>(true) {
        @Override
        public MapCodec<SonicParticleOptions> codec() {
            return SonicParticleOptions.createCodec();
        }

        @Override
        public StreamCodec<? super RegistryFriendlyByteBuf, SonicParticleOptions> streamCodec() {
            return SonicParticleOptions.STREAM_CODEC;
        }
    });



}
