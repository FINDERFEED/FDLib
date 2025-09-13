package com.finderfeed.fdlib.systems.bedrock.animations.animation_system.entity;

import com.finderfeed.fdlib.network.FDPacketHandler;
import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.AnimatedObject;
import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.AnimationTicker;
import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.entity.packets.SyncEntityAnimationsPacket;
import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.entity.packets.EntityStartAnimationPacket;
import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.entity.packets.EntityStopAnimationPacket;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.PacketDistributor;

public class ServersideEntityAnimationSystem<T extends Entity & AnimatedObject> extends EntityAnimationSystem<T> {
    protected ServersideEntityAnimationSystem(T entity) {
        super(entity);
    }


    public void syncAnimations(){
        FDPacketHandler.INSTANCE.send(PacketDistributor.TRACKING_ENTITY.with(this::getEntity), new SyncEntityAnimationsPacket(this.getEntity().getId(),this.getTickers()));
    }

    @Override
    public void onAnimationStart(String name, AnimationTicker ticker) {
        FDPacketHandler.INSTANCE.send(PacketDistributor.TRACKING_ENTITY.with(this::getEntity), new EntityStartAnimationPacket(this.getEntity().getId(),name,ticker));
    }

    @Override
    public void onAnimationStop(String name) {
        FDPacketHandler.INSTANCE.send(PacketDistributor.TRACKING_ENTITY.with(this::getEntity), new EntityStopAnimationPacket(this.getEntity().getId(),name));
    }

    @Override
    public void onFreeze(boolean state) {

    }

    @Override
    public void onVariableAdded(String name, float variable) {

    }
}
