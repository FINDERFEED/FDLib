package com.finderfeed.fdlib.init;

import com.finderfeed.fdlib.nbt.TagDeserializer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.phys.Vec3;

public class FDDefaultTagDeserializers {

    public static TagDeserializer<Vec3> VEC3 = new TagDeserializer<Vec3>() {
        @Override
        public void serialize(String name, Vec3 object, CompoundTag tag) {
            CompoundTag t = new CompoundTag();
            t.putDouble("x",object.x);
            t.putDouble("y",object.y);
            t.putDouble("z",object.z);
            tag.put(name,t);
        }

        @Override
        public Vec3 deserialize(String name, CompoundTag tag) {
            CompoundTag v = tag.getCompound(name);
            return new Vec3(
                    v.getDouble("x"),
                    v.getDouble("y"),
                    v.getDouble("z")
            );
        }
    };

}
