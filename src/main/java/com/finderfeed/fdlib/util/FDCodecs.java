package com.finderfeed.fdlib.util;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.phys.Vec3;

public class FDCodecs {

    public static Codec<Vec3> VEC3 = RecordCodecBuilder.create(p->p.group(
            Codec.DOUBLE.fieldOf("x").forGetter(v->v.x),
            Codec.DOUBLE.fieldOf("y").forGetter(v->v.y),
            Codec.DOUBLE.fieldOf("z").forGetter(v->v.z)
            ).apply(p,Vec3::new)
    );

}
