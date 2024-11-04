package com.finderfeed.fdlib.systems.particle.particle_emitter;

import com.finderfeed.fdlib.systems.particle.particle_emitter.processors.BoundToEntityProcessor;
import com.finderfeed.fdlib.systems.particle.particle_emitter.processors.CircleSpawnProcessor;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;

public class FDEmitterProcessorTypes {

    private static HashMap<String,EmitterProcessorType<?>> TYPES_REGISTRY = new HashMap<>();


    public static final EmitterProcessorType<EmptyEmitterProcessor> EMPTY = register(new EmptyEmitterProcessor.Type());

    public static final EmitterProcessorType<CompositeEmitterProcessor> COMPOSITE = register(new CompositeEmitterProcessor.Type());

    public static final EmitterProcessorType<CircleSpawnProcessor> CIRCLE_SPAWN_PROCESSOR = register(new CircleSpawnProcessor.Type());

    public static final EmitterProcessorType<BoundToEntityProcessor> BOUND_TO_ENTITY = register(new BoundToEntityProcessor.Type());

    public static EmitterProcessorType<?> get(ResourceLocation location){
        return TYPES_REGISTRY.get(location.toString());
    }

    public static <T extends EmitterProcessor<T>> EmitterProcessorType<T> register(EmitterProcessorType<T> type){
        var id = type.id().toString();

        if (TYPES_REGISTRY.containsKey(id)){
            throw new RuntimeException("Emitter processor type already exists: " + id);
        }
        TYPES_REGISTRY.put(id,type);

        return type;
    }

}
