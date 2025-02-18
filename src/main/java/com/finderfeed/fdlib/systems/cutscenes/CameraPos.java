package com.finderfeed.fdlib.systems.cutscenes;

import com.finderfeed.fdlib.FDLibCalls;
import com.finderfeed.fdlib.nbt.AutoSerializable;
import com.finderfeed.fdlib.nbt.SerializableField;
import com.finderfeed.fdlib.util.math.FDMathUtil;
import net.minecraft.world.phys.Vec3;

public class CameraPos implements AutoSerializable {

    @SerializableField
    private Vec3 pos;

    @SerializableField
    private Vec3 lookDirection;

    protected CameraPos(){}

    public CameraPos(Vec3 pos,Vec3 lookDirection){
        this.pos = pos;
        this.lookDirection = lookDirection;
    }

    public Vec3 getPos() {
        return pos;
    }

    public Vec3 getLookDirection() {
        return lookDirection;
    }

    public Vec3 interpolate(CameraPos next,float p){
        return new Vec3(
                FDMathUtil.lerp(this.pos.x,next.pos.x,p),
                FDMathUtil.lerp(this.pos.y,next.pos.y,p),
                FDMathUtil.lerp(this.pos.z,next.pos.z,p)
        );
    }

    //pitch
    public float getXRot(){
        return FDMathUtil.xRotFromVector(lookDirection);
    }

    //yaw
    public float getYRot(){
        return FDMathUtil.yRotFromVector(lookDirection);
    }

}
