package com.finderfeed.fdlib.to_other_mod.earthshatter_entity;

import com.finderfeed.fdlib.init.FDEDataSerializers;
import com.finderfeed.fdlib.to_other_mod.FDEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class EarthShatterEntity extends Entity {

    public static final EntityDataAccessor<Vec3> DIRECTION = SynchedEntityData.defineId(EarthShatterEntity.class, FDEDataSerializers.VEC3.get());
    public static final EntityDataAccessor<BlockState> STATE = SynchedEntityData.defineId(EarthShatterEntity.class, EntityDataSerializers.BLOCK_STATE);

    private int lifetime = 0;

    public EarthShatterEntity(EntityType<?> type, Level level) {
        super(type, level);
    }

    public static EarthShatterEntity summon(Level level, BlockPos pos,Vec3 shatterDirection,int lifetime){
        BlockState state = level.getBlockState(pos);
        if (state.isAir()) return null;
        EarthShatterEntity entity = new EarthShatterEntity(FDEntities.EARTH_SHATTER.get(),level);

        entity.setPos(pos.getX(),pos.getY(),pos.getZ());
        entity.setDirection(shatterDirection);
        entity.setBlockState(state);
        entity.lifetime = lifetime;

        level.addFreshEntity(entity);

        return entity;
    }

    @Override
    public void tick() {
        super.tick();
        if (!level().isClientSide){
            if (this.tickCount > lifetime){
                this.remove(RemovalReason.DISCARDED);
            }
        }
    }

    public BlockState getBlockState(){
        return this.entityData.get(STATE);
    }

    public void setBlockState(BlockState state){
        this.entityData.set(STATE,state);
    }

    public Vec3 getShatterDirection(){
        return this.entityData.get(DIRECTION);
    }

    public void setDirection(Vec3 v){
        this.entityData.set(DIRECTION,v);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(DIRECTION,new Vec3(0,1,0))
                .define(STATE, Blocks.STONE.defaultBlockState());
    }


    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {

    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {

    }
}
