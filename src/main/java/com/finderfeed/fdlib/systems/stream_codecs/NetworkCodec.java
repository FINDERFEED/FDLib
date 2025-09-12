package com.finderfeed.fdlib.systems.stream_codecs;

import com.finderfeed.fdlib.util.FDColor;
import com.mojang.datafixers.util.*;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.Vec3;

import java.util.function.BiFunction;
import java.util.function.Function;

//I need it to port to 1.20.1 easier, i am sorry :D
public abstract class NetworkCodec<K> {

    public abstract void toNetwork(FriendlyByteBuf buf, K object);

    public abstract K fromNetwork(FriendlyByteBuf buf);

    public static NetworkCodec<Integer> INT = new NetworkCodec<Integer>() {
        @Override
        public void toNetwork(FriendlyByteBuf buf, Integer object) {
            buf.writeInt(object);
        }

        @Override
        public Integer fromNetwork(FriendlyByteBuf buf) {
            return buf.readInt();
        }
    };

    public static NetworkCodec<Float> FLOAT = new NetworkCodec<Float>() {
        @Override
        public void toNetwork(FriendlyByteBuf buf, Float object) {
            buf.writeFloat(object);
        }

        @Override
        public Float fromNetwork(FriendlyByteBuf buf) {
            return buf.readFloat();
        }
    };

    public static NetworkCodec<Double> DOUBLE = new NetworkCodec<Double>() {
        @Override
        public void toNetwork(FriendlyByteBuf buf, Double object) {
            buf.writeDouble(object);
        }

        @Override
        public Double fromNetwork(FriendlyByteBuf buf) {
            return buf.readDouble();
        }
    };

    public static NetworkCodec<Direction> DIRECTION = new NetworkCodec<Direction>() {
        @Override
        public void toNetwork(FriendlyByteBuf buf, Direction object) {
            buf.writeInt(object.ordinal());
        }

        @Override
        public Direction fromNetwork(FriendlyByteBuf buf) {
            return Direction.values()[buf.readInt()];
        }
    };

    public static NetworkCodec<java.util.UUID> UUID = new NetworkCodec<java.util.UUID>() {
        @Override
        public java.util.UUID fromNetwork(FriendlyByteBuf buf) {
            return buf.readUUID();
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, java.util.UUID uuid) {
            buf.writeUUID(uuid);
        }
    };

    public static NetworkCodec<Vec3> VEC3 = new NetworkCodec<Vec3>() {
        @Override
        public Vec3 fromNetwork(FriendlyByteBuf buf) {
            return new Vec3(
                    buf.readFloat(),
                    buf.readFloat(),
                    buf.readFloat()
            );
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, Vec3 vec3) {
            buf.writeFloat((float)vec3.x);
            buf.writeFloat((float)vec3.y);
            buf.writeFloat((float)vec3.z);
        }
    };

    public static NetworkCodec<BlockPos> BLOCK_POS = new NetworkCodec<BlockPos>() {
        @Override
        public BlockPos fromNetwork(FriendlyByteBuf buf) {
            return new BlockPos(
                    buf.readInt(),
                    buf.readInt(),
                    buf.readInt()
            );
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, BlockPos pos) {
            buf.writeInt(pos.getX());
            buf.writeInt(pos.getY());
            buf.writeInt(pos.getZ());
        }
    };

    public static NetworkCodec<FDColor> COLOR = new NetworkCodec<FDColor>() {
        @Override
        public FDColor fromNetwork(FriendlyByteBuf buf) {
            return FDColor.decode(buf.readInt());
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, FDColor color) {
            buf.writeInt(color.encode());
        }
    };


    public static <C, T1> NetworkCodec<C> composite(
            NetworkCodec<T1> c1,
            Function<C, T1> f1,
            Function<T1, C> constructor
    ) {
        return new NetworkCodec<C>() {
            @Override
            public C fromNetwork(FriendlyByteBuf p_330310_) {
                T1 t1 = c1.fromNetwork(p_330310_);
                return constructor.apply(t1);
            }

            @Override
            public void toNetwork(FriendlyByteBuf buf, C p_331912_) {
                c1.toNetwork(buf, f1.apply(p_331912_));
            }
        };
    }


    public static <C, T1, T2> NetworkCodec<C> composite(
            NetworkCodec<T1> c1,
            Function<C, T1> f1,
            NetworkCodec<T2> c2,
            Function<C, T2> f2,
            BiFunction<T1, T2, C> constructor
    ) {
        return new NetworkCodec<C>() {
            @Override
            public C fromNetwork(FriendlyByteBuf p_330310_) {
                T1 t1 = c1.fromNetwork(p_330310_);
                T2 t2 = c2.fromNetwork(p_330310_);
                return constructor.apply(t1, t2);
            }

            @Override
            public void toNetwork(FriendlyByteBuf buf, C p_331912_) {
                c1.toNetwork(buf, f1.apply(p_331912_));
                c2.toNetwork(buf, f2.apply(p_331912_));
            }
        };
    }

    public static <C, T1, T2, T3> NetworkCodec<C> composite(
            NetworkCodec<T1> c1,
            Function<C, T1> f1,
            NetworkCodec<T2> c2,
            Function<C, T2> f2,
            NetworkCodec<T3> c3,
            Function<C, T3> f3,
            Function3<T1, T2, T3, C> constructor
    ) {
        return new NetworkCodec<C>() {
            @Override
            public C fromNetwork(FriendlyByteBuf p_330310_) {
                T1 t1 = c1.fromNetwork(p_330310_);
                T2 t2 = c2.fromNetwork(p_330310_);
                T3 t3 = c3.fromNetwork(p_330310_);
                return constructor.apply(t1, t2, t3);
            }

            @Override
            public void toNetwork(FriendlyByteBuf buf, C p_331912_) {
                c1.toNetwork(buf, f1.apply(p_331912_));
                c2.toNetwork(buf, f2.apply(p_331912_));
                c3.toNetwork(buf, f3.apply(p_331912_));
            }
        };
    }

    public static <C, T1, T2, T3, T4> NetworkCodec<C> composite(
            NetworkCodec<T1> c1,
            Function<C, T1> f1,
            NetworkCodec<T2> c2,
            Function<C, T2> f2,
            NetworkCodec<T3> c3,
            Function<C, T3> f3,
            NetworkCodec<T4> c4,
            Function<C, T4> f4,
            Function4<T1, T2, T3, T4, C> constructor
    ) {
        return new NetworkCodec<C>() {
            @Override
            public C fromNetwork(FriendlyByteBuf p_330310_) {
                T1 t1 = c1.fromNetwork(p_330310_);
                T2 t2 = c2.fromNetwork(p_330310_);
                T3 t3 = c3.fromNetwork(p_330310_);
                T4 t4 = c4.fromNetwork(p_330310_);
                return constructor.apply(t1, t2, t3, t4);
            }

            @Override
            public void toNetwork(FriendlyByteBuf buf, C p_331912_) {
                c1.toNetwork(buf, f1.apply(p_331912_));
                c2.toNetwork(buf, f2.apply(p_331912_));
                c3.toNetwork(buf, f3.apply(p_331912_));
                c4.toNetwork(buf, f4.apply(p_331912_));
            }
        };
    }



    public static <C, T1, T2, T3, T4, T5> NetworkCodec<C> composite(
            NetworkCodec<T1> c1,
            Function<C, T1> f1,
            NetworkCodec<T2> c2,
            Function<C, T2> f2,
            NetworkCodec<T3> c3,
            Function<C, T3> f3,
            NetworkCodec<T4> c4,
            Function<C, T4> f4,
            NetworkCodec<T5> c5,
            Function<C, T5> f5,
            Function5<T1, T2, T3, T4, T5, C> constructor
    ) {
        return new NetworkCodec<C>() {
            @Override
            public C fromNetwork(FriendlyByteBuf p_330310_) {
                T1 t1 = c1.fromNetwork(p_330310_);
                T2 t2 = c2.fromNetwork(p_330310_);
                T3 t3 = c3.fromNetwork(p_330310_);
                T4 t4 = c4.fromNetwork(p_330310_);
                T5 t5 = c5.fromNetwork(p_330310_);
                return constructor.apply(t1, t2, t3, t4, t5);
            }

            @Override
            public void toNetwork(FriendlyByteBuf buf, C p_331912_) {
                c1.toNetwork(buf, f1.apply(p_331912_));
                c2.toNetwork(buf, f2.apply(p_331912_));
                c3.toNetwork(buf, f3.apply(p_331912_));
                c4.toNetwork(buf, f4.apply(p_331912_));
                c5.toNetwork(buf, f5.apply(p_331912_));
            }
        };
    }


    public static <C, T1, T2, T3, T4, T5, T6> NetworkCodec<C> composite(
            NetworkCodec<T1> c1,
            Function<C, T1> f1,
            NetworkCodec<T2> c2,
            Function<C, T2> f2,
            NetworkCodec<T3> c3,
            Function<C, T3> f3,
            NetworkCodec<T4> c4,
            Function<C, T4> f4,
            NetworkCodec<T5> c5,
            Function<C, T5> f5,
            NetworkCodec<T6> c6,
            Function<C, T6> f6,
            Function6<T1, T2, T3, T4, T5, T6, C> constructor
    ) {
        return new NetworkCodec<C>() {
            @Override
            public C fromNetwork(FriendlyByteBuf p_330310_) {
                T1 t1 = c1.fromNetwork(p_330310_);
                T2 t2 = c2.fromNetwork(p_330310_);
                T3 t3 = c3.fromNetwork(p_330310_);
                T4 t4 = c4.fromNetwork(p_330310_);
                T5 t5 = c5.fromNetwork(p_330310_);
                T6 t6 = c6.fromNetwork(p_330310_);
                return constructor.apply(t1, t2, t3, t4, t5, t6);
            }

            @Override
            public void toNetwork(FriendlyByteBuf buf, C p_331912_) {
                c1.toNetwork(buf, f1.apply(p_331912_));
                c2.toNetwork(buf, f2.apply(p_331912_));
                c3.toNetwork(buf, f3.apply(p_331912_));
                c4.toNetwork(buf, f4.apply(p_331912_));
                c5.toNetwork(buf, f5.apply(p_331912_));
                c6.toNetwork(buf, f6.apply(p_331912_));
            }
        };
    }




    public static <C, T1, T2, T3, T4, T5, T6, T7> NetworkCodec<C> composite(
            NetworkCodec<T1> c1,
            Function<C, T1> f1,
            NetworkCodec<T2> c2,
            Function<C, T2> f2,
            NetworkCodec<T3> c3,
            Function<C, T3> f3,
            NetworkCodec<T4> c4,
            Function<C, T4> f4,
            NetworkCodec<T5> c5,
            Function<C, T5> f5,
            NetworkCodec<T6> c6,
            Function<C, T6> f6,
            NetworkCodec<T7> c7,
            Function<C, T7> f7,
            Function7<T1, T2, T3, T4, T5, T6, T7, C> constructor
    ) {
        return new NetworkCodec<C>() {
            @Override
            public C fromNetwork(FriendlyByteBuf p_330310_) {
                T1 t1 = c1.fromNetwork(p_330310_);
                T2 t2 = c2.fromNetwork(p_330310_);
                T3 t3 = c3.fromNetwork(p_330310_);
                T4 t4 = c4.fromNetwork(p_330310_);
                T5 t5 = c5.fromNetwork(p_330310_);
                T6 t6 = c6.fromNetwork(p_330310_);
                T7 t7 = c7.fromNetwork(p_330310_);
                return constructor.apply(t1, t2, t3, t4, t5, t6,t7);
            }

            @Override
            public void toNetwork(FriendlyByteBuf buf, C p_331912_) {
                c1.toNetwork(buf, f1.apply(p_331912_));
                c2.toNetwork(buf, f2.apply(p_331912_));
                c3.toNetwork(buf, f3.apply(p_331912_));
                c4.toNetwork(buf, f4.apply(p_331912_));
                c5.toNetwork(buf, f5.apply(p_331912_));
                c6.toNetwork(buf, f6.apply(p_331912_));
                c7.toNetwork(buf, f7.apply(p_331912_));
            }
        };
    }

    public static <C, T1, T2, T3, T4, T5, T6, T7, T8> NetworkCodec<C> composite(
            NetworkCodec<T1> c1,
            Function<C, T1> f1,
            NetworkCodec<T2> c2,
            Function<C, T2> f2,
            NetworkCodec<T3> c3,
            Function<C, T3> f3,
            NetworkCodec<T4> c4,
            Function<C, T4> f4,
            NetworkCodec<T5> c5,
            Function<C, T5> f5,
            NetworkCodec<T6> c6,
            Function<C, T6> f6,
            NetworkCodec<T7> c7,
            Function<C, T7> f7,
            NetworkCodec<T8> c8,
            Function<C, T8> f8,
            Function8<T1, T2, T3, T4, T5, T6, T7,T8, C> constructor
    ) {
        return new NetworkCodec<C>() {
            @Override
            public C fromNetwork(FriendlyByteBuf p_330310_) {
                T1 t1 = c1.fromNetwork(p_330310_);
                T2 t2 = c2.fromNetwork(p_330310_);
                T3 t3 = c3.fromNetwork(p_330310_);
                T4 t4 = c4.fromNetwork(p_330310_);
                T5 t5 = c5.fromNetwork(p_330310_);
                T6 t6 = c6.fromNetwork(p_330310_);
                T7 t7 = c7.fromNetwork(p_330310_);
                T8 t8 = c8.fromNetwork(p_330310_);
                return constructor.apply(t1, t2, t3, t4, t5, t6,t7,t8);
            }

            @Override
            public void toNetwork(FriendlyByteBuf buf, C p_331912_) {
                c1.toNetwork(buf, f1.apply(p_331912_));
                c2.toNetwork(buf, f2.apply(p_331912_));
                c3.toNetwork(buf, f3.apply(p_331912_));
                c4.toNetwork(buf, f4.apply(p_331912_));
                c5.toNetwork(buf, f5.apply(p_331912_));
                c6.toNetwork(buf, f6.apply(p_331912_));
                c7.toNetwork(buf, f7.apply(p_331912_));
                c8.toNetwork(buf, f8.apply(p_331912_));
            }
        };
    }

    public static <C, T1, T2, T3, T4, T5, T6, T7, T8, T9> NetworkCodec<C> composite(
            NetworkCodec<T1> c1,
            Function<C, T1> f1,
            NetworkCodec<T2> c2,
            Function<C, T2> f2,
            NetworkCodec<T3> c3,
            Function<C, T3> f3,
            NetworkCodec<T4> c4,
            Function<C, T4> f4,
            NetworkCodec<T5> c5,
            Function<C, T5> f5,
            NetworkCodec<T6> c6,
            Function<C, T6> f6,
            NetworkCodec<T7> c7,
            Function<C, T7> f7,
            NetworkCodec<T8> c8,
            Function<C, T8> f8,
            NetworkCodec<T9> c9,
            Function<C, T9> f9,
            Function9<T1, T2, T3, T4, T5, T6, T7,T8,T9 ,C> constructor
    ) {
        return new NetworkCodec<C>() {
            @Override
            public C fromNetwork(FriendlyByteBuf p_330310_) {
                T1 t1 = c1.fromNetwork(p_330310_);
                T2 t2 = c2.fromNetwork(p_330310_);
                T3 t3 = c3.fromNetwork(p_330310_);
                T4 t4 = c4.fromNetwork(p_330310_);
                T5 t5 = c5.fromNetwork(p_330310_);
                T6 t6 = c6.fromNetwork(p_330310_);
                T7 t7 = c7.fromNetwork(p_330310_);
                T8 t8 = c8.fromNetwork(p_330310_);
                T9 t9 = c9.fromNetwork(p_330310_);
                return constructor.apply(t1, t2, t3, t4, t5, t6,t7,t8,t9);
            }

            @Override
            public void toNetwork(FriendlyByteBuf buf, C p_331912_) {
                c1.toNetwork(buf, f1.apply(p_331912_));
                c2.toNetwork(buf, f2.apply(p_331912_));
                c3.toNetwork(buf, f3.apply(p_331912_));
                c4.toNetwork(buf, f4.apply(p_331912_));
                c5.toNetwork(buf, f5.apply(p_331912_));
                c6.toNetwork(buf, f6.apply(p_331912_));
                c7.toNetwork(buf, f7.apply(p_331912_));
                c8.toNetwork(buf, f8.apply(p_331912_));
                c9.toNetwork(buf, f9.apply(p_331912_));
            }
        };
    }

    public static <C, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> NetworkCodec<C> composite(
            NetworkCodec<T1> c1,
            Function<C, T1> f1,
            NetworkCodec<T2> c2,
            Function<C, T2> f2,
            NetworkCodec<T3> c3,
            Function<C, T3> f3,
            NetworkCodec<T4> c4,
            Function<C, T4> f4,
            NetworkCodec<T5> c5,
            Function<C, T5> f5,
            NetworkCodec<T6> c6,
            Function<C, T6> f6,
            NetworkCodec<T7> c7,
            Function<C, T7> f7,
            NetworkCodec<T8> c8,
            Function<C, T8> f8,
            NetworkCodec<T9> c9,
            Function<C, T9> f9,
            NetworkCodec<T10> c10,
            Function<C, T10> f10,
            Function10<T1, T2, T3, T4, T5, T6, T7,T8,T9,T10,C> constructor
    ) {
        return new NetworkCodec<C>() {
            @Override
            public C fromNetwork(FriendlyByteBuf p_330310_) {
                T1 t1 = c1.fromNetwork(p_330310_);
                T2 t2 = c2.fromNetwork(p_330310_);
                T3 t3 = c3.fromNetwork(p_330310_);
                T4 t4 = c4.fromNetwork(p_330310_);
                T5 t5 = c5.fromNetwork(p_330310_);
                T6 t6 = c6.fromNetwork(p_330310_);
                T7 t7 = c7.fromNetwork(p_330310_);
                T8 t8 = c8.fromNetwork(p_330310_);
                T9 t9 = c9.fromNetwork(p_330310_);
                T10 t10 = c10.fromNetwork(p_330310_);
                return constructor.apply(t1, t2, t3, t4, t5, t6,t7,t8,t9,t10);
            }

            @Override
            public void toNetwork(FriendlyByteBuf buf, C p_331912_) {
                c1.toNetwork(buf, f1.apply(p_331912_));
                c2.toNetwork(buf, f2.apply(p_331912_));
                c3.toNetwork(buf, f3.apply(p_331912_));
                c4.toNetwork(buf, f4.apply(p_331912_));
                c5.toNetwork(buf, f5.apply(p_331912_));
                c6.toNetwork(buf, f6.apply(p_331912_));
                c7.toNetwork(buf, f7.apply(p_331912_));
                c8.toNetwork(buf, f8.apply(p_331912_));
                c9.toNetwork(buf, f9.apply(p_331912_));
                c10.toNetwork(buf, f10.apply(p_331912_));
            }
        };
    }

    public static <C, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10,T11> NetworkCodec<C> composite(
            NetworkCodec<T1> c1,
            Function<C, T1> f1,
            NetworkCodec<T2> c2,
            Function<C, T2> f2,
            NetworkCodec<T3> c3,
            Function<C, T3> f3,
            NetworkCodec<T4> c4,
            Function<C, T4> f4,
            NetworkCodec<T5> c5,
            Function<C, T5> f5,
            NetworkCodec<T6> c6,
            Function<C, T6> f6,
            NetworkCodec<T7> c7,
            Function<C, T7> f7,
            NetworkCodec<T8> c8,
            Function<C, T8> f8,
            NetworkCodec<T9> c9,
            Function<C, T9> f9,
            NetworkCodec<T10> c10,
            Function<C, T10> f10,
            NetworkCodec<T11> c11,
            Function<C, T11> f11,
            Function11<T1, T2, T3, T4, T5, T6, T7,T8,T9,T10,T11,C> constructor
    ) {
        return new NetworkCodec<C>() {
            @Override
            public C fromNetwork(FriendlyByteBuf p_330310_) {
                T1 t1 = c1.fromNetwork(p_330310_);
                T2 t2 = c2.fromNetwork(p_330310_);
                T3 t3 = c3.fromNetwork(p_330310_);
                T4 t4 = c4.fromNetwork(p_330310_);
                T5 t5 = c5.fromNetwork(p_330310_);
                T6 t6 = c6.fromNetwork(p_330310_);
                T7 t7 = c7.fromNetwork(p_330310_);
                T8 t8 = c8.fromNetwork(p_330310_);
                T9 t9 = c9.fromNetwork(p_330310_);
                T10 t10 = c10.fromNetwork(p_330310_);
                T11 t11 = c11.fromNetwork(p_330310_);
                return constructor.apply(t1, t2, t3, t4, t5, t6,t7,t8,t9,t10,t11);
            }

            @Override
            public void toNetwork(FriendlyByteBuf buf, C p_331912_) {
                c1.toNetwork(buf, f1.apply(p_331912_));
                c2.toNetwork(buf, f2.apply(p_331912_));
                c3.toNetwork(buf, f3.apply(p_331912_));
                c4.toNetwork(buf, f4.apply(p_331912_));
                c5.toNetwork(buf, f5.apply(p_331912_));
                c6.toNetwork(buf, f6.apply(p_331912_));
                c7.toNetwork(buf, f7.apply(p_331912_));
                c8.toNetwork(buf, f8.apply(p_331912_));
                c9.toNetwork(buf, f9.apply(p_331912_));
                c10.toNetwork(buf, f10.apply(p_331912_));
                c11.toNetwork(buf, f11.apply(p_331912_));
            }
        };
    }

    public static <C, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10,T11,T12> NetworkCodec<C> composite(
            NetworkCodec<T1> c1,
            Function<C, T1> f1,
            NetworkCodec<T2> c2,
            Function<C, T2> f2,
            NetworkCodec<T3> c3,
            Function<C, T3> f3,
            NetworkCodec<T4> c4,
            Function<C, T4> f4,
            NetworkCodec<T5> c5,
            Function<C, T5> f5,
            NetworkCodec<T6> c6,
            Function<C, T6> f6,
            NetworkCodec<T7> c7,
            Function<C, T7> f7,
            NetworkCodec<T8> c8,
            Function<C, T8> f8,
            NetworkCodec<T9> c9,
            Function<C, T9> f9,
            NetworkCodec<T10> c10,
            Function<C, T10> f10,
            NetworkCodec<T11> c11,
            Function<C, T11> f11,
            NetworkCodec<T12> c12,
            Function<C, T12> f12,
            Function12<T1, T2, T3, T4, T5, T6, T7,T8,T9,T10,T11,T12,C> constructor
    ) {
        return new NetworkCodec<C>() {
            @Override
            public C fromNetwork(FriendlyByteBuf p_330310_) {
                T1 t1 = c1.fromNetwork(p_330310_);
                T2 t2 = c2.fromNetwork(p_330310_);
                T3 t3 = c3.fromNetwork(p_330310_);
                T4 t4 = c4.fromNetwork(p_330310_);
                T5 t5 = c5.fromNetwork(p_330310_);
                T6 t6 = c6.fromNetwork(p_330310_);
                T7 t7 = c7.fromNetwork(p_330310_);
                T8 t8 = c8.fromNetwork(p_330310_);
                T9 t9 = c9.fromNetwork(p_330310_);
                T10 t10 = c10.fromNetwork(p_330310_);
                T11 t11 = c11.fromNetwork(p_330310_);
                T12 t12 = c12.fromNetwork(p_330310_);
                return constructor.apply(t1, t2, t3, t4, t5, t6,t7,t8,t9,t10,t11,t12);
            }

            @Override
            public void toNetwork(FriendlyByteBuf buf, C p_331912_) {
                c1.toNetwork(buf, f1.apply(p_331912_));
                c2.toNetwork(buf, f2.apply(p_331912_));
                c3.toNetwork(buf, f3.apply(p_331912_));
                c4.toNetwork(buf, f4.apply(p_331912_));
                c5.toNetwork(buf, f5.apply(p_331912_));
                c6.toNetwork(buf, f6.apply(p_331912_));
                c7.toNetwork(buf, f7.apply(p_331912_));
                c8.toNetwork(buf, f8.apply(p_331912_));
                c9.toNetwork(buf, f9.apply(p_331912_));
                c10.toNetwork(buf, f10.apply(p_331912_));
                c11.toNetwork(buf, f11.apply(p_331912_));
                c12.toNetwork(buf, f12.apply(p_331912_));
            }
        };
    }

}
