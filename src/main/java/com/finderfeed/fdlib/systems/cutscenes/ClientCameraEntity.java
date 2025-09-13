package com.finderfeed.fdlib.systems.cutscenes;

import com.finderfeed.fdlib.FDClientHelpers;
import com.finderfeed.fdlib.init.FDEntities;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class ClientCameraEntity extends LivingEntity {

    public ClientCameraEntity(EntityType<? extends LivingEntity> p_19870_, Level p_19871_) {
        super(p_19870_, p_19871_);
    }


    public ClientCameraEntity(Level level){
        this(FDEntities.CLIENT_CAMERA.get(),level);
    }

    @Override
    public void tick() {
        super.tick();

    }

    @Override
    public HumanoidArm getMainArm() {
        return HumanoidArm.LEFT;
    }

    @Override
    public void setPos(double p_20210_, double p_20211_, double p_20212_) {
        super.setPos(p_20210_, p_20211_, p_20212_);
    }

    @Override
    public boolean hasEffect(MobEffect effect) {
        return FDClientHelpers.getClientPlayer().hasEffect(effect);
    }


    @Nullable
    @Override
    public MobEffectInstance getEffect(MobEffect effect) {
        return FDClientHelpers.getClientPlayer().getEffect(effect);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag p_20052_) {

    }

    @Override
    public Iterable<ItemStack> getArmorSlots() {
        return new ArrayList<>();
    }

    @Override
    public ItemStack getItemBySlot(EquipmentSlot p_21127_) {
        return ItemStack.EMPTY;
    }

    @Override
    public void setItemSlot(EquipmentSlot p_21036_, ItemStack p_21037_) {

    }

    @Override
    public void addAdditionalSaveData(CompoundTag p_20139_) {

    }

    @Override
    public void push(Entity p_21294_) {

    }

    @Override
    public void push(double p_20286_, double p_20287_, double p_20288_) {

    }

    @Override
    protected void pushEntities() {

    }


}
