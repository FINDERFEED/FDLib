package com.finderfeed.fdlib;

import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.AnimatedObject;
import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.AnimationSystem;
import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.AnimationTicker;
import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.TickerSyncInstance;
import com.finderfeed.fdlib.systems.cutscenes.CutsceneCameraHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class FDClientPacketExecutables {

    public static void testCameraPacket(){
//        CutsceneCameraHandler.initiateCamera();
    }

    public static void movePlayer(Vec3 movement){
        Player player = Minecraft.getInstance().player;
        player.setDeltaMovement(movement);
    }

    public static void playsoundInEars(SoundEvent event,float pitch,float volume){
        Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(event,pitch,volume));
    }

    public static void entityStartAnimationPacket(int entityId, String tickerName, AnimationTicker ticker){
        Entity entity = Minecraft.getInstance().level.getEntity(entityId);
        if (entity instanceof AnimatedObject object){
            AnimationSystem system = object.getSystem();
            system.startAnimation(tickerName,ticker);
        }
    }

    public static void entityStopAnimationPacket(int entityId, String tickerName){
        Entity entity = Minecraft.getInstance().level.getEntity(entityId);
        if (entity instanceof AnimatedObject object){
            AnimationSystem system = object.getSystem();
            system.stopAnimation(tickerName);
        }
    }

    public static void entitySyncAnimationsPacket(int entityId, List<TickerSyncInstance> syncInstances){
        Entity entity = Minecraft.getInstance().level.getEntity(entityId);
        if (entity instanceof AnimatedObject object){
            AnimationSystem system = object.getSystem();
            var map = system.getTickers();
            map.clear();
            for (var inst : syncInstances){
                map.put(inst.tickerName(),inst.ticker());
            }
        }
    }

    public static void tileEntityFreezeAnimations(BlockPos pos,boolean state){
        Level level = Minecraft.getInstance().level;
        if (!((level.getBlockEntity(pos)) instanceof AnimatedObject object)) return;
        object.getSystem().setFrozen(state);
    }
    public static void tileEntityStartAnimation(BlockPos pos,AnimationTicker ticker,String layer){
        Level level = Minecraft.getInstance().level;
        if (!((level.getBlockEntity(pos)) instanceof AnimatedObject object)) return;
        object.getSystem().startAnimation(layer,ticker);
    }

    public static void tileEntityStopAnimation(BlockPos pos,String layer){
        Level level = Minecraft.getInstance().level;
        if (!((level.getBlockEntity(pos)) instanceof AnimatedObject object)) return;
        object.getSystem().stopAnimation(layer);
    }

    public static void tileEntitySyncAnimations(BlockPos pos,List<TickerSyncInstance> syncInstances){
        Level level = Minecraft.getInstance().level;
        if (!((level.getBlockEntity(pos)) instanceof AnimatedObject object)) return;
        AnimationSystem system = object.getSystem();
        var map = system.getTickers();
        map.clear();
        for (var inst : syncInstances){
            map.put(inst.tickerName(),inst.ticker());
        }
    }

    public static void entityFreezeAnimations(int entityId,boolean state){
        Entity entity = Minecraft.getInstance().level.getEntity(entityId);
        if (entity instanceof AnimatedObject object){
            AnimationSystem system = object.getSystem();
            system.setFrozen(state);
        }
    }


}
