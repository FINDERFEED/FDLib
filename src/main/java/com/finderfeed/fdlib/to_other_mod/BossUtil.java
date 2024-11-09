package com.finderfeed.fdlib.to_other_mod;

import com.finderfeed.fdlib.to_other_mod.packets.PosLevelEventPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.PacketDistributor;

public class BossUtil {


    public static final TargetingConditions ALL = TargetingConditions.forNonCombat().selector(p->true).ignoreLineOfSight().ignoreInvisibilityTesting();

    public static final int CHESED_GET_BLOCKS_FROM_EARTH_EVENT = 1;
    public static final int RADIAL_EARTHQUAKE_PARTICLES = 2;
    public static final int ROCKFALL_PARTICLES = 3;
    public static final int CHESED_RAY_EXPLOSION = 4;



    public static void chesedRayExplosion(ServerLevel level,Vec3 pos,Vec3 direction,double radius,int particlesCount,float sizeModifier){
        if (particlesCount >= 0xf) throw new RuntimeException("Cannot encode more than 16 particles count");
        if (sizeModifier > 1) throw new RuntimeException("Cannot encode size modifier > 1");

        direction = direction.normalize();
        int dx = (int)Math.round((direction.x + 1) / 2 * 0xff);
        int dy = (int)Math.round((direction.y + 1) / 2 * 0xff);
        int dz = (int)Math.round((direction.z + 1) / 2 * 0xff);



        int data = (dx << 16) + (dy << 8) + dz;

        particlesCount = particlesCount << 24;
        data += particlesCount;

        int size = Math.round(sizeModifier * 0xf);
        size = size << 28;

        data += size;

        posEvent(level,pos,CHESED_RAY_EXPLOSION,data,radius);

    }

    public static void posEvent(ServerLevel level, Vec3 pos, int event,int data,double radius){
        PacketDistributor.sendToPlayersNear(level,null,pos.x,pos.y,pos.z,radius,new PosLevelEventPacket(pos,event,data));
    }

}
