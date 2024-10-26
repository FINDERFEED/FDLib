package com.finderfeed.fdlib.util;

import com.finderfeed.fdlib.nbt.AutoSerializable;
import com.finderfeed.fdlib.nbt.FDTagHelper;
import com.finderfeed.fdlib.nbt.SerializableField;
import com.finderfeed.fdlib.util.math.FDMathUtil;
import com.finderfeed.fdlib.util.rendering.FDEasings;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

public class ProjectileMovementPath implements AutoSerializable {

    private List<Vec3> positions = new ArrayList<>();

    @SerializableField
    private int time;

    @SerializableField
    private int progress = 0;

    @SerializableField
    private boolean cycle = false;

    @SerializableField
    private boolean easeInOut = false;

    @SerializableField
    private boolean easeIn = false;

    @SerializableField
    private Vec3 speedOnEnd = null;

    @SerializableField
    private ProjectileMovementPath next;

    public ProjectileMovementPath(){}

    public ProjectileMovementPath(int time, boolean cycle){
        this.time = time;
        this.cycle = cycle;
    }

    public ProjectileMovementPath(Vec3 initPos, int time, boolean cycle){
        this.positions.add(initPos);
        this.time = time;
        this.cycle = cycle;
    }

    public ProjectileMovementPath addPos(Vec3 pos){
        this.positions.add(pos);
        return this;
    }

    public void tick(Entity entity){
        if (!cycle){
            if (this.isFinished()){
                if (speedOnEnd != null) {
                    entity.setDeltaMovement(speedOnEnd);
                }
                return;
            }
            progress++;
        }else{
            progress = (progress + 1) % (time + 1);
        }
        float p = progress / (float) time;
        if (easeInOut){
            p = FDEasings.easeInOut(p);
        }else if (easeIn){
            p = FDEasings.easeIn(p);
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

    public void setEaseIn(boolean easeIn) {
        this.easeIn = easeIn;
    }

    public void setSpeedOnEnd(Vec3 speedOnEnd) {
        this.speedOnEnd = speedOnEnd;
    }

    public List<Vec3> getPositions() {
        return positions;
    }

    public ProjectileMovementPath getNext() {
        return next;
    }

    public void setNext(ProjectileMovementPath next) {
        this.next = next;
    }

    @Override
    public void autoSave(CompoundTag tag) {
        AutoSerializable.super.autoSave(tag);
        FDTagHelper.saveVec3List(tag,"pos",positions);
    }

    @Override
    public void autoLoad(CompoundTag tag) {
        AutoSerializable.super.autoLoad(tag);
        this.positions = FDTagHelper.loadVec3List(tag,"pos");
    }
}
