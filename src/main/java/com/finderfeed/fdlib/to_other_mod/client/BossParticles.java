package com.finderfeed.fdlib.to_other_mod.client;

import com.finderfeed.fdlib.FDLib;
import com.finderfeed.fdlib.to_other_mod.client.particles.arc_lightning.ArcLightningOptions;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class BossParticles {

    public static final DeferredRegister<ParticleType<?>> PARTICLES = DeferredRegister.create(Registries.PARTICLE_TYPE, FDLib.MOD_ID);

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



}
