package com.finderfeed.fdlib.util;

import com.finderfeed.fdlib.nbt.AutoSerializable;
import com.finderfeed.fdlib.nbt.FDTagHelper;
import com.finderfeed.fdlib.nbt.SerializableField;
import com.finderfeed.fdlib.util.math.FDMathUtil;
import com.finderfeed.fdlib.util.rendering.FDEasings;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

public class EntityMovementPath implements AutoSerializable {

    private List<Vec3> positions = new ArrayList<>();

    @SerializableField
    private int time;

    @SerializableField
    private int progress = 0;

    @SerializableField
    private boolean cycle = false;

    @SerializableField
    private boolean easeInOut = false;

    public EntityMovementPath(){}

    public EntityMovementPath(int time,boolean cycle){
        this.time = time;
        this.cycle = cycle;
    }

    public EntityMovementPath(Vec3 initPos,int time,boolean cycle){
        this.positions.add(initPos);
        this.time = time;
        this.cycle = cycle;
    }

    public void addPos(Vec3 pos){
        this.positions.add(pos);
    }

    public void tick(Entity entity){
        if (!cycle){
            if (this.isFinished()){
                entity.setDeltaMovement(Vec3.ZERO);
                return;
            }
            progress++;
        }else{
            progress = (progress + 1) % (time + 1);
        }
        float p = progress / (float) time;
        if (easeInOut){
            p = FDEasings.easeInOut(p);
        }
        Vec3 v = FDMathUtil.linear(positions,p);
        Vec3 currentPos = entity.position();
        Vec3 b = v.subtract(currentPos);
        entity.setDeltaMovement(b);
    }

    public boolean isFinished(){
        return this.progress >= time && !cycle;
    }

    public void setEaseInOut(boolean easeInOut) {
        this.easeInOut = easeInOut;
    }

    public List<Vec3> getPositions() {
        return positions;
    }

    @Override
    public void save(CompoundTag tag) {
        AutoSerializable.super.save(tag);
        FDTagHelper.saveVec3List(tag,"pos",positions);
    }

    @Override
    public void load(CompoundTag tag) {
        AutoSerializable.super.load(tag);
        this.positions = FDTagHelper.loadVec3List(tag,"pos");
    }
}
