package com.finderfeed.fdlib.util;

import com.mojang.datafixers.util.*;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.phys.Vec3;

import java.util.function.Function;
import java.util.stream.Stream;

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

    public static final StreamCodec<ByteBuf, BlockPos> BLOCK_POS = new StreamCodec<ByteBuf, BlockPos>() {
        @Override
        public BlockPos decode(ByteBuf buf) {
            return new BlockPos(
                    buf.readInt(),
                    buf.readInt(),
                    buf.readInt()
            );
        }

        @Override
        public void encode(ByteBuf buf, BlockPos pos) {
            buf.writeInt(pos.getX());
            buf.writeInt(pos.getY());
            buf.writeInt(pos.getZ());
        }
    };

    public static final StreamCodec<ByteBuf,FDColor> COLOR = new StreamCodec<ByteBuf, FDColor>() {
        @Override
        public FDColor decode(ByteBuf buf) {
            return new FDColor(
                    buf.readFloat(),
                    buf.readFloat(),
                    buf.readFloat(),
                    buf.readFloat()
            );
        }

        @Override
        public void encode(ByteBuf buf, FDColor color) {
            buf.writeFloat(color.r);
            buf.writeFloat(color.g);
            buf.writeFloat(color.b);
            buf.writeFloat(color.a);
        }
    };

    public static <B, C, T1, T2, T3, T4, T5, T6, T7> StreamCodec<B, C> composite(
            final StreamCodec<? super B, T1> c1,
            final Function<C, T1> f1,
            final StreamCodec<? super B, T2> c2,
            final Function<C, T2> f2,
            final StreamCodec<? super B, T3> c3,
            final Function<C, T3> f3,
            final StreamCodec<? super B, T4> c4,
            final Function<C, T4> f4,
            final StreamCodec<? super B, T5> c5,
            final Function<C, T5> f5,
            final StreamCodec<? super B, T6> c6,
            final Function<C, T6> f6,
            final StreamCodec<? super B, T7> c7,
            final Function<C, T7> f7,
            final Function7<T1, T2, T3, T4, T5, T6, T7, C> constructor
    ) {
        return new StreamCodec<B, C>() {
            @Override
            public C decode(B p_330310_) {
                T1 t1 = c1.decode(p_330310_);
                T2 t2 = c2.decode(p_330310_);
                T3 t3 = c3.decode(p_330310_);
                T4 t4 = c4.decode(p_330310_);
                T5 t5 = c5.decode(p_330310_);
                T6 t6 = c6.decode(p_330310_);
                T7 t7 = c7.decode(p_330310_);
                return constructor.apply(t1, t2, t3, t4, t5, t6,t7);
            }

            @Override
            public void encode(B buf, C p_331912_) {
                c1.encode(buf, f1.apply(p_331912_));
                c2.encode(buf, f2.apply(p_331912_));
                c3.encode(buf, f3.apply(p_331912_));
                c4.encode(buf, f4.apply(p_331912_));
                c5.encode(buf, f5.apply(p_331912_));
                c6.encode(buf, f6.apply(p_331912_));
                c7.encode(buf, f7.apply(p_331912_));
            }
        };
    }

    public static <B, C, T1, T2, T3, T4, T5, T6, T7, T8> StreamCodec<B, C> composite(
            final StreamCodec<? super B, T1> c1,
            final Function<C, T1> f1,
            final StreamCodec<? super B, T2> c2,
            final Function<C, T2> f2,
            final StreamCodec<? super B, T3> c3,
            final Function<C, T3> f3,
            final StreamCodec<? super B, T4> c4,
            final Function<C, T4> f4,
            final StreamCodec<? super B, T5> c5,
            final Function<C, T5> f5,
            final StreamCodec<? super B, T6> c6,
            final Function<C, T6> f6,
            final StreamCodec<? super B, T7> c7,
            final Function<C, T7> f7,
            final StreamCodec<? super B, T8> c8,
            final Function<C, T8> f8,
            final Function8<T1, T2, T3, T4, T5, T6, T7,T8, C> constructor
    ) {
        return new StreamCodec<B, C>() {
            @Override
            public C decode(B p_330310_) {
                T1 t1 = c1.decode(p_330310_);
                T2 t2 = c2.decode(p_330310_);
                T3 t3 = c3.decode(p_330310_);
                T4 t4 = c4.decode(p_330310_);
                T5 t5 = c5.decode(p_330310_);
                T6 t6 = c6.decode(p_330310_);
                T7 t7 = c7.decode(p_330310_);
                T8 t8 = c8.decode(p_330310_);
                return constructor.apply(t1, t2, t3, t4, t5, t6,t7,t8);
            }

            @Override
            public void encode(B buf, C p_331912_) {
                c1.encode(buf, f1.apply(p_331912_));
                c2.encode(buf, f2.apply(p_331912_));
                c3.encode(buf, f3.apply(p_331912_));
                c4.encode(buf, f4.apply(p_331912_));
                c5.encode(buf, f5.apply(p_331912_));
                c6.encode(buf, f6.apply(p_331912_));
                c7.encode(buf, f7.apply(p_331912_));
                c8.encode(buf, f8.apply(p_331912_));
            }
        };
    }

    public static <B, C, T1, T2, T3, T4, T5, T6, T7, T8, T9> StreamCodec<B, C> composite(
            final StreamCodec<? super B, T1> c1,
            final Function<C, T1> f1,
            final StreamCodec<? super B, T2> c2,
            final Function<C, T2> f2,
            final StreamCodec<? super B, T3> c3,
            final Function<C, T3> f3,
            final StreamCodec<? super B, T4> c4,
            final Function<C, T4> f4,
            final StreamCodec<? super B, T5> c5,
            final Function<C, T5> f5,
            final StreamCodec<? super B, T6> c6,
            final Function<C, T6> f6,
            final StreamCodec<? super B, T7> c7,
            final Function<C, T7> f7,
            final StreamCodec<? super B, T8> c8,
            final Function<C, T8> f8,
            final StreamCodec<? super B, T9> c9,
            final Function<C, T9> f9,
            final Function9<T1, T2, T3, T4, T5, T6, T7,T8,T9 ,C> constructor
    ) {
        return new StreamCodec<B, C>() {
            @Override
            public C decode(B p_330310_) {
                T1 t1 = c1.decode(p_330310_);
                T2 t2 = c2.decode(p_330310_);
                T3 t3 = c3.decode(p_330310_);
                T4 t4 = c4.decode(p_330310_);
                T5 t5 = c5.decode(p_330310_);
                T6 t6 = c6.decode(p_330310_);
                T7 t7 = c7.decode(p_330310_);
                T8 t8 = c8.decode(p_330310_);
                T9 t9 = c9.decode(p_330310_);
                return constructor.apply(t1, t2, t3, t4, t5, t6,t7,t8,t9);
            }

            @Override
            public void encode(B buf, C p_331912_) {
                c1.encode(buf, f1.apply(p_331912_));
                c2.encode(buf, f2.apply(p_331912_));
                c3.encode(buf, f3.apply(p_331912_));
                c4.encode(buf, f4.apply(p_331912_));
                c5.encode(buf, f5.apply(p_331912_));
                c6.encode(buf, f6.apply(p_331912_));
                c7.encode(buf, f7.apply(p_331912_));
                c8.encode(buf, f8.apply(p_331912_));
                c9.encode(buf, f9.apply(p_331912_));
            }
        };
    }

    public static <B, C, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> StreamCodec<B, C> composite(
            final StreamCodec<? super B, T1> c1,
            final Function<C, T1> f1,
            final StreamCodec<? super B, T2> c2,
            final Function<C, T2> f2,
            final StreamCodec<? super B, T3> c3,
            final Function<C, T3> f3,
            final StreamCodec<? super B, T4> c4,
            final Function<C, T4> f4,
            final StreamCodec<? super B, T5> c5,
            final Function<C, T5> f5,
            final StreamCodec<? super B, T6> c6,
            final Function<C, T6> f6,
            final StreamCodec<? super B, T7> c7,
            final Function<C, T7> f7,
            final StreamCodec<? super B, T8> c8,
            final Function<C, T8> f8,
            final StreamCodec<? super B, T9> c9,
            final Function<C, T9> f9,
            final StreamCodec<? super B, T10> c10,
            final Function<C, T10> f10,
            final Function10<T1, T2, T3, T4, T5, T6, T7,T8,T9,T10,C> constructor
    ) {
        return new StreamCodec<B, C>() {
            @Override
            public C decode(B p_330310_) {
                T1 t1 = c1.decode(p_330310_);
                T2 t2 = c2.decode(p_330310_);
                T3 t3 = c3.decode(p_330310_);
                T4 t4 = c4.decode(p_330310_);
                T5 t5 = c5.decode(p_330310_);
                T6 t6 = c6.decode(p_330310_);
                T7 t7 = c7.decode(p_330310_);
                T8 t8 = c8.decode(p_330310_);
                T9 t9 = c9.decode(p_330310_);
                T10 t10 = c10.decode(p_330310_);
                return constructor.apply(t1, t2, t3, t4, t5, t6,t7,t8,t9,t10);
            }

            @Override
            public void encode(B buf, C p_331912_) {
                c1.encode(buf, f1.apply(p_331912_));
                c2.encode(buf, f2.apply(p_331912_));
                c3.encode(buf, f3.apply(p_331912_));
                c4.encode(buf, f4.apply(p_331912_));
                c5.encode(buf, f5.apply(p_331912_));
                c6.encode(buf, f6.apply(p_331912_));
                c7.encode(buf, f7.apply(p_331912_));
                c8.encode(buf, f8.apply(p_331912_));
                c9.encode(buf, f9.apply(p_331912_));
                c10.encode(buf, f10.apply(p_331912_));
            }
        };
    }

    public static <B, C, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10,T11> StreamCodec<B, C> composite(
            final StreamCodec<? super B, T1> c1,
            final Function<C, T1> f1,
            final StreamCodec<? super B, T2> c2,
            final Function<C, T2> f2,
            final StreamCodec<? super B, T3> c3,
            final Function<C, T3> f3,
            final StreamCodec<? super B, T4> c4,
            final Function<C, T4> f4,
            final StreamCodec<? super B, T5> c5,
            final Function<C, T5> f5,
            final StreamCodec<? super B, T6> c6,
            final Function<C, T6> f6,
            final StreamCodec<? super B, T7> c7,
            final Function<C, T7> f7,
            final StreamCodec<? super B, T8> c8,
            final Function<C, T8> f8,
            final StreamCodec<? super B, T9> c9,
            final Function<C, T9> f9,
            final StreamCodec<? super B, T10> c10,
            final Function<C, T10> f10,
            final StreamCodec<? super B, T11> c11,
            final Function<C, T11> f11,
            final Function11<T1, T2, T3, T4, T5, T6, T7,T8,T9,T10,T11,C> constructor
    ) {
        return new StreamCodec<B, C>() {
            @Override
            public C decode(B p_330310_) {
                T1 t1 = c1.decode(p_330310_);
                T2 t2 = c2.decode(p_330310_);
                T3 t3 = c3.decode(p_330310_);
                T4 t4 = c4.decode(p_330310_);
                T5 t5 = c5.decode(p_330310_);
                T6 t6 = c6.decode(p_330310_);
                T7 t7 = c7.decode(p_330310_);
                T8 t8 = c8.decode(p_330310_);
                T9 t9 = c9.decode(p_330310_);
                T10 t10 = c10.decode(p_330310_);
                T11 t11 = c11.decode(p_330310_);
                return constructor.apply(t1, t2, t3, t4, t5, t6,t7,t8,t9,t10,t11);
            }

            @Override
            public void encode(B buf, C p_331912_) {
                c1.encode(buf, f1.apply(p_331912_));
                c2.encode(buf, f2.apply(p_331912_));
                c3.encode(buf, f3.apply(p_331912_));
                c4.encode(buf, f4.apply(p_331912_));
                c5.encode(buf, f5.apply(p_331912_));
                c6.encode(buf, f6.apply(p_331912_));
                c7.encode(buf, f7.apply(p_331912_));
                c8.encode(buf, f8.apply(p_331912_));
                c9.encode(buf, f9.apply(p_331912_));
                c10.encode(buf, f10.apply(p_331912_));
                c11.encode(buf, f11.apply(p_331912_));
            }
        };
    }

    public static <B, C, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10,T11,T12> StreamCodec<B, C> composite(
            final StreamCodec<? super B, T1> c1,
            final Function<C, T1> f1,
            final StreamCodec<? super B, T2> c2,
            final Function<C, T2> f2,
            final StreamCodec<? super B, T3> c3,
            final Function<C, T3> f3,
            final StreamCodec<? super B, T4> c4,
            final Function<C, T4> f4,
            final StreamCodec<? super B, T5> c5,
            final Function<C, T5> f5,
            final StreamCodec<? super B, T6> c6,
            final Function<C, T6> f6,
            final StreamCodec<? super B, T7> c7,
            final Function<C, T7> f7,
            final StreamCodec<? super B, T8> c8,
            final Function<C, T8> f8,
            final StreamCodec<? super B, T9> c9,
            final Function<C, T9> f9,
            final StreamCodec<? super B, T10> c10,
            final Function<C, T10> f10,
            final StreamCodec<? super B, T11> c11,
            final Function<C, T11> f11,
            final StreamCodec<? super B, T12> c12,
            final Function<C, T12> f12,
            final Function12<T1, T2, T3, T4, T5, T6, T7,T8,T9,T10,T11,T12,C> constructor
    ) {
        return new StreamCodec<B, C>() {
            @Override
            public C decode(B p_330310_) {
                T1 t1 = c1.decode(p_330310_);
                T2 t2 = c2.decode(p_330310_);
                T3 t3 = c3.decode(p_330310_);
                T4 t4 = c4.decode(p_330310_);
                T5 t5 = c5.decode(p_330310_);
                T6 t6 = c6.decode(p_330310_);
                T7 t7 = c7.decode(p_330310_);
                T8 t8 = c8.decode(p_330310_);
                T9 t9 = c9.decode(p_330310_);
                T10 t10 = c10.decode(p_330310_);
                T11 t11 = c11.decode(p_330310_);
                T12 t12 = c12.decode(p_330310_);
                return constructor.apply(t1, t2, t3, t4, t5, t6,t7,t8,t9,t10,t11,t12);
            }

            @Override
            public void encode(B buf, C p_331912_) {
                c1.encode(buf, f1.apply(p_331912_));
                c2.encode(buf, f2.apply(p_331912_));
                c3.encode(buf, f3.apply(p_331912_));
                c4.encode(buf, f4.apply(p_331912_));
                c5.encode(buf, f5.apply(p_331912_));
                c6.encode(buf, f6.apply(p_331912_));
                c7.encode(buf, f7.apply(p_331912_));
                c8.encode(buf, f8.apply(p_331912_));
                c9.encode(buf, f9.apply(p_331912_));
                c10.encode(buf, f10.apply(p_331912_));
                c11.encode(buf, f11.apply(p_331912_));
                c12.encode(buf, f12.apply(p_331912_));
            }
        };
    }

}
