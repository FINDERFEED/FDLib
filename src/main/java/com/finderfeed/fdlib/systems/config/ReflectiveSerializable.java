package com.finderfeed.fdlib.systems.config;

import com.mojang.serialization.Codec;

public interface ReflectiveSerializable<T> {
    Codec<T> reflectiveCodec();

}
