package com.finderfeed.fdlib.systems.particle;

import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.function.Function;
import java.util.function.Supplier;

public class FDParticleProcessors {

    private static final HashMap<String, ParticleProcessorType<?>> PARTICLE_PROCESSORS = new HashMap<>();

    public static final ParticleProcessorType<EmptyParticleProcessor> EMPTY_PROCESSOR = register(new EmptyParticleProcessor.Type());
    public static final ParticleProcessorType<CompositeParticleProcessor> COMPOSITE = register(new CompositeParticleProcessor.Type());
    public static final ParticleProcessorType<CircleParticleProcessor> CIRCLE_PARTICLE_PROCESSOR = register(new CircleParticleProcessor.Type());
    public static final ParticleProcessorType<SetParticleSpeedProcessor> SET_PARTICLE_SPEED = register(new SetParticleSpeedProcessor.Type());

    public static <T extends ParticleProcessor<T>>  ParticleProcessorType<T> register(ParticleProcessorType<T> type){
        PARTICLE_PROCESSORS.put(type.id().toString(),type);
        return type;
    }

    public static ParticleProcessorType<?> getType(ResourceLocation location){
        return PARTICLE_PROCESSORS.get(location.toString());
    }

}
