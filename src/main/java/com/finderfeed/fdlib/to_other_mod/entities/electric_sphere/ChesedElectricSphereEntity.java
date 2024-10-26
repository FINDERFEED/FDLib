package com.finderfeed.fdlib.to_other_mod.entities.electric_sphere;

import com.finderfeed.fdlib.nbt.AutoSerializable;
import com.finderfeed.fdlib.nbt.SerializableField;
import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.AnimationTicker;
import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.entity.FDEntity;
import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.entity.FDLivingEntity;
import com.finderfeed.fdlib.to_other_mod.BossAnims;
import com.finderfeed.fdlib.to_other_mod.BossEntities;
import com.finderfeed.fdlib.to_other_mod.entities.ChesedEntity;
import com.finderfeed.fdlib.util.ProjectileMovementPath;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class ChesedElectricSphereEntity extends FDLivingEntity implements AutoSerializable {

    @SerializableField
    private ProjectileMovementPath path;

    @SerializableField
    private float damage;


    public ChesedElectricSphereEntity(EntityType<? extends LivingEntity> type, Level level) {
        super(type, level);
    }

    public static ChesedElectricSphereEntity summon(Level level,float damage, ProjectileMovementPath path){

        ChesedElectricSphereEntity sphereEntity = new ChesedElectricSphereEntity(BossEntities.CHESED_ELECTRIC_SPHERE.get(),level);
        sphereEntity.setPos(path.getPositions().getFirst());
        sphereEntity.path = path;
        sphereEntity.damage = damage;
        level.addFreshEntity(sphereEntity);

        return sphereEntity;
    }

    @Override
    public void tick() {
        noPhysics = true;
        super.tick();
        if (!level().isClientSide) {
            if (this.path != null) {
                if (!path.isFinished()) {
                    this.path.tick(this);
                }else{
                    this.explode();
                    this.discard();
                }
            }
            this.detectEntitiesAndExplode();
        }else{
            this.getSystem().startAnimation("SPAWN", AnimationTicker.builder(BossAnims.ELECTRIC_ORB_SPAWN.get()).build());
            this.getSystem().startAnimation("IDLE", AnimationTicker.builder(BossAnims.ELECTRIC_ORB_IDLE.get()).build());
        }
    }

    private void detectEntitiesAndExplode(){
        var list = level().getEntitiesOfClass(LivingEntity.class,this.getBoundingBox(),living->{
            return !(living instanceof ChesedElectricSphereEntity) && !(living instanceof ChesedEntity);
        });
        if (list.isEmpty()) return;

        this.explode();

        this.discard();
    }

    @Override
    public void onAddedToLevel() {
        super.onAddedToLevel();
        if (level().isClientSide){
            this.getSystem().startAnimation("IDLE", AnimationTicker.builder(BossAnims.ELECTRIC_ORB_IDLE.get()).build());
        }
    }

    private void explode(){
        var list = level().getEntitiesOfClass(LivingEntity.class,this.getBoundingBox().inflate(1,1,1),living->{
            return !(living instanceof ChesedElectricSphereEntity) && !(living instanceof ChesedEntity);
        });

        for (LivingEntity entity : list){

            //TODO: damage

        }

        //TODO: FX

    }

    @Override
    protected void onInsideBlock(BlockState p_20005_) {
        super.onInsideBlock(p_20005_);
        this.explode();
    }

    @Override
    public boolean isNoGravity() {
        return true;
    }


    @Override
    public boolean save(CompoundTag tag) {
        this.autoSave(tag);
        return super.save(tag);
    }

    @Override
    public void load(CompoundTag tag) {
        this.autoLoad(tag);
        super.load(tag);
    }
}
