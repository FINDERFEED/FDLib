package com.finderfeed.fdlib.nbt;

import net.minecraft.nbt.CompoundTag;

/**
 * If an annotated field is implementing
 * that interface you SHOULD have it initialized,
 * it will be loaded upon "load" call.
 */
public interface AutoSerializable {

    default void save(CompoundTag tag){
        TagSerializationHelper.saveFields(tag,this);
    }

    default void load(CompoundTag tag){
        TagSerializationHelper.loadFields(tag,this);
    }

}
