package com.finderfeed.fdlib.systems.bedrock.animations.animation_system.model_system.attachments.instances.particle;

import com.finderfeed.fdlib.FDClientHelpers;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

public class ParticleContainer {

    private List<Particle> particles = new ArrayList<>();

    public List<Particle> getParticles() {
        return particles;
    }

    public void addParticle(ParticleOptions particleOptions, Vec3 initialParticlePos){
        ParticleEngine particleEngine = Minecraft.getInstance().particleEngine;
        var particle = particleEngine.createParticle(particleOptions, initialParticlePos.x,initialParticlePos.y,initialParticlePos.z, 0,0,0);
        if (particle != null){
            particles.add(particle);
        }
    }

    public void removeParticle(Particle particle){
        this.particles.remove(particle);
    }

}
