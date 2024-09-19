package com.finderfeed.fdlib.to_other_mod.client.particles.arc_lightning;

import com.finderfeed.fdlib.util.FDByteBufCodecs;
import com.finderfeed.fdlib.util.FDCodecs;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

public class ArcLightningOptions implements ParticleOptions {

    private static final Codec<ArcLightningOptions> CODEC = RecordCodecBuilder.create(p->p.group(
            FDCodecs.VEC3.fieldOf("end").forGetter(v->v.end),
            Codec.INT.fieldOf("lifetime").forGetter(v->v.lifetime),
            Codec.FLOAT.fieldOf("lightningWidth").forGetter(v->v.lightningWidth),
            Codec.FLOAT.fieldOf("r").forGetter(v->v.r),
            Codec.FLOAT.fieldOf("g").forGetter(v->v.g),
            Codec.FLOAT.fieldOf("b").forGetter(v->v.b)
            ).apply(p,ArcLightningOptions::new)
    );

    private static final StreamCodec<FriendlyByteBuf,ArcLightningOptions> STREAM_CODEC = StreamCodec.composite(
            FDByteBufCodecs.VEC3, v->v.end,
            ByteBufCodecs.INT,v->v.lifetime,
            ByteBufCodecs.FLOAT,v->v.lightningWidth,
            ByteBufCodecs.FLOAT,v->v.r,
            ByteBufCodecs.FLOAT,v->v.g,
            ByteBufCodecs.FLOAT,v->v.b,
            ArcLightningOptions::new
    );

    public static MapCodec<ArcLightningOptions> createCodec(ParticleType<?> type){
        return CODEC.xmap(a->new ArcLightningOptions(type,a.end,a.lifetime,a.lightningWidth,a.r,a.g,a.b),f->f).fieldOf("options");
    }

    public static StreamCodec<FriendlyByteBuf,ArcLightningOptions> createStreamCodec(ParticleType<?> type){
        return STREAM_CODEC.map(opt->new ArcLightningOptions(type,opt.end,opt.lifetime,opt.lightningWidth,opt.r,opt.g,opt.b),opt->opt);
    }

    public ParticleType<?> particleType;
    public Vec3 end;
    public int lifetime;
    public float lightningWidth;
    public float r;
    public float g;
    public float b;

    public ArcLightningOptions(ParticleType<?> particleType, Vec3 end,int lifetime,float lightningWidth, float r, float g, float b){
        this.particleType = particleType;
        this.lightningWidth = lightningWidth;
        this.r = r;
        this.g = g;
        this.b = b;
        this.end = end;
        this.lifetime = lifetime;
    }

    public ArcLightningOptions(Vec3 end,int lifetime,float lightningWidth, float r, float g, float b){
        this(null,end,lifetime,lightningWidth,r,g,b);
    }

    @Override
    public ParticleType<?> getType() {
        return particleType;
    }
}
