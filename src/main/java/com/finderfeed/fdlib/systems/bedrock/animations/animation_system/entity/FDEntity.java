package com.finderfeed.fdlib.systems.bedrock.animations.animation_system.entity;

import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.AnimatedObject;
import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.AnimationSystem;
import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.entity.packets.SyncEntityAnimationsPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.PacketDistributor;

public abstract class FDEntity extends Entity implements AnimatedObject {

    public AnimationSystem animationSystem;

    public FDEntity(EntityType<?> type, Level level) {
        super(type, level);
        this.animationSystem = EntityAnimationSystem.create(this);
    }

    @Override
    public void tick() {
        super.tick();
        this.tickAnimationSystem();
    }

    @Override
    public AnimationSystem getSystem() {
        return animationSystem;
    }


    @Override
    public void startSeenByPlayer(ServerPlayer player) {
        super.startSeenByPlayer(player);
        SyncEntityAnimationsPacket packet = new SyncEntityAnimationsPacket(this.getId(),this.getSystem().getTickers());
        PacketDistributor.sendToPlayer(player,packet);
    }

}
