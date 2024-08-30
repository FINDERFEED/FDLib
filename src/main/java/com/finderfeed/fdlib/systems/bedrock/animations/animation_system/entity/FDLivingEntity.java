package com.finderfeed.fdlib.systems.bedrock.animations.animation_system.entity;

import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.AnimatedObject;
import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.AnimationSystem;
import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.entity.packets.SyncEntityAnimationsPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.ArrayList;
import java.util.List;

public abstract class FDLivingEntity extends LivingEntity implements AnimatedObject {

    private static final List<ItemStack> DUMMY_LIST = List.of();

    public AnimationSystem animationSystem;

    protected FDLivingEntity(EntityType<? extends LivingEntity> type, Level level) {
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


    @Override
    public Iterable<ItemStack> getArmorSlots() {
        return DUMMY_LIST;
    }

    @Override
    public ItemStack getItemBySlot(EquipmentSlot p_21127_) {
        return ItemStack.EMPTY;
    }

    @Override
    public void setItemSlot(EquipmentSlot p_21036_, ItemStack p_21037_) {

    }

    @Override
    public HumanoidArm getMainArm() {
        return HumanoidArm.LEFT;
    }

}