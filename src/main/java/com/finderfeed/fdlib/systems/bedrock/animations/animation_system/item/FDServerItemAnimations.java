package com.finderfeed.fdlib.systems.bedrock.animations.animation_system.item;

import com.finderfeed.fdlib.systems.bedrock.animations.Animation;
import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.AnimationTicker;
import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.item.packets.StartItemAnimationInHandPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.network.PacketDistributor;

public class FDServerItemAnimations {

    public static void startItemAnimation(LivingEntity entity, String layer, AnimationTicker animation, InteractionHand hand){
        PacketDistributor.sendToPlayersTrackingEntity(entity, new StartItemAnimationInHandPacket(layer, entity, animation, hand));
        if (entity instanceof ServerPlayer serverPlayer){
            PacketDistributor.sendToPlayer(serverPlayer, new StartItemAnimationInHandPacket(layer, entity, animation, hand));
        }
    }

}
