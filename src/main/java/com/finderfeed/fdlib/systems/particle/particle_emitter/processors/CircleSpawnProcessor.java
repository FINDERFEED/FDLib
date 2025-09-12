package com.finderfeed.fdlib.systems.particle.particle_emitter.processors;

import com.finderfeed.fdlib.FDLib;
import com.finderfeed.fdlib.systems.particle.particle_emitter.EmitterProcessor;
import com.finderfeed.fdlib.systems.particle.particle_emitter.EmitterProcessorType;
import com.finderfeed.fdlib.systems.particle.particle_emitter.FDEmitterProcessorTypes;
import com.finderfeed.fdlib.systems.particle.particle_emitter.ParticleEmitter;
import com.finderfeed.fdlib.util.NetworkCodec;
import com.finderfeed.fdlib.util.math.FDMathUtil;
import com.finderfeed.fdlib.util.rendering.FDRenderUtil;
import net.minecraft.client.particle.Particle;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.NetworkCodec;
import net.minecraft.network.codec.NetworkCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.joml.Random;
import org.joml.Vector4f;

public class CircleSpawnProcessor implements EmitterProcessor<CircleSpawnProcessor> {

    private static final Random r = new Random();
    private Vec3 direction = new Vec3(0,-1,0);
    private float minSpeed = 0;
    private float maxSpeed = 0.1f;
    private float radius = 1f;

    private Matrix4f mt;

    public CircleSpawnProcessor(Vec3 direction,float minSpeed,float maxSpeed,float radius){
        this.direction = direction.normalize();
        this.minSpeed = minSpeed;
        this.maxSpeed = maxSpeed;
        this.radius = radius;
    }

    @Override
    public void initEmitter(ParticleEmitter emitter) {
        Matrix4f mt = new Matrix4f();
        FDRenderUtil.applyMovementMatrixRotations(mt,direction);
        this.mt = mt;
    }

    @Override
    public void tickEmitter(ParticleEmitter emitter) {

    }

    @Override
    public void tickParticle(Particle particle) {

    }

    @Override
    public void initParticle(Particle particle) {

        float x = (r.nextFloat() * 2 - 1) * radius;
        float z = (r.nextFloat() * 2 - 1) * radius;

        while (x * x +  z * z > radius * radius){
            x = (r.nextFloat() * 2 - 1) * radius;
            z = (r.nextFloat() * 2 - 1) * radius;
        }

        Vector4f pos = mt.transform(x,0,z,1f,new Vector4f());

        float sp = FDMathUtil.lerp(minSpeed,maxSpeed,r.nextFloat());

        particle.xo = pos.x + particle.x;
        particle.yo = pos.y + particle.y;
        particle.zo = pos.z + particle.z;
        particle.setPos(pos.x + particle.x,pos.y + particle.y,pos.z + particle.z);

        particle.xd = direction.x * sp;
        particle.yd = direction.y * sp;
        particle.zd = direction.z * sp;

    }

    @Override
    public EmitterProcessorType<CircleSpawnProcessor> type() {
        return FDEmitterProcessorTypes.CIRCLE_SPAWN_PROCESSOR;
    }

    public static class Type implements EmitterProcessorType<CircleSpawnProcessor>{

        public static final NetworkCodec<CircleSpawnProcessor> STREAM_CODEC = NetworkCodec.composite(
                NetworkCodec.VEC3,v->v.direction,
                NetworkCodec.FLOAT,v->v.minSpeed,
                NetworkCodec.FLOAT,v->v.maxSpeed,
                NetworkCodec.FLOAT,v->v.radius,
                (direction,minSpeed,maxSpeed,radius)->{
                    return new CircleSpawnProcessor(direction,minSpeed,maxSpeed,radius);
                }
        );

        @Override
        public NetworkCodec<CircleSpawnProcessor> codec() {
            return STREAM_CODEC;
        }

        @Override
        public ResourceLocation id() {
            return ResourceLocation.tryBuild(FDLib.MOD_ID,"circle_spawn_processor");
        }

    }

}
