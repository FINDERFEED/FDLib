package com.finderfeed.fdlib.nbt;

import net.minecraft.nbt.CompoundTag;
import org.lwjgl.system.MemoryUtil;

/**
 * Untested. Idk if it works or not.

 * If an annotated field is implementing
 * that interface you SHOULD have it initialized,
 * it will be loaded upon "load" call.
 */
public interface AutoSerializable {

    default void save(CompoundTag tag){
        TagSerializationHelper.saveFields(tag,this);
    }

    default void save(String name,CompoundTag tag){
        CompoundTag t = new CompoundTag();
        TagSerializationHelper.saveFields(t,this);
        tag.put(name,t);
    }

    default void load(CompoundTag tag){
        TagSerializationHelper.loadFields(tag,this);
    }

    default void load(String name,CompoundTag tag){
        CompoundTag t = tag.getCompound(name);
        TagSerializationHelper.loadFields(t,this);
    }

}
