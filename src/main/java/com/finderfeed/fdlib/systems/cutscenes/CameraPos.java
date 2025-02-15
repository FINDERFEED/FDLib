package com.finderfeed.fdlib.systems.cutscenes;

import com.finderfeed.fdlib.nbt.AutoSerializable;
import com.finderfeed.fdlib.nbt.SerializableField;
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

}
