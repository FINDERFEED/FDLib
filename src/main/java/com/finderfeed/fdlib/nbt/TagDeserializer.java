package com.finderfeed.fdlib.nbt;

import net.minecraft.nbt.CompoundTag;

public interface TagDeserializer<T> {

    /**
     * YOU SHOULD PUT THE OBJECT UNDER THE GIVEN NAME OR BANG BADABUM AND EVERYTHING EXPLODES
     */
    void serialize(String name,T object, CompoundTag tag);

    T deserialize(String name,CompoundTag tag);

}
