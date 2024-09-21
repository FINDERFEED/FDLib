package com.finderfeed.fdlib.init;

import com.finderfeed.fdlib.FDLib;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class FDParticles {

    public static final DeferredRegister<ParticleType<?>> PARTICLES = DeferredRegister.create(Registries.PARTICLE_TYPE, FDLib.MOD_ID);

    public static final Supplier<SimpleParticleType> INVISIBLE = PARTICLES.register("invisible",()->new SimpleParticleType(true));


}
