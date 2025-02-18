package com.finderfeed.fdlib;

import com.finderfeed.fdlib.network.lib_packets.PlayerMovePacket;
import com.finderfeed.fdlib.systems.impact_frames.ImpactFrame;
import com.finderfeed.fdlib.systems.impact_frames.ImpactFramesPacket;
import com.finderfeed.fdlib.systems.particle.particle_emitter.ParticleEmitterData;
import com.finderfeed.fdlib.systems.particle.particle_emitter.ParticleEmitterHandler;
import com.finderfeed.fdlib.systems.particle.particle_emitter.ParticleEmitterPacket;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.Arrays;
import java.util.List;

public class FDLibCalls {

    public static final TargetingConditions ALL = TargetingConditions.forNonCombat().selector(p->true).ignoreLineOfSight().ignoreInvisibilityTesting();



    public static <T> T getListValueSafe(int id,List<T> list){

        if (list.isEmpty()){
            return null;
        }

        if (id < 0 || id >= list.size()){
            return null;
        }

        return list.get(id);
    }
    public static <T> T getListValueOrBoundaries(int id,List<T> list){

        if (list.isEmpty()){
            return null;
        }

        if (id < 0){
            return list.getFirst();
        }else if (id >= list.size()){
            return list.getLast();
        }

        return list.get(id);
    }

    public static void addParticleEmitter(Level level, double sendRadius, ParticleEmitterData data){
        if (level instanceof ServerLevel serverLevel) {
            PacketDistributor.sendToPlayersNear(serverLevel, null, data.position.x, data.position.y, data.position.z, sendRadius, new ParticleEmitterPacket(data));
        }else{
            ParticleEmitterHandler.addParticleEmitter(data);
        }
    }

    public static void addParticleEmitter(ServerPlayer player,ParticleEmitterData data){
        PacketDistributor.sendToPlayer(player, new ParticleEmitterPacket(data));
    }

    public static void sendImpactFrames(ServerLevel level, Vec3 point,float sendRadius, ImpactFrame... frames){
        PacketDistributor.sendToPlayersNear(level,null,point.x,point.y,point.z,sendRadius,new ImpactFramesPacket(Arrays.stream(frames).toList()));
    }

    public static void setServerPlayerSpeed(ServerPlayer player,Vec3 deltaMovement){
        player.setDeltaMovement(deltaMovement);
        PacketDistributor.sendToPlayer(player,new PlayerMovePacket(deltaMovement));
    }


    public static void sendParticles(ServerLevel level, ParticleOptions options, Vec3 pos, double radius){
        for (Player player : level.getNearbyPlayers(ALL,null,new AABB(
                pos.add(-radius,-radius,-radius),
                pos.add(radius,radius,radius)
        ))) {
            ((ServerLevel) level).sendParticles((ServerPlayer) player, options, true, pos.x, pos.y ,pos.z, 1, 0, 0, 0, 0);
        }
    }

    public static void sendParticles(ServerLevel level, ParticleOptions options, Vec3 pos, double radius,int amount,double xd,double yd,double zd,double speed){
        for (Player player : level.getNearbyPlayers(ALL,null,new AABB(
                pos.add(-radius,-radius,-radius),
                pos.add(radius,radius,radius)
        ))) {
            ((ServerLevel) level).sendParticles((ServerPlayer) player, options, true, pos.x, pos.y ,pos.z, amount, xd,yd,zd,speed);
        }
    }

}
