package com.finderfeed.fdlib.to_other_mod.entities.falling_block;

import com.finderfeed.fdlib.systems.shake.FDShakeData;
import com.finderfeed.fdlib.systems.shake.PositionedScreenShakePacket;
import com.finderfeed.fdlib.to_other_mod.BossEntities;
import com.finderfeed.fdlib.to_other_mod.BossUtil;
import com.finderfeed.fdlib.to_other_mod.entities.ChesedEntity;
import com.finderfeed.fdlib.to_other_mod.entities.flying_block_entity.FlyingBlockEntity;
import com.finderfeed.fdlib.to_other_mod.packets.SlamParticlesPacket;
import com.finderfeed.fdlib.util.FDProjectile;
import com.finderfeed.fdlib.util.ProjectileMovementPath;
import com.finderfeed.fdlib.util.math.FDMathUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.PacketDistributor;

public class ChesedFallingBlock extends FDProjectile {

    public static final EntityDataAccessor<BlockState> STATE = SynchedEntityData.defineId(ChesedFallingBlock.class, EntityDataSerializers.BLOCK_STATE);

    public ChesedFallingBlock(EntityType<? extends AbstractHurtingProjectile> type, Level level) {
        super(type, level);
    }

    public static ChesedFallingBlock summon(Level level,BlockState state,Vec3 pos){
        ChesedFallingBlock block = new ChesedFallingBlock(BossEntities.CHESED_FALLING_BLOCK.get(),level);
        block.setPos(pos);
        block.setBlockState(state);
        level.addFreshEntity(block);
        return block;
    }

    @Override
    public void tick() {
        super.tick();
        if (!level().isClientSide){
            this.applyGravity();
        }
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        super.onHitBlock(result);
        Vec3 pos = result.getLocation();
        if (!level().isClientSide){
            float rd = 1.5f;
            AABB box = new AABB(-rd,-rd,-rd,rd,rd,rd).move(pos);
            for (LivingEntity entity : level().getEntitiesOfClass(LivingEntity.class,box,pr->{return !(pr instanceof ChesedEntity);})){

                //TODO: damage

                if (entity instanceof ServerPlayer player){
                    PositionedScreenShakePacket.send((ServerLevel) level(), FDShakeData.builder()
                            .frequency(1.5f)
                            .stayTime(0)
                            .inTime(2)
                            .outTime(6)
                            .amplitude(15.f)
                            .build(),pos,5);
                }
            }

            SlamParticlesPacket packet = new SlamParticlesPacket(
                    new SlamParticlesPacket.SlamData(result.getBlockPos(),pos.add(0,0.5,0),new Vec3(1,0,0))
                            .maxAngle(FDMathUtil.FPI * 2)
                            .maxSpeed(0.15f)
                            .collectRadius(2)
                            .maxParticleLifetime(30)
                            .maxVerticalSpeedEdges(0.07f)
                            .maxVerticalSpeedCenter(0.07f)
            );
            PacketDistributor.sendToPlayersTrackingEntity(this,packet);
            this.remove(RemovalReason.DISCARDED);
        }
    }

    public BlockState getBlockState(){
        return this.entityData.get(STATE);
    }

    public void setBlockState(BlockState state){
        this.entityData.set(STATE,state);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder data) {
        data

                .define(STATE, Blocks.STONE.defaultBlockState());
    }

    @Override
    public boolean save(CompoundTag tag) {
        tag.put("state", NbtUtils.writeBlockState(this.getBlockState()));
        return super.save(tag);
    }

    @Override
    public void load(CompoundTag tag) {
        this.setBlockState(NbtUtils.readBlockState(level().holderLookup(Registries.BLOCK),tag.getCompound("state")));
        super.load(tag);
    }


    @Override
    protected double getDefaultGravity() {
        return 0.025;
    }
}
