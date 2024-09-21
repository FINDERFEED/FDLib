package com.finderfeed.fdlib.to_other_mod.projectiles;

import com.finderfeed.fdlib.init.FDEDataSerializers;
import com.finderfeed.fdlib.util.FDProjectile;
import com.finderfeed.fdlib.util.math.FDMathUtil;
import net.minecraft.core.particles.ParticleTypes;
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

public class BlockProjectile extends FDProjectile {

    public static final EntityDataAccessor<Float> ROTATION_SPEED = SynchedEntityData.defineId(BlockProjectile.class, EntityDataSerializers.FLOAT);
    public static final EntityDataAccessor<BlockState> STATE = SynchedEntityData.defineId(BlockProjectile.class, EntityDataSerializers.BLOCK_STATE);

    public Vec3 previousDirection = new Vec3(0,1,0);
    public Vec3 direction = new Vec3(0,1,0);

    public Quaternionf currentRotation = new Quaternionf(new AxisAngle4f(0,0,1,0));

    public BlockProjectile(EntityType<? extends AbstractHurtingProjectile> type, Level level) {
        super(type, level);
    }

    @Override
    public void tick() {
        super.tick();

        if (level().isClientSide){
            previousDirection = this.getLookDirection();
            Vec3 oldPos = new Vec3(xo,yo,zo);
            Vec3 currentPos = this.position();
            Vec3 b = currentPos.subtract(oldPos);
            double len = b.length();
            if (len > 0.01){

                var v = currentRotation.transform(0,1,0,new Vector3f());
                Vec3 v3 = FDMathUtil.vector3fToVec3(v);
                float currentAngleBetween = (float) FDMathUtil.angleBetweenVectors(b,new Vec3(0,1,0));

                Vec3 axis = b.cross(new Vec3(0,1,0)).normalize();

                Vec3 ppos = this.position().add(0,0.5,0).add(axis.multiply(3,3,3));
                Vec3 ppos2 = this.position().add(0,0.5,0).add(v3.multiply(3,3,3));
                Vec3 ppos3 = this.position().add(0,0.5,0).add(b.normalize().multiply(3,3,3));
                level().addParticle(ParticleTypes.FLAME,ppos.x,ppos.y,ppos.z,0,0,0);
                level().addParticle(ParticleTypes.CAMPFIRE_COSY_SMOKE,ppos2.x,ppos2.y,ppos2.z,0,0,0);
                level().addParticle(ParticleTypes.BUBBLE,ppos3.x,ppos3.y,ppos3.z,0,0,0);

                if (currentAngleBetween > 0.05){

                    Quaternionf targetQuaternion = new Quaternionf(
                            new AxisAngle4f(-currentAngleBetween,
                                    (float)axis.x,
                                    (float)axis.y,
                                    (float)axis.z
                            )
                    );
//                    currentRotation = targetQuaternion;
                    currentRotation.slerp(targetQuaternion,0.1f);

//                    currentRotation.mul(new Quaternionf(
//                            new AxisAngle4f(
//                                    Mth.clamp(rotationAngle,0,currentAngleBetween),
//                                    (float)axis.x,
//                                    (float)axis.y,
//                                    (float)axis.z
//                            )
//                    ));
//                    currentRotation.rotateAxis(Mth.clamp(rotationAngle,0,currentAngleBetween),
//                            (float)axis.x,
//                            (float)axis.y,
//                            (float)axis.z
//                    );
                }

            }
        }else{
            if (tickCount >= 20000){
                this.remove(RemovalReason.DISCARDED);
            }
        }

    }


    @Override
    public boolean deflect(ProjectileDeflection p_341900_, @Nullable Entity p_341912_, @Nullable Entity p_341932_, boolean p_341948_) {
        if (!this.level().isClientSide) {

            ProjectileDeflection deflection = (projectile,entity,randomSource)->{
                projectile.setDeltaMovement(entity.getLookAngle().multiply(0.1,0.1,0.1));
                projectile.hasImpulse = true;
            };

            deflection.deflect(this, p_341912_, this.random);
            this.setOwner(p_341932_);
//            this.onDeflection(p_341912_, p_341948_);
        }

        return true;
    }

    public float getRotationSpeed() {
        return this.entityData.get(ROTATION_SPEED);
    }

    public void setRotationSpeed(float rotationSpeed) {
        this.entityData.set(ROTATION_SPEED,rotationSpeed);
    }

    public void setLookDirection(Vec3 direction){
        this.direction = direction;
    }

    public Vec3 getLookDirection(){
        return this.direction;
    }

    public BlockState getBlockState(){
        return this.entityData.get(STATE);
    }

    public void setBlockState(BlockState state){
        this.entityData.set(STATE,state);
    }


    @Override
    public boolean hurt(DamageSource p_341896_, float p_341906_) {
        return super.hurt(p_341896_, p_341906_);
    }

    @Override
    protected void onHitBlock(BlockHitResult blockHitResult) {
        super.onHitBlock(blockHitResult);
        this.remove(RemovalReason.DISCARDED);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder
                .define(ROTATION_SPEED,5f)
                .define(STATE, Blocks.STONE.defaultBlockState());
    }


    @Override
    public boolean save(CompoundTag tag) {
        tag.putFloat("rotationSpeed",this.getRotationSpeed());
        tag.put("state", NbtUtils.writeBlockState(this.getBlockState()));
        return super.save(tag);
    }

    @Override
    public void load(CompoundTag load) {
        this.setRotationSpeed(load.getFloat("rotationSpeed"));
        this.setBlockState(NbtUtils.readBlockState(level().holderLookup(Registries.BLOCK),load.getCompound("state")));
        super.load(load);
    }
}
