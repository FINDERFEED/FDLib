package com.finderfeed.fdlib.systems.particle.particle_emitter;

import com.finderfeed.fdlib.util.NetworkCodec;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.NetworkCodec;
import net.minecraft.network.codec.NetworkCodec;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.neoforge.registries.NeoForgeRegistries;

import java.util.ArrayList;
import java.util.List;

public class ParticleEmitterData {

    public static final NetworkCodec<List<ParticleOptions>> OPTIONS_CODEC = new NetworkCodec<List<ParticleOptions>>() {
        @Override
        public List<ParticleOptions> decode(FriendlyByteBuf buf) {

            List<ParticleOptions> list = new ArrayList<>();
            int size = buf.readInt();
            for (int i = 0; i < size;i++){

                var location = buf.readResourceLocation();
                var type = BuiltInRegistries.PARTICLE_TYPE.get(location);
                if (type == null) throw new RuntimeException("Unknown particle type: " + location);

                ParticleOptions options = ((NetworkCodec<ParticleOptions>)type.NetworkCodec()).decode(buf);
                list.add(options);

            }

            return list;
        }

        @Override
        public void encode(FriendlyByteBuf buf, List<ParticleOptions> list) {
            buf.writeInt(list.size());
            for (ParticleOptions options : list){
                var type = options.getType();

                var key = BuiltInRegistries.PARTICLE_TYPE.getKey(type);
                buf.writeResourceLocation(key);
                ((NetworkCodec<ParticleOptions>)type.NetworkCodec()).encode(buf,options);
            }

        }
    };

    public static final NetworkCodec<ParticleEmitterData> STREAM_CODEC = NetworkCodec.composite(
            NetworkCodec.VEC3,v->v.position,
            NetworkCodec.INT,v->v.lifetime,
            NetworkCodec.INT,v->v.particlesPerTick,
            EmitterProcessor.STREAM_CODEC,v->v.processor,
            OPTIONS_CODEC,v->v.particleTypes,
            (position,lifetime,particlesPerTick,processor,types)->{
                ParticleEmitterData data = new ParticleEmitterData();
                data.position = position;
                data.lifetime = lifetime;
                data.processor = processor;
                data.particleTypes = types;
                data.particlesPerTick = particlesPerTick;
                return data;
            }

    );

    private ParticleEmitterData(){}

    public Vec3 position = Vec3.ZERO;

    public int particlesPerTick = 1;

    public int lifetime = 20;

    public EmitterProcessor<?> processor = new EmptyEmitterProcessor();

    public List<ParticleOptions> particleTypes = new ArrayList<>();

    public static Builder builder(ParticleOptions options){
        return new Builder(options);
    }

    public static class Builder{

        private ParticleEmitterData data = new ParticleEmitterData();

        public Builder(ParticleOptions options){
            this.data.particleTypes.add(options);
        }

        public Builder position(Vec3 v){
            this.data.position = v;
            return this;
        }

        public Builder lifetime(int lifetime){
            this.data.lifetime = lifetime;
            return this;
        }

        public Builder processor(EmitterProcessor<?> processor){
            this.data.processor = processor;
            return this;
        }

        public Builder particlesPerTick(int amount){
            this.data.particlesPerTick = amount;
            return this;
        }

        public Builder addParticle(ParticleOptions options){
            this.data.particleTypes.add(options);
            return this;
        }

        public ParticleEmitterData build(){
            return data;
        }


    }

}
