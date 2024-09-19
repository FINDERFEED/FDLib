package com.finderfeed.fdlib.to_other_mod.client;

import com.finderfeed.fdlib.FDLib;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class FDParticles {

    public static final DeferredRegister<ParticleType<?>> PARTICLES = DeferredRegister.create(Registries.PARTICLE_TYPE, FDLib.MOD_ID);

    public static final Supplier<SimpleParticleType> ARC_LIGHTNING = PARTICLES.register("arc",()->new SimpleParticleType(true));



}
