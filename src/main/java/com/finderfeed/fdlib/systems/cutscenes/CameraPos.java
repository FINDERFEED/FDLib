package com.finderfeed.fdlib.systems.cutscenes;

import com.finderfeed.fdlib.FDLibCalls;
import com.finderfeed.fdlib.nbt.AutoSerializable;
import com.finderfeed.fdlib.nbt.SerializableField;
import com.finderfeed.fdlib.util.math.FDMathUtil;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

public class CameraPos implements AutoSerializable {

    @SerializableField
    private Vec3 pos;

    @SerializableField
    private float yaw;

    @SerializableField
    private float pitch;

    protected CameraPos(){}

    public CameraPos(Vec3 pos, float yaw,float pitch){
        this.pos = pos;
        this.yaw = FDMathUtil.convertMCYRotationToNormal(yaw);
        this.pitch = Mth.clamp(pitch,-90,90);
    }

    //Will cause anomalies with look direction vec (0; Y; 0)
    public CameraPos(Vec3 pos,Vec3 lookDirection){
        this(pos,FDMathUtil.yRotFromVector(lookDirection.normalize()),FDMathUtil.xRotFromVector(lookDirection.normalize()));
    }

    public Vec3 interpolate(CameraPos next,float p){
        return new Vec3(
                FDMathUtil.lerp(this.pos.x,next.pos.x,p),
                FDMathUtil.lerp(this.pos.y,next.pos.y,p),
                FDMathUtil.lerp(this.pos.z,next.pos.z,p)
        );
    }

    public Vec3 getPos() {
        return pos;
    }

    public float getYaw(){
        return yaw;
    }

    public float getPitch(){
        return pitch;
    }

}
