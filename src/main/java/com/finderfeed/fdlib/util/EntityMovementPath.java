package com.finderfeed.fdlib.util;

import com.finderfeed.fdlib.nbt.AutoSerializable;
import com.finderfeed.fdlib.nbt.FDTagHelper;
import com.finderfeed.fdlib.nbt.SerializableField;
import com.finderfeed.fdlib.util.math.FDMathUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
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

    public EntityMovementPath(){}

    public EntityMovementPath(int time,boolean cycle){
        this.time = time;
        this.cycle = cycle;
    }

    public EntityMovementPath(Vec3 initPos,int time,boolean cycle){
        this.positions.add(initPos);
        this.time = time;
    }

    public void addPos(Vec3 pos){
        this.positions.add(pos);
    }

    public void tick(Entity entity){
        if (!cycle){
            if (this.isFinished()){
                return;
            }
            progress++;
        }else{
            progress = (progress + 1) % (time + 1);
        }
        Vec3 v = FDMathUtil.catmullRom(positions,progress / (float) time);
        entity.setPos(v);
    }

    public boolean isFinished(){
        return this.progress >= time && !cycle;
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
