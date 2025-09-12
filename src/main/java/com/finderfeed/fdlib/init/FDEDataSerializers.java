package com.finderfeed.fdlib.init;

import com.finderfeed.fdlib.FDLib;
import com.finderfeed.fdlib.systems.stream_codecs.NetworkCodec;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public class FDEDataSerializers {


    public static final DeferredRegister<EntityDataSerializer<?>> SERIALIZERS = DeferredRegister.create(ForgeRegistries.Keys.ENTITY_DATA_SERIALIZERS, FDLib.MOD_ID);

    public static final Supplier<EntityDataSerializer<Vec3>> VEC3 = SERIALIZERS.register("vec3",()->{
        return new EntityDataSerializer<Vec3>() {

            @Override
            public void write(FriendlyByteBuf buf, Vec3 vec3) {
                NetworkCodec.VEC3.toNetwork(buf,vec3);
            }

            @Override
            public Vec3 read(FriendlyByteBuf p_135024_) {
                return NetworkCodec.VEC3.fromNetwork(p_135024_);
            }

            @Override
            public Vec3 copy(Vec3 vec) {
                return new Vec3(vec.x,vec.y,vec.z);
            }
        };
    });

}
