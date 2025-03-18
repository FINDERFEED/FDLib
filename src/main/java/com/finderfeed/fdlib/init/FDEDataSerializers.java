package com.finderfeed.fdlib.init;

import com.finderfeed.fdlib.FDLib;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public class FDEDataSerializers {


    public static final DeferredRegister<EntityDataSerializer<?>> SERIALIZERS = DeferredRegister.create(NeoForgeRegistries.ENTITY_DATA_SERIALIZERS, FDLib.MOD_ID);

    public static final Supplier<EntityDataSerializer<Vec3>> VEC3 = SERIALIZERS.register("vec3",()->{
        return new EntityDataSerializer<Vec3>() {

            public static final StreamCodec<FriendlyByteBuf,Vec3> CODEC = StreamCodec.composite(
                    ByteBufCodecs.DOUBLE,v->v.x,
                    ByteBufCodecs.DOUBLE,v->v.y,
                    ByteBufCodecs.DOUBLE,v->v.z,
                    Vec3::new
            );

            @Override
            public StreamCodec<? super RegistryFriendlyByteBuf, Vec3> codec() {
                return CODEC;
            }

            @Override
            public Vec3 copy(Vec3 vec) {
                return new Vec3(vec.x,vec.y,vec.z);
            }
        };
    });

}
