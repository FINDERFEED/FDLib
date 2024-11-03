package com.finderfeed.fdlib.systems.particle.particle_emitter;

import com.finderfeed.fdlib.util.FDByteBufCodecs;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.ArrayList;
import java.util.List;

public class ParticleEmitterData {

    public static final StreamCodec<FriendlyByteBuf,List<ParticleOptions>> OPTIONS_CODEC = new StreamCodec<FriendlyByteBuf, List<ParticleOptions>>() {
        @Override
        public List<ParticleOptions> decode(FriendlyByteBuf buf) {

            List<ParticleOptions> list = new ArrayList<>();
            int size = buf.readInt();
            for (int i = 0; i < size;i++){

                var location = buf.readResourceLocation();
                var type = BuiltInRegistries.PARTICLE_TYPE.get(location);
                if (type == null) throw new RuntimeException("Unknown particle type: " + location);

                ParticleOptions options = ((StreamCodec<FriendlyByteBuf,ParticleOptions>)type.streamCodec()).decode(buf);
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
                ((StreamCodec<FriendlyByteBuf,ParticleOptions>)type.streamCodec()).encode(buf,options);
            }

        }
    };

    public static final StreamCodec<FriendlyByteBuf,ParticleEmitterData> STREAM_CODEC = StreamCodec.composite(
            FDByteBufCodecs.VEC3,v->v.position,
            ByteBufCodecs.INT,v->v.lifetime,
            EmitterProcessor.STREAM_CODEC,v->v.processor,
            OPTIONS_CODEC,v->v.particleTypes,
            (position,lifetime,processor,types)->{
                ParticleEmitterData data = new ParticleEmitterData();
                data.position = position;
                data.lifetime = lifetime;
                data.processor = processor;
                data.particleTypes = types;
                return data;
            }

    );

    public Vec3 position;

    public int lifetime;

    public EmitterProcessor<?> processor;

    public List<ParticleOptions> particleTypes;



}
