package com.finderfeed.fdlib.to_other_mod;

import com.finderfeed.fdlib.to_other_mod.client.particles.ball_particle.BallParticleOptions;
import com.finderfeed.fdlib.to_other_mod.entities.earthshatter_entity.EarthShatterEntity;
import com.finderfeed.fdlib.to_other_mod.entities.earthshatter_entity.EarthShatterSettings;
import com.finderfeed.fdlib.to_other_mod.packets.SlamParticlesPacket;
import com.finderfeed.fdlib.util.client.FDBlockParticleOptions;
import com.finderfeed.fdlib.util.math.FDMathUtil;
import com.finderfeed.fdlib.util.rendering.FDEasings;
import com.finderfeed.fdlib.util.rendering.FDRenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.joml.Quaternionf;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BossClientPackets {

    private static Random random = new Random();

    public static void posEvent(Vec3 pos,int event,int data){
        switch (event) {
            case BossUtil.CHESED_GET_BLOCKS_FROM_EARTH_EVENT -> {
                summonBlocksOutOfEarthParticles(pos,5);
            }
            case BossUtil.RADIAL_EARTHQUAKE_PARTICLES -> {
                radialEarthquakeParticles(pos,data);
            }
            case BossUtil.ROCKFALL_PARTICLES -> {
                rockfallParticles(pos,data);
            }
            case BossUtil.CHESED_RAY_EXPLOSION -> {
                rayExplosion(pos,data);
            }
        }
    }

    public static void rayExplosion(Vec3 pos,int data){

        int dx = data >> 16;
        int dy = (data & 0x00ff00) >> 8;
        int dz = data & 0x0000ff;

        Vec3 direction = new Vec3(
                (dx / (double) 0xff) * 2 - 1,
                (dy / (double) 0xff) * 2 - 1,
                (dz / (double) 0xff) * 2 - 1
        );

        Matrix4f mt = new Matrix4f();
        FDRenderUtil.applyMovementMatrixRotations(mt,direction);


        Level level = Minecraft.getInstance().level;
        int maxCount = 10;
        int maxParticlePerCount = 15;
        float maxVerticalSpeed = 5f;
        float maxHorizontalSpeed = maxVerticalSpeed / 4f;
        float maxFriction = 0.7f;
        float maxSize = 2.5f;

        for (int i = 0; i < maxCount;i++){

            float p = i / 10f;
            float angle = p * FDMathUtil.FPI * 2;
            float rangle = FDMathUtil.FPI * 2f / maxCount / 2;
            for (int g = 0; g < maxParticlePerCount;g++){
                Vec3 dir = new Vec3(1,0,0).yRot(angle + random.nextFloat() * rangle * 2 - rangle);
                Vec3 ppos = pos.add(dir.multiply(1.5,1.5,1.5));
                float p2 = g / (float) (maxParticlePerCount - 1);

                float size = maxSize / 2 * (1 - p2) + maxSize / 2;
                float yspeed = maxVerticalSpeed * p2 + random.nextFloat() * maxVerticalSpeed / 5;
                float zxspeedAddition = random.nextFloat() * maxHorizontalSpeed * 2 - maxHorizontalSpeed;
                float zxspeed = (maxHorizontalSpeed + zxspeedAddition / 4) * p2;

                BallParticleOptions options = BallParticleOptions.builder()
                        .friction(maxFriction)
                        .size(size)
                        .color(100 + random.nextInt(50), 255, 255)
                        .scalingOptions(3,0,50)
                        .build();


                level.addParticle(options,true,ppos.x,ppos.y,ppos.z,

                        dir.x * zxspeed,
                        -yspeed,
                        dir.z * zxspeed
                        );



            }
        }
    }

    public static void rockfallParticles(Vec3 tpos,int maxRad){

        for (int rad = 0; rad < maxRad;rad++){
            Vec3 b = new Vec3(rad,0,0);
            float angle;
            if (rad != 0){
                angle = 0.5f / rad;
            }else{
                angle = FDMathUtil.FPI * 2;
            }
            Level level = Minecraft.getInstance().level;


            for (float i = 0; i <= FDMathUtil.FPI * 2;i += angle){
                Vec3 v = b.yRot(i);
                level.addParticle(ParticleTypes.CAMPFIRE_SIGNAL_SMOKE,true,
                        tpos.x + v.x + random.nextFloat() - 0.5,
                        tpos.y + v.y + random.nextFloat() * 0.1 - 0.05,
                        tpos.z + v.z + random.nextFloat() - 0.5,

                        random.nextFloat() * 0.025 -  0.0125,
                        -0.015 - random.nextFloat() * 0.015,
                        random.nextFloat() * 0.025 -  0.0125
                        );


            }
        }

    }

    public static void radialEarthquakeParticles(Vec3 tpos,int rad){
        Vec3 b = new Vec3(rad,0,0);
        float angle;
        if (rad != 0){
            angle = 1f / rad;
        }else{
            angle = FDMathUtil.FPI * 2;
        }
        Level level = Minecraft.getInstance().level;

        BlockPos prevPos = null;
        for (float i = 0; i < FDMathUtil.FPI * 2;i += angle){
            Vec3 pos = tpos.add(b.yRot(i));
            BlockPos ppos = FDMathUtil.vec3ToBlockPos(pos);
            if (!ppos.equals(prevPos)){
                Vec3 c = ppos.getCenter();
                Vec3 dir = tpos.subtract(c).multiply(1,0,1).normalize();

                BlockState state = level.getBlockState(ppos);
                if (state.isAir()) continue;


                    Vec3 sppos = new Vec3(
                            c.x + random.nextFloat() * 2 - 1 - dir.x,
                            c.y + 0.1 + random.nextFloat() * 0.19,
                            c.z + random.nextFloat() * 2 - 1 - dir.z
                    );

                    Vec3 speed = dir.yRot(FDMathUtil.FPI / 4 * (random.nextFloat() * 2 - 1)).multiply(0.075,0.,0.075).add(0,0.25f + random.nextFloat() * 0.2f,0);

                    FDBlockParticleOptions options = FDBlockParticleOptions.builder()
                            .lifetime(10 + random.nextInt(5))
                            .state(state)
                            .quadSizeMultiplier(1 + random.nextFloat() * 0.2f)
                            .build();

                    level.addParticle(options,sppos.x,sppos.y,sppos.z,speed.x,speed.y,speed.z);

//                }
            }
            prevPos = ppos;
        }
    }

    public static void summonBlocksOutOfEarthParticles(Vec3 pos,float radius){
        Vec3 p = new Vec3(radius,0,0);
        float rotationAngle = 1 / radius;

        Vec3 poss = pos.add(p);
        for (float i = 0; i <= FDMathUtil.FPI * 2f; i += rotationAngle){
            Vec3 pose = pos.add(p.yRot(i + rotationAngle));
            var bpos = FDMathUtil.vec3ToBlockPos(poss).below();
            BlockState state = Minecraft.getInstance().level.getBlockState(bpos);
            if (state.isAir()) continue;
            Vec3 between = pose.subtract(poss).normalize();
            for (int k = 0; k < 5 + random.nextInt(5);k++){
                FDBlockParticleOptions options = FDBlockParticleOptions.builder()
                        .state(state)
                        .quadSizeMultiplier(1.5f + random.nextFloat() * 0.5f)
                        .lifetime(30 + random.nextInt(20))
                        .build();

                float speedMod = random.nextFloat() + 0.5f;

                Minecraft.getInstance().level.addParticle(options,true,
                        poss.x + random.nextFloat() * 2 - 1,
                        poss.y,
                        poss.z + random.nextFloat() * 2 - 1,
                        between.x * 0.3 * speedMod,
                        random.nextFloat() * 0.8f + 0.1f,
                        between.z * 0.3 * speedMod
                );

            }
            poss = pose;
        }
    }


    public static void blockProjectileSlamParticles(SlamParticlesPacket.SlamData slamData){
        Level level = Minecraft.getInstance().level;
        var states = collectStatesForSlam(level,slamData.collectRadius,slamData.bPos);
        if (states.isEmpty()) return;
        Vec3 horizontal = slamData.direction.multiply(1,0,1).normalize();
        float angle = slamData.maxAngle / slamData.count;
        float half = slamData.maxAngle / 2;
        FDBlockParticleOptions options;

        for (int i = 0; i <= slamData.count;i++){
            float a = -half + angle * i;
            Vec3 rot = horizontal.yRot(a + (random.nextFloat() * 2 - 1) * angle * 0.5f);
            float p = 1 - Math.abs(a) / half;
            p = FDEasings.easeOut(p);
            float verticalSpeed = FDMathUtil.lerp(slamData.maxVerticalSpeedEdges,slamData.maxVerticalSpeedCenter,p);

            int rowParticleCount = Math.round(slamData.maxSpeed / slamData.perRowDivide);

            for (int k = 1; k <= rowParticleCount + 1;k++){
                float percent = (k / (float)rowParticleCount);
                float sp = slamData.maxSpeed * percent + (random.nextFloat() * 2 - 1) * slamData.perRowDivide;
                float vsp = verticalSpeed * percent + random.nextFloat() * verticalSpeed * -0.5f;

                int rnd = slamData.maxParticleLifetime / 4;
                if (rnd != 0){
                    rnd = random.nextInt(rnd);
                }
                int lifetime = slamData.maxParticleLifetime - rnd;

                options = FDBlockParticleOptions.builder()
                        .state(states.get(random.nextInt(states.size())))
                        .lifetime(lifetime)
                        .quadSizeMultiplier(slamData.particleSizeMult)
                        .build();


                level.addParticle(options,true,slamData.pos.x,slamData.pos.y,slamData.pos.z,
                        rot.x * sp,
                        vsp,
                        rot.z * sp
                );

            }
        }

    }

    private static List<BlockState> collectStatesForSlam(Level level, int collectRadius, BlockPos pos){
        List<BlockState> states = new ArrayList<>();
        for (int x = -collectRadius;x <= collectRadius;x++){
            for (int y = -collectRadius;y <= collectRadius;y++){
                for (int z = -collectRadius;z <= collectRadius;z++){
                    BlockPos p = pos.offset(x,y,z);
                    var state = level.getBlockState(p);
                    if (!state.isAir()){
                        states.add(state);
                    }
                }
            }
        }
        return states;
    }

    public static void handleEarthShatterSpawnPacket(int entityId, EarthShatterSettings settings){
        if (Minecraft.getInstance().level.getEntity(entityId) instanceof EarthShatterEntity entity){
            entity.settings = settings;
        }
    }

}
