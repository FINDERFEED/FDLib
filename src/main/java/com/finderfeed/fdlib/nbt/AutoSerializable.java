package com.finderfeed.fdlib.nbt;

import net.minecraft.nbt.CompoundTag;

/**
 * Untested. Idk if it works or not.

 * If an annotated field is implementing
 * that interface you SHOULD have it initialized,
 * it will be loaded upon "load" call.
 */
public interface AutoSerializable {

    default void autoSave(CompoundTag tag){
        TagSerializationHelper.saveFields(tag,this);
    }

    default void autoSave(String name, CompoundTag tag){
        CompoundTag t = new CompoundTag();
        this.autoSave(t);
        tag.put(name,t);
    }

    default void autoLoad(CompoundTag tag){
        TagSerializationHelper.loadFields(tag,this);
    }

    default void autoLoad(String name, CompoundTag tag){
        CompoundTag t = tag.getCompound(name);
        this.autoLoad(t);
    }

}
