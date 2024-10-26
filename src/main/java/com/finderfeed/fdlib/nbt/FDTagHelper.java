package com.finderfeed.fdlib.nbt;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

public class FDTagHelper {


    public static void saveVec3List(CompoundTag tag,String name, List<Vec3> vecs){
        CompoundTag vec3list = new CompoundTag();

        vec3list.putInt(name + "_size",vecs.size());
        for (int i = 0; i < vecs.size();i++){
            Vec3 v = vecs.get(i);
            vec3list.putDouble(name + "_x_" + i,v.x);
            vec3list.putDouble(name + "_y_" + i,v.y);
            vec3list.putDouble(name + "_z_" + i,v.z);
        }

        tag.put(name,vec3list);
    }

    public static List<Vec3> loadVec3List(CompoundTag tag,String name){
        List<Vec3> l = new ArrayList<>();
        if (!tag.contains(name)) return new ArrayList<>();
        CompoundTag vec3list = tag.getCompound(name);
        int len = vec3list.getInt(name + "_size");
        for (int i = 0; i < len;i++){
            l.add(new Vec3(
                    vec3list.getDouble(name + "_x_" + i),
                    vec3list.getDouble(name + "_y_" + i),
                    vec3list.getDouble(name + "_z_" + i)
            ));
        }
        return l;
    }


}
