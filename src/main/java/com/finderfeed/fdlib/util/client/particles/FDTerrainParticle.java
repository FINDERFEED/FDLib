package com.finderfeed.fdlib.util.client.particles;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.TerrainParticle;
import org.jetbrains.annotations.Nullable;

public class FDTerrainParticle extends TerrainParticle {

    public FDTerrainParticle(ClientLevel level, double x, double y, double z, double xd, double yd, double zd, FDBlockParticleOptions options) {
        super(level, x, y, z, xd, yd, zd, options.state);
        this.x = x;
        this.y = y;
        this.z = z;
        this.xd = xd;
        this.yd = yd;
        this.zd = zd;
        this.lifetime = options.lifetime;
        this.quadSize *= options.quadSizeMultiplier;
    }

    public static class Provider implements ParticleProvider<FDBlockParticleOptions> {
        @Nullable
        @Override
        public Particle createParticle(FDBlockParticleOptions options, ClientLevel level, double x, double y, double z, double xd, double yd, double zd) {
            return new FDTerrainParticle(level,x,y,z,xd,yd,zd,options);
        }
    }

}
