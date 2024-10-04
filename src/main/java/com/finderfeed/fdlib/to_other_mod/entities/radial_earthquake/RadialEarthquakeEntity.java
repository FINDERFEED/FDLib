package com.finderfeed.fdlib.to_other_mod.entities.radial_earthquake;

import com.finderfeed.fdlib.nbt.AutoSerializable;
import com.finderfeed.fdlib.nbt.SerializableField;
import com.finderfeed.fdlib.to_other_mod.BossEntities;
import com.finderfeed.fdlib.to_other_mod.entities.earthshatter_entity.EarthShatterEntity;
import com.finderfeed.fdlib.to_other_mod.entities.earthshatter_entity.EarthShatterSettings;
import com.finderfeed.fdlib.util.math.FDMathUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class RadialEarthquakeEntity extends Entity implements AutoSerializable {


    @SerializableField
    public float damage;

    @SerializableField
    public int endRadius = 10;

    @SerializableField
    public float speed;

    @SerializableField
    public float current = 0;

    @SerializableField
    private int previousRadius = -1;

    public RadialEarthquakeEntity(EntityType<?> type, Level level) {
        super(type, level);
    }

    public static RadialEarthquakeEntity summon(Level level,BlockPos pos,int startRadius,int endRadius,float speed,float damage){
        RadialEarthquakeEntity radialEarthquakeEntity = new RadialEarthquakeEntity(BossEntities.RADIAL_EARTHQUAKE.get(),level);
        radialEarthquakeEntity.speed = speed;
        radialEarthquakeEntity.current = startRadius;
        radialEarthquakeEntity.endRadius = endRadius;
        radialEarthquakeEntity.damage = damage;
        radialEarthquakeEntity.setPos(pos.getCenter());
        level.addFreshEntity(radialEarthquakeEntity);
        return radialEarthquakeEntity;
    }


    @Override
    public void tick() {
        super.tick();
        if (!level().isClientSide){
            int currentRad = (int)current;
            if (currentRad != previousRadius) {
                for (int i = previousRadius + 1; i <= currentRad;i++) {
                    this.spawnAndDamageWithRadius(i);
                }
            }
            previousRadius = currentRad;
            current += speed;
            if (current >= endRadius){
                this.remove(RemovalReason.DISCARDED);
            }
        }
    }


    public void doEarthquake(int start,int end){
        for (int i = start; i <= end;i++){
            this.spawnAndDamageWithRadius(i);
        }
    }


    public void spawnAndDamageWithRadius(int rad){
        Vec3 b = new Vec3(rad,0,0);
        float angle;
        if (rad != 0){
            angle = 0.5f / rad;
        }else{
            angle = FDMathUtil.FPI * 2;
        }

        BlockPos prevPos = null;
        Vec3 tpos = this.position();
        for (float i = 0; i < FDMathUtil.FPI * 2;i += angle){
            Vec3 pos = tpos.add(b.yRot(i));
            BlockPos ppos = FDMathUtil.vec3ToBlockPos(pos);
            if (!ppos.equals(prevPos)){
                Vec3 c = ppos.getCenter();
                Vec3 dir = tpos.subtract(c).multiply(1,0,1).normalize().add(0,1,0);
                EarthShatterEntity entity = EarthShatterEntity.summon(level(),ppos, EarthShatterSettings.builder()
                                .direction(dir)
                                .stayTime(0)
                                .upTime(2)
                                .downTime(2)
                        .build());
            }
            prevPos = ppos;
        }


    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder data) {

    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        this.autoLoad(tag);
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        this.autoSave(tag);
    }

}
