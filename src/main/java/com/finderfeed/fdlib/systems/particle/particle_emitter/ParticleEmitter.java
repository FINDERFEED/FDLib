package com.finderfeed.fdlib.systems.particle.particle_emitter;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.phys.Vec3;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class ParticleEmitter {

    private static final Random random = new Random();

    public ParticleEmitterData data;

    public boolean removed;

    public int age;

    private List<Particle> activeParticles = new LinkedList<>();

    public ParticleEmitter(ParticleEmitterData data){
        this.data = data;
        this.age = 0;
        this.removed = false;
    }

    public void tick(){
        ParticleEngine engine = Minecraft.getInstance().particleEngine;

        age++;


        data.processor.tickEmitter(this);

        var types = data.particleTypes;

        for (int i = 0; i < data.particlesPerTick;i++) {
            Particle newParticle = engine.createParticle(types.get(random.nextInt(types.size())), data.position.x, data.position.y, data.position.z, 0, 0, 0);
            if (newParticle != null) {
                activeParticles.add(newParticle);
                data.processor.initParticle(newParticle);
            }
        }


        var iterator = activeParticles.listIterator();
        while (iterator.hasNext()){

            var particle = iterator.next();

            if (!particle.isAlive()){
                iterator.remove();
                continue;
            }

            data.processor.tickParticle(particle);
        }



        if (age > data.lifetime){
            this.removed = true;
        }
    }

    public void init(){
        data.processor.initEmitter(this);
    }



}
