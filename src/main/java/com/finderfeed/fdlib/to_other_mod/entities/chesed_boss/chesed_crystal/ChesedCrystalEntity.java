package com.finderfeed.fdlib.to_other_mod.entities.chesed_boss.chesed_crystal;

import com.finderfeed.fdlib.init.FDEDataSerializers;
import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.AnimationTicker;
import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.entity.FDEntity;
import com.finderfeed.fdlib.to_other_mod.init.BossAnims;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class ChesedCrystalEntity extends FDEntity {

    public static final EntityDataAccessor<Vec3> DIRECTION = SynchedEntityData.defineId(ChesedCrystalEntity.class, FDEDataSerializers.VEC3.get());

    public ChesedCrystalEntity(EntityType<?> type, Level level) {
        super(type, level);
        this.getSystem().startAnimation("SPAWN", AnimationTicker.builder(BossAnims.CHESED_CRYSTAL_SPAWN.get())
                .setToNullTransitionTime(0)
                        .setSpeed(2)
                .build());
    }



    @Override
    public void onAddedToLevel() {
        super.onAddedToLevel();

    }

    @Override
    public void onRemovedFromLevel() {
        super.onRemovedFromLevel();

    }

    public Vec3 getCrystalFacingDirection(){
        return this.entityData.get(DIRECTION);
    }

    public void setCrystalFacingDirection(Vec3 v){
        this.entityData.set(DIRECTION,v);
    }

    @Override
    public boolean isPickable() {
        return true;
    }

    @Override
    public boolean shouldRender(double p_20296_, double p_20297_, double p_20298_) {
        return true;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder data) {
        data.define(DIRECTION,new Vec3(0,1,0));
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {

    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {

    }
}
