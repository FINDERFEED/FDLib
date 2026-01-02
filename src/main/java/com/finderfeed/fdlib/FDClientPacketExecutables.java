package com.finderfeed.fdlib;

import com.finderfeed.fdlib.systems.bedrock.animations.Animation;
import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.AnimatedObject;
import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.AnimationSystem;
import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.AnimationTicker;
import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.TickerSyncInstance;
import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.item.FDItemAnimationHandler;
import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.model_system.ModelSystem;
import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.model_system.attachments.ModelAttachmentData;
import com.finderfeed.fdlib.systems.bedrock.models.FDModelInfo;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.UUID;

public class FDClientPacketExecutables {

    public static void startItemAnimationInHand(int entityId, AnimationTicker animation, InteractionHand hand, String layer){
        if (FDClientHelpers.getClientLevel().getEntity(entityId) instanceof LivingEntity livingEntity){
            ItemStack itemStack = livingEntity.getItemInHand(hand);
            var animSystem = FDItemAnimationHandler.getItemAnimationSystem(itemStack);
            animSystem.startAnimation(layer, animation);
        }
    }

    public static void addEntityAttachmentPacket(int entityId, int layer, String bone, UUID uuid, ModelAttachmentData<?> modelInfo){
        if (FDClientHelpers.getClientLevel().getEntity(entityId) instanceof AnimatedObject animatedObject){
            animatedObject.getModelSystem().attachToLayer(layer,bone, uuid, modelInfo);
        }
    }

    public static void removeEntityAttachment(int entityId, UUID uuid){
        if (FDClientHelpers.getClientLevel().getEntity(entityId) instanceof AnimatedObject animatedObject){
            animatedObject.getModelSystem().removeAttachment(uuid);
        }
    }

    public static void syncEntityAttachmentsPacket(int entityId, CompoundTag data){
        if (FDClientHelpers.getClientLevel().getEntity(entityId) instanceof AnimatedObject animatedObject){
            ModelSystem modelSystem = animatedObject.getModelSystem();
            modelSystem.loadAttachments(FDClientHelpers.getClientLevel().registryAccess(), data);
        }
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
            AnimationSystem system = object.getAnimationSystem();
            system.startAnimation(tickerName,ticker);
        }
    }

    public static void entityStopAnimationPacket(int entityId, String tickerName){
        Entity entity = Minecraft.getInstance().level.getEntity(entityId);
        if (entity instanceof AnimatedObject object){
            AnimationSystem system = object.getAnimationSystem();
            system.stopAnimation(tickerName);
        }
    }

    public static void entitySyncAnimationsPacket(int entityId, List<TickerSyncInstance> syncInstances){
        Entity entity = Minecraft.getInstance().level.getEntity(entityId);
        if (entity instanceof AnimatedObject object){
            AnimationSystem system = object.getAnimationSystem();
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
        object.getAnimationSystem().setFrozen(state);
    }
    public static void tileEntityStartAnimation(BlockPos pos,AnimationTicker ticker,String layer){
        Level level = Minecraft.getInstance().level;
        if (!((level.getBlockEntity(pos)) instanceof AnimatedObject object)) return;
        object.getAnimationSystem().startAnimation(layer,ticker);
    }

    public static void tileEntityStopAnimation(BlockPos pos,String layer){
        Level level = Minecraft.getInstance().level;
        if (!((level.getBlockEntity(pos)) instanceof AnimatedObject object)) return;
        object.getAnimationSystem().stopAnimation(layer);
    }

    public static void tileEntitySyncAnimations(BlockPos pos,List<TickerSyncInstance> syncInstances){
        Level level = Minecraft.getInstance().level;
        if (!((level.getBlockEntity(pos)) instanceof AnimatedObject object)) return;
        AnimationSystem system = object.getAnimationSystem();
        var map = system.getTickers();
        map.clear();
        for (var inst : syncInstances){
            map.put(inst.tickerName(),inst.ticker());
        }
    }

    public static void entityFreezeAnimations(int entityId,boolean state){
        Entity entity = Minecraft.getInstance().level.getEntity(entityId);
        if (entity instanceof AnimatedObject object){
            AnimationSystem system = object.getAnimationSystem();
            system.setFrozen(state);
        }
    }


}
