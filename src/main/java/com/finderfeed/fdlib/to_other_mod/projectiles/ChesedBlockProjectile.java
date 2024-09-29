package com.finderfeed.fdlib.to_other_mod.projectiles;

import com.finderfeed.fdlib.to_other_mod.FDEntities;
import com.finderfeed.fdlib.to_other_mod.entities.earthshatter_entity.EarthShatterEntity;
import com.finderfeed.fdlib.to_other_mod.entities.earthshatter_entity.EarthShatterSettings;
import com.finderfeed.fdlib.to_other_mod.entities.flying_block_entity.FlyingBlockEntity;
import com.finderfeed.fdlib.to_other_mod.packets.SlamParticlesPacket;
import com.finderfeed.fdlib.util.ProjectileMovementPath;
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
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.PacketDistributor;
import org.joml.*;

import java.lang.Math;
import java.util.ArrayList;
import java.util.List;

public class ChesedBlockProjectile extends FDProjectile {

    public static final EntityDataAccessor<Float> ROTATION_SPEED = SynchedEntityData.defineId(ChesedBlockProjectile.class, EntityDataSerializers.FLOAT);
    public static final EntityDataAccessor<BlockState> STATE = SynchedEntityData.defineId(ChesedBlockProjectile.class, EntityDataSerializers.BLOCK_STATE);

    public ProjectileMovementPath movementPath = null;

    public Quaternionf currentRotation = new Quaternionf(new AxisAngle4f(0,0.01f,1,0));
    public Quaternionf previousRotation = new Quaternionf(new AxisAngle4f(0,0.01f,1,0));

    public ChesedBlockProjectile(EntityType<? extends AbstractHurtingProjectile> type, Level level) {
        super(type, level);
    }

    @Override
    public void tick() {
        super.tick();

        if (level().isClientSide){
           this.handleRotation();
        }else{

            if (movementPath != null){
                movementPath.tick(this);
                if (movementPath.isFinished()){
                    movementPath = movementPath.getNext();
                }
            }

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
        if (!level().isClientSide && !this.noPhysics){
            SlamParticlesPacket packet = new SlamParticlesPacket(
                    new SlamParticlesPacket.SlamData(blockHitResult.getBlockPos(),blockHitResult.getLocation(),this.getDeltaMovement())
            );
            PacketDistributor.sendToPlayersTrackingEntity(this,packet);
            this.shatter(blockHitResult,FDMathUtil.FPI / 6);
            this.launchBlocks(blockHitResult,FDMathUtil.FPI / 6);
            this.remove(RemovalReason.DISCARDED);
        }
    }


    private void shatter(BlockHitResult result,float rotationLimit){
        Vec3 direction = this.getDeltaMovement().multiply(1,0,1).normalize();
        BlockPos hitPos = result.getBlockPos();
        int maxDist = 7;

        Vec3 hitCenter = hitPos.getCenter();

        for (int x = -maxDist;x <= maxDist;x++){
            for (int z = -maxDist;z <= maxDist;z++){
                BlockPos p = hitPos.offset(x,0,z);
                Vec3 center = p.getCenter();
                Vec3 b = center.subtract(hitCenter).multiply(1,0,1);
                double len = b.length();
                if (len > maxDist) continue;

                double diff = FDMathUtil.angleBetweenVectors(direction,b);
                if (diff <= rotationLimit){

                    EarthShatterEntity shatter = EarthShatterEntity.summon(level(),p, EarthShatterSettings.builder()
                                    .direction(b.normalize().reverse().add(0,0.5,0))
                                    .delay((int)Math.round(b.length()))
                                    .upDistance(0.1f + (float)b.length() / maxDist)
                                    .upTime(1)
                                    .stayTime(2)
                                    .downTime(3)
                            .build());
                }
            }
        }
    }

    private void launchBlocks(BlockHitResult blockHitResult,float rotationLimit){
        float baseSpeed = 2.4f;
        Vec3 base = this.getDeltaMovement().multiply(1,0,1).normalize()
                .multiply(baseSpeed,0,baseSpeed);

        int count = 3 + random.nextInt(3);


        Vec3 center = blockHitResult.getBlockPos().getCenter().add(0,0.5,0);

        var states = this.collectStates(blockHitResult.getBlockPos(),2);

        float angle = rotationLimit * 2 / count;

        for (int i = 0; i <= count;i++){

            float ang = i * angle - rotationLimit + random.nextFloat() * (angle / 2) - (angle / 4);

            float md = 1 - Math.abs(ang) / rotationLimit;

            float randY = random.nextFloat() * 0.05f * md + 0.32f;

            float speedMd = 1f + (random.nextFloat() * 0.25f + md * 0.75f);

            Vec3 f = base.multiply(
                    speedMd,0,speedMd
            ).yRot(ang).add(0,randY,0);

            var state = states.get(random.nextInt(states.size()));

            FlyingBlockEntity blockEntity = new FlyingBlockEntity(FDEntities.FLYING_BLOCK.get(),level());
            blockEntity.setAirFriction(0.7f);
            blockEntity.setPos(center);
            blockEntity.setNoPhysicsTime(2);
            blockEntity.setBlockState(state);
            blockEntity.setRotationSpeed((float)f.length() / 2 * 15f);
            blockEntity.setDeltaMovement(f);
            level().addFreshEntity(blockEntity);
            for (int g = 0;g < md * 2;g++){
                float mod = 0.6f + random.nextFloat() * 0.4f - 0.2f;
                f = base.multiply(
                        speedMd,0,speedMd
                ).yRot(ang + random.nextFloat() * (angle / 2) - (angle / 4)).add(0,randY,0).multiply(mod,mod,mod);
                blockEntity = new FlyingBlockEntity(FDEntities.FLYING_BLOCK.get(),level());
                blockEntity.setAirFriction(0.72f);
                blockEntity.setPos(center);
                blockEntity.setNoPhysicsTime(2);
                blockEntity.setBlockState(state);
                blockEntity.setRotationSpeed((float)f.length() / 2 * 15f);
                blockEntity.setDeltaMovement(f);
                level().addFreshEntity(blockEntity);
            }
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
    public void lerpTo(double x, double y, double z, float p_19899_, float p_19900_, int p_19901_) {
        super.lerpTo(x, y, z, p_19899_, p_19900_, p_19901_);
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
        if (movementPath != null){
            movementPath.save("path",tag);
        }
        tag.putFloat("rotationSpeed",this.getRotationSpeed());
        tag.put("state", NbtUtils.writeBlockState(this.getBlockState()));
        return super.save(tag);
    }

    @Override
    public void load(CompoundTag tag) {
        if (tag.contains("path")){
            this.movementPath = new ProjectileMovementPath();
            this.movementPath.load("path",tag);
        }
        this.setRotationSpeed(tag.getFloat("rotationSpeed"));
        this.setBlockState(NbtUtils.readBlockState(level().holderLookup(Registries.BLOCK),tag.getCompound("state")));
        super.load(tag);
    }
}
