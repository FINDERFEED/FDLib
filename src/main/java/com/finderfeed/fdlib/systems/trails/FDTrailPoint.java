package com.finderfeed.fdlib.systems.trails;

import net.minecraft.world.phys.Vec3;

public class FDTrailPoint {

    private Vec3 pos;
    private int age;

    public FDTrailPoint(Vec3 pos, int age){
        this.age = age;
        this.pos = pos;
    }

    public void tick(){
        age--;
    }

    public Vec3 getPos() {
        return pos;
    }

    public boolean shouldBeRemoved(){
        return age < 0;
    }

}
