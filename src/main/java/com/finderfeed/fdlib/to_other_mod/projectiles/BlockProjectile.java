package com.finderfeed.fdlib.to_other_mod.projectiles;

import com.finderfeed.fdlib.to_other_mod.FDEntities;
import com.finderfeed.fdlib.to_other_mod.entities.flying_block_entity.FlyingBlockEntity;
import com.finderfeed.fdlib.util.FDProjectile;
import com.finderfeed.fdlib.util.math.FDMathUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.entity.projectile.ProjectileDeflection;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.joml.*;

import java.lang.Math;
import java.util.ArrayList;
import java.util.List;

public class BlockProjectile extends FDProjectile {

    public static final EntityDataAccessor<Float> ROTATION_SPEED = SynchedEntityData.defineId(BlockProjectile.class, EntityDataSerializers.FLOAT);
    public static final EntityDataAccessor<BlockState> STATE = SynchedEntityData.defineId(BlockProjectile.class, EntityDataSerializers.BLOCK_STATE);

    public Quaternionf currentRotation = new Quaternionf(new AxisAngle4f(0,0.01f,1,0));
    public Quaternionf previousRotation = new Quaternionf(new AxisAngle4f(0,0.01f,1,0));

    public BlockProjectile(EntityType<? extends AbstractHurtingProjectile> type, Level level) {
        super(type, level);
    }

    @Override
    public void tick() {
        super.tick();

        if (level().isClientSide){
           this.handleRotation();
        }else{
            if (tickCount >= 2000){
                this.remove(RemovalReason.DISCARDED);
            }
        }

    }

    private void handleRotation(){
        Vec3 oldPos = new Vec3(xo,yo,zo);
        Vec3 currentPos = this.position();
        Vec3 b = currentPos.subtract(oldPos).add(0.01,0,0.01); // for some ducking reason it messes up when going straight down, so
        double len = b.length();
        if (len > 0.01){

            var v = currentRotation.transform(0,1,0,new Vector3f());
            Vec3 v3 = FDMathUtil.vector3fToVec3(v);
            float currentAngleBetween = (float) FDMathUtil.angleBetweenVectors(b,v3);

            Vec3 axis = b.cross(new Vec3(0,1,0)).normalize();
            previousRotation = new Quaternionf(currentRotation);
            if (currentAngleBetween > 0.1){
                float bt = (float) FDMathUtil.angleBetweenVectors(b,new Vec3(0,1,0));
                Quaternionf targetQuaternion = new Quaternionf(
                        new AxisAngle4f(-bt,
                                (float)axis.x,
                                (float)axis.y,
                                (float)axis.z
                        )
                );
                float rot = (float) Math.toRadians(this.getRotationSpeed() * len);
                currentRotation.slerp(targetQuaternion,Mth.clamp(rot / currentAngleBetween,0,1));
            }

        }
    }



    public float getRotationSpeed() {
        return this.entityData.get(ROTATION_SPEED);
    }

    public void setRotationSpeed(float rotationSpeed) {
        this.entityData.set(ROTATION_SPEED,rotationSpeed);
    }

    public BlockState getBlockState(){
        return this.entityData.get(STATE);
    }

    public void setBlockState(BlockState state){
        this.entityData.set(STATE,state);
    }


    @Override
    protected void onHitBlock(BlockHitResult blockHitResult) {
        super.onHitBlock(blockHitResult);
        if (!level().isClientSide){

            float baseSpeed = 2;
            Vec3 base = this.getDeltaMovement().multiply(1,0,1).normalize()
                    .multiply(baseSpeed,0,baseSpeed);

            float rotationLimit = FDMathUtil.FPI / 6;
            int count = 10 + random.nextInt(5);


            Vec3 center = blockHitResult.getBlockPos().getCenter().add(0,0.5,0);

            var states = this.collectStates(blockHitResult.getBlockPos(),2);

            for (int i = 0; i < count;i++){
                FlyingBlockEntity blockEntity = new FlyingBlockEntity(FDEntities.FLYING_BLOCK.get(),level());

                float randRotation = rotationLimit * (random.nextFloat() * 2 - 1);

                float md = Math.abs(randRotation) / rotationLimit;

                float randY = random.nextFloat() * 0.5f  * md + 0.5f;

                float speedMd = random.nextFloat() / 0.8f + 0.2f;

                Vec3 f = base.multiply(
                        speedMd,0,speedMd
                ).yRot(randRotation).add(0,randY,0);

                var state = states.get(random.nextInt(states.size()));

                blockEntity.setAirFriction(0.935f);
                blockEntity.setPos(center);
                blockEntity.setNoPhysicsTime(2);
                blockEntity.setBlockState(state);
                blockEntity.setRotationSpeed((float)f.length() / 2 * 20f);
                blockEntity.setDeltaMovement(f);

                level().addFreshEntity(blockEntity);
            }
            this.remove(RemovalReason.DISCARDED);
        }
    }

    private List<BlockState> collectStates(BlockPos baseImpactPos, int blockRadius){
        List<BlockState> states = new ArrayList<>();
        for (int x = -blockRadius;x <= blockRadius;x++){
            for (int y = -blockRadius;y <= blockRadius;y++){
                for (int z = -blockRadius;z <= blockRadius;z++){
                    BlockPos takePos = baseImpactPos.offset(x,y,z);
                    var state = level().getBlockState(takePos);
                    if (!state.isAir() && !states.contains(state)){
                        states.add(state);
                    }
                }
            }
        }
        return states;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder
                .define(ROTATION_SPEED,20f)
                .define(STATE, Blocks.STONE.defaultBlockState());
    }


    @Override
    public boolean save(CompoundTag tag) {
        tag.putFloat("rotationSpeed",this.getRotationSpeed());
        tag.put("state", NbtUtils.writeBlockState(this.getBlockState()));
        return super.save(tag);
    }

    @Override
    public void load(CompoundTag tag) {
        this.setRotationSpeed(tag.getFloat("rotationSpeed"));
        this.setBlockState(NbtUtils.readBlockState(level().holderLookup(Registries.BLOCK),tag.getCompound("state")));
        super.load(tag);
    }
}
