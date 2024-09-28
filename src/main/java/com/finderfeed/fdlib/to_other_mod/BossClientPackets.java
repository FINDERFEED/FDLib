package com.finderfeed.fdlib.to_other_mod;

import com.finderfeed.fdlib.to_other_mod.entities.earthshatter_entity.EarthShatterEntity;
import com.finderfeed.fdlib.to_other_mod.entities.earthshatter_entity.EarthShatterSettings;
import com.finderfeed.fdlib.to_other_mod.packets.SlamParticlesPacket;
import com.finderfeed.fdlib.util.client.FDBlockParticleOptions;
import com.finderfeed.fdlib.util.math.FDMathUtil;
import com.finderfeed.fdlib.util.rendering.FDEasings;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BossClientPackets {

    private static Random random = new Random();

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
