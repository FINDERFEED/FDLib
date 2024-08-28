package com.finderfeed.fdlib.nbt;

import java.util.HashMap;

public class FDTagDeserializers {

    public static final HashMap<Class<?>,TagDeserializer<?>> DESERIALIZERS = new HashMap<>();


    /**
     * Register in FMLCommonSetupEvent
     */
    public static <T> void registerDeserializer(Class<T> serializableClass,TagDeserializer<T> deserializer){
        DESERIALIZERS.put(serializableClass,deserializer);
    }
}
