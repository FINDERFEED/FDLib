package com.finderfeed.fdlib;

import com.finderfeed.fdlib.network.FDPacketHandler;
import com.finderfeed.fdlib.network.lib_packets.PlayerMovePacket;
import com.finderfeed.fdlib.systems.cutscenes.CutsceneData;
import com.finderfeed.fdlib.systems.cutscenes.packets.MoveCutsceneCameraPacket;
import com.finderfeed.fdlib.systems.cutscenes.packets.StartCutscenePacket;
import com.finderfeed.fdlib.systems.cutscenes.packets.StopCutscenePacket;
import com.finderfeed.fdlib.systems.impact_frames.ImpactFrame;
import com.finderfeed.fdlib.systems.impact_frames.ImpactFramesPacket;
import com.finderfeed.fdlib.systems.particle.particle_emitter.ParticleEmitterData;
import com.finderfeed.fdlib.systems.particle.particle_emitter.ParticleEmitterHandler;
import com.finderfeed.fdlib.systems.particle.particle_emitter.ParticleEmitterPacket;
import com.finderfeed.fdlib.systems.screen.screen_effect.ScreenEffect;
import com.finderfeed.fdlib.systems.screen.screen_effect.ScreenEffectData;
import com.finderfeed.fdlib.systems.screen.screen_effect.ScreenEffectType;
import com.finderfeed.fdlib.systems.screen.screen_effect.SendScreenEffectPacket;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.PacketDistributor;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

public class FDLibCalls {

    public static final TargetingConditions ALL = TargetingConditions.forNonCombat().selector(p->true).ignoreLineOfSight().ignoreInvisibilityTesting();

    public static void startCutsceneForPlayer(ServerPlayer player, CutsceneData data){
        FDPacketHandler.INSTANCE.sendTo(new StartCutscenePacket(data), player.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
    }

    public static void startCutsceneForPlayers(ServerLevel level,Vec3 pos,double radius, CutsceneData data){
        FDPacketHandler.INSTANCE.send(PacketDistributor.NEAR.with(()->new PacketDistributor.TargetPoint(pos.x,pos.y,pos.z,radius,level.dimension())),new StartCutscenePacket(data));
    }

    public static void moveCutsceneCameraForPlayer(ServerPlayer player, CutsceneData data){
        FDPacketHandler.INSTANCE.sendTo(new MoveCutsceneCameraPacket(data), player.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
    }

    public static void moveCutsceneCameraForPlayers(ServerLevel level,Vec3 pos,double radius, CutsceneData data){
        FDPacketHandler.INSTANCE.send(PacketDistributor.NEAR.with(()->new PacketDistributor.TargetPoint(pos.x,pos.y,pos.z,radius,level.dimension())),new MoveCutsceneCameraPacket(data));
    }

    public static void stopCutsceneForPlayer(ServerPlayer player){
        FDPacketHandler.INSTANCE.sendTo(new StopCutscenePacket(), player.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
    }

    public static void stopCutsceneForPlayers(ServerLevel level,Vec3 pos,double radius){
        FDPacketHandler.INSTANCE.send(PacketDistributor.NEAR.with(()->new PacketDistributor.TargetPoint(pos.x,pos.y,pos.z,radius,level.dimension())),new StopCutscenePacket());
    }

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
            return list.get(0);
        }else if (id >= list.size()){
            return list.get(list.size() - 1);
        }

        return list.get(id);
    }

    public static void addParticleEmitter(Level level, double sendRadius, ParticleEmitterData data){
        if (level instanceof ServerLevel serverLevel) {
            FDPacketHandler.INSTANCE.send(PacketDistributor.NEAR.with(()->new PacketDistributor.TargetPoint(data.position.x, data.position.y, data.position.z, sendRadius,level.dimension())),new ParticleEmitterPacket(data));
        }else{
            ParticleEmitterHandler.addParticleEmitter(data);
        }
    }

    public static void addParticleEmitter(ServerPlayer player,ParticleEmitterData data){
        FDPacketHandler.INSTANCE.sendTo(new ParticleEmitterPacket(data), player.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
    }

    public static void sendImpactFrames(ServerLevel level, Vec3 point,float sendRadius, ImpactFrame... frames){
        FDPacketHandler.INSTANCE.send(PacketDistributor.NEAR.with(()->new PacketDistributor.TargetPoint(point.x,point.y,point.z,sendRadius,level.dimension())),new ImpactFramesPacket(Arrays.stream(frames).toList()));
    }

    public static void setServerPlayerSpeed(ServerPlayer player,Vec3 deltaMovement){
        player.setDeltaMovement(deltaMovement);
        FDPacketHandler.INSTANCE.sendTo(new PlayerMovePacket(deltaMovement), player.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
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

    public static <D extends ScreenEffectData, T extends ScreenEffect<D>> void sendScreenEffect(ServerPlayer player, ScreenEffectType<D,T> type, D data, int inTime, int stayTime, int outTime){
        FDPacketHandler.INSTANCE.sendTo(new SendScreenEffectPacket<>(data, type, inTime, stayTime, outTime), player.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
    }

    public static <D extends ScreenEffectData, T extends ScreenEffect<D>> void sendScreenEffect(ServerPlayer player, Supplier<ScreenEffectType<D, T>> type, D data, int inTime, int stayTime, int outTime){
        FDPacketHandler.INSTANCE.sendTo(new SendScreenEffectPacket<>(data, type.get(), inTime, stayTime, outTime), player.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
    }

}
