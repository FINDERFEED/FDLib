package com.finderfeed.fdlib.nbt;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

public class FDTagHelper {


    public static void saveVec3List(CompoundTag tag,String name, List<Vec3> vecs){
        tag.putInt(name + "_size",vecs.size());
        for (int i = 0; i < vecs.size();i++){
            Vec3 v = vecs.get(i);
            tag.putDouble(name + "_x_" + i,v.x);
            tag.putDouble(name + "_y_" + i,v.y);
            tag.putDouble(name + "_z_" + i,v.z);
        }
    }

    public static List<Vec3> loadVec3List(CompoundTag tag,String name){
        List<Vec3> l = new ArrayList<>();
        int len = tag.getInt(name + "_size");
        for (int i = 0; i < len;i++){
            l.add(new Vec3(
                    tag.getDouble(name + "_x_" + i),
                    tag.getDouble(name + "_y_" + i),
                    tag.getDouble(name + "_z_" + i)
            ));
        }
        return l;
    }


}
