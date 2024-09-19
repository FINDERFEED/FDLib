package com.finderfeed.fdlib.util;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.phys.Vec3;

public class FDByteBufCodecs {

    public static final StreamCodec<ByteBuf, Vec3> VEC3 = new StreamCodec<ByteBuf, Vec3>() {
        @Override
        public Vec3 decode(ByteBuf buf) {
            return new Vec3(
                    buf.readDouble(),
                    buf.readDouble(),
                    buf.readDouble()
            );
        }

        @Override
        public void encode(ByteBuf buf, Vec3 vec3) {
            buf.writeDouble(vec3.x);
            buf.writeDouble(vec3.y);
            buf.writeDouble(vec3.z);
        }
    };

}
