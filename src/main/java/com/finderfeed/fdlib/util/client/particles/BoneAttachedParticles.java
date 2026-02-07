package com.finderfeed.fdlib.util.client.particles;

import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.AnimatedObject;
import com.finderfeed.fdlib.systems.bedrock.models.FDModel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class BoneAttachedParticles {

    private AnimatedObject animatedObject;
    private List<Particle> particles;
    private FDModel fdModel;
    private String bone;
    private Vec3 offset;

    public BoneAttachedParticles(FDModel model,AnimatedObject animatedObject, String bone, Vec3 offset){
        this.animatedObject = animatedObject;
        this.particles = new ArrayList<>();
        this.fdModel = model;
        this.bone = bone;
        this.offset = offset;
    }

    public void clientTick(Vec3 worldPos){
        if (!particles.isEmpty()) {
            Vec3 particlePos = this.getWorldPos(worldPos);
            var iterator = particles.iterator();
            while (iterator.hasNext()) {
                var particle = iterator.next();
                if (!particle.isAlive()) {
                    iterator.remove();
                } else {
                    particle.setPos(particlePos.x, particlePos.y, particlePos.z);
                }
            }
        }
    }

    public void addParticle(ParticleOptions particleOptions, Vec3 worldPos){
        Vec3 particlePos = this.getWorldPos(worldPos);
        var particleEngine = Minecraft.getInstance().particleEngine;
        var particle = particleEngine.createParticle(particleOptions, particlePos.x,particlePos.y,particlePos.z,0,0,0);
        if (particle != null){
            this.particles.add(particle);
        }
    }

    private Vec3 getWorldPos(Vec3 worldPos){
        var transformation = this.getTransformation();
        Vec3 particlePos = new Vec3(transformation.transformPosition(offset.toVector3f())).add(worldPos);
        return particlePos;
    }

    private Matrix4f getTransformation(){
        if (animatedObject instanceof Entity entity){
            return animatedObject.getModelPartTransformation(entity, bone, fdModel);
        }else{
            return animatedObject.getModelPartTransformation(bone, fdModel);
        }
    }

    public AnimatedObject getAnimatedObject() {
        return animatedObject;
    }

    public FDModel getFdModel() {
        return fdModel;
    }

}
