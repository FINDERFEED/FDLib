package com.finderfeed.fdlib.util;

import com.finderfeed.fdlib.init.FDParticles;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class FDProjectile extends AbstractHurtingProjectile {

    public FDProjectile(EntityType<? extends AbstractHurtingProjectile> type, Level level) {
        super(type, level);
        this.xPower = 0;
        this.yPower = 0;
        this.zPower = 0;
    }


    @Override
    public void tick() {
        super.tick();
    }

    @Override
    protected boolean shouldBurn() {
        return false;
    }

    @Override
    protected float getInertia() {
        return 1;
    }

    @Nullable
    @Override
    protected ParticleOptions getTrailParticle() {
        return FDParticles.INVISIBLE.get();
    }
}
