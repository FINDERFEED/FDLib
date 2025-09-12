package com.finderfeed.fdlib.systems.particle.particle_emitter.processors;

import com.finderfeed.fdlib.FDClientHelpers;
import com.finderfeed.fdlib.FDLib;
import com.finderfeed.fdlib.systems.particle.particle_emitter.EmitterProcessor;
import com.finderfeed.fdlib.systems.particle.particle_emitter.EmitterProcessorType;
import com.finderfeed.fdlib.systems.particle.particle_emitter.FDEmitterProcessorTypes;
import com.finderfeed.fdlib.systems.particle.particle_emitter.ParticleEmitter;
import com.finderfeed.fdlib.util.NetworkCodec;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.NetworkCodec;
import net.minecraft.network.codec.NetworkCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class BoundToEntityProcessor implements EmitterProcessor<BoundToEntityProcessor> {

    private int id;

    private Vec3 offset;

    private Entity entity;

    public BoundToEntityProcessor(int id, Vec3 offset){
        this.id = id;
        this.offset = offset;
    }

    @Override
    public void initEmitter(ParticleEmitter emitter) {
        Level level = FDClientHelpers.getClientLevel();
        var entity = level.getEntity(id);
        if (entity == null) {
            emitter.removed = true;
        }else{
            this.entity = entity;
            emitter.data.position = entity.position().add(offset);
        }
    }

    @Override
    public void tickEmitter(ParticleEmitter emitter) {
        if (entity == null){
            emitter.removed = true;
            return;
        }else {
            emitter.data.position = entity.position().add(offset);
            if (entity.isRemoved()) {
                emitter.removed = true;
            }
        }
    }

    @Override
    public void tickParticle(Particle particle) {

    }

    @Override
    public void initParticle(Particle particle) {

    }

    @Override
    public EmitterProcessorType<BoundToEntityProcessor> type() {
        return FDEmitterProcessorTypes.BOUND_TO_ENTITY;
    }

    public static class Type implements EmitterProcessorType<BoundToEntityProcessor>{

        public static final NetworkCodec<BoundToEntityProcessor> STREAM_CODEC = NetworkCodec.composite(
                NetworkCodec.INT,v->v.id,
                NetworkCodec.VEC3,v->v.offset,
                (id,offset)->{
                    BoundToEntityProcessor processor = new BoundToEntityProcessor(id,offset);
                    return processor;
                }
        );

        @Override
        public NetworkCodec<BoundToEntityProcessor> codec() {
            return STREAM_CODEC;
        }

        @Override
        public ResourceLocation id() {
            return ResourceLocation.tryBuild(FDLib.MOD_ID,"bound_to_entity");
        }
    }

}
