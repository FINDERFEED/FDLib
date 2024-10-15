package com.finderfeed.fdlib.util;

import com.finderfeed.fdlib.to_other_mod.BossUtil;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class FDUtil {


    public static void sendParticles(ServerLevel level, ParticleOptions options, Vec3 pos, double radius){
        for (Player player : level.getNearbyPlayers(BossUtil.ALL,null,new AABB(
                pos.add(-radius,-radius,-radius),
                pos.add(radius,radius,radius)
        ))) {
            ((ServerLevel) level).sendParticles((ServerPlayer) player, options, true, pos.x, pos.y ,pos.z, 1, 0, 0, 0, 0);
        }
    }

    public static void sendParticles(ServerLevel level, ParticleOptions options, Vec3 pos, double radius,int amount,double xd,double yd,double zd,double speed){
        for (Player player : level.getNearbyPlayers(BossUtil.ALL,null,new AABB(
                pos.add(-radius,-radius,-radius),
                pos.add(radius,radius,radius)
        ))) {
            ((ServerLevel) level).sendParticles((ServerPlayer) player, options, true, pos.x, pos.y ,pos.z, amount, xd,yd,zd,speed);
        }
    }

}
