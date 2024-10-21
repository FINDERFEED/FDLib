package com.finderfeed.fdlib.systems.particle;

import com.finderfeed.fdlib.FDLib;
import com.finderfeed.fdlib.util.FDByteBufCodecs;
import com.finderfeed.fdlib.util.FDCodecs;
import com.finderfeed.fdlib.util.math.FDMathUtil;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.particle.Particle;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.joml.*;

public class CircleParticleProcessor implements ParticleProcessor<CircleParticleProcessor> {


    private Vec3 point;
    private boolean forward;
    private int circleCount;

    private Vec3 initialPoint = null;
    private Vector3f axis = null;
    private Vec3 oldSpeed = Vec3.ZERO;

    public CircleParticleProcessor(Vec3 point,boolean forward,int circleCount){
        this.point = point;
        this.forward = forward;
        this.circleCount = circleCount;
    }

    @Override
    public ParticleProcessorType<CircleParticleProcessor> type() {
        return FDParticleProcessors.CIRCLE_PARTICLE_PROCESSOR;
    }

    @Override
    public void processParticle(Particle particle) {
        if (initialPoint == null){
            initialPoint = new Vec3(particle.x,particle.y,particle.z);
            Vec3 b = initialPoint.subtract(point);
            Vec3 left = b.cross(new Vec3(0,1,0));
            Vec3 axis = left.cross(b);
            this.axis = new Vector3f((float)axis.x,(float)axis.y,(float)axis.z);

        }
        if (particle.age > particle.lifetime) return;

        float p2 = particle.age / (float) particle.lifetime;

        double len = initialPoint.subtract(point).length();

        double targetRad = p2 * len;

        float fullAngle = FDMathUtil.FPI * 2 * circleCount;

        float targetAngle = fullAngle * p2;

        if (!forward) targetAngle = -targetAngle;

        Quaternionf q = new Quaternionf(new AxisAngle4f(targetAngle,axis.x,axis.y,axis.z));

        var p = q.transform(
                (initialPoint.x - point.x) / len,
                (initialPoint.y - point.y) / len,
                (initialPoint.z - point.z) / len,
                new Vector3d()
        );

        Vec3 targetPoint = point.add(
                p.x * targetRad,
                p.y * targetRad,
                p.z * targetRad
        );

        Vec3 speed = new Vec3(particle.x,particle.y,particle.z).subtract(targetPoint);

        particle.xd -= oldSpeed.x / (particle.friction);
        particle.yd -= oldSpeed.y / (particle.friction);
        particle.zd -= oldSpeed.z / (particle.friction);

        particle.xd += speed.x;
        particle.yd += speed.y;
        particle.zd += speed.z;

        oldSpeed = speed;
    }


    public static class Type implements ParticleProcessorType<CircleParticleProcessor>{

        public static final StreamCodec<FriendlyByteBuf,CircleParticleProcessor> STREAM_CODEC = StreamCodec.composite(
                FDByteBufCodecs.VEC3,v->v.point,
                ByteBufCodecs.BOOL,v->v.forward,
                ByteBufCodecs.INT,v->v.circleCount,
                CircleParticleProcessor::new
        );

        public static final Codec<CircleParticleProcessor> CODEC = RecordCodecBuilder.create(p->p.group(
                FDCodecs.VEC3.fieldOf("point").forGetter(v->v.point),
                Codec.BOOL.fieldOf("forward").forGetter(v->v.forward),
                Codec.INT.fieldOf("circleCount").forGetter(v->v.circleCount)
        ).apply(p,CircleParticleProcessor::new));

        @Override
        public StreamCodec<FriendlyByteBuf, CircleParticleProcessor> streamCodec() {
            return STREAM_CODEC;
        }

        @Override
        public Codec<CircleParticleProcessor> codec() {
            return CODEC;
        }

        @Override
        public ResourceLocation id() {
            return ResourceLocation.tryBuild(FDLib.MOD_ID,"circle_particle_processor");
        }
    }

}
