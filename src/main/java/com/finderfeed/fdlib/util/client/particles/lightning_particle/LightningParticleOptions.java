package com.finderfeed.fdlib.util.client.particles.lightning_particle;

import com.finderfeed.fdlib.init.FDParticles;
import com.finderfeed.fdlib.systems.particle.EmptyParticleProcessor;
import com.finderfeed.fdlib.systems.particle.ParticleProcessor;
import com.finderfeed.fdlib.systems.stream_codecs.NetworkCodec;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;

public class LightningParticleOptions implements ParticleOptions {

    public static final Deserializer<LightningParticleOptions> DESERIALIZER = new Deserializer<LightningParticleOptions>() {
        @Override
        public LightningParticleOptions fromCommand(ParticleType<LightningParticleOptions> p_123733_, StringReader p_123734_) throws CommandSyntaxException {
            return LightningParticleOptions.builder().build();
        }

        @Override
        public LightningParticleOptions fromNetwork(ParticleType<LightningParticleOptions> p_123735_, FriendlyByteBuf p_123736_) {
            return STREAM_CODEC.fromNetwork(p_123736_);
        }
    };

    public static final Codec<LightningParticleOptions> CODEC = RecordCodecBuilder.create(builder->
            builder.group(
                    ParticleProcessor.CODEC.fieldOf("processor").forGetter(p->p.processor),
                    Codec.FLOAT.fieldOf("quadSize").forGetter(p->p.quadSize),
                    Codec.INT.fieldOf("r").forGetter(p->p.r),
                    Codec.INT.fieldOf("g").forGetter(p->p.g),
                    Codec.INT.fieldOf("b").forGetter(p->p.b),
                    Codec.INT.fieldOf("seed").forGetter(p->p.seed),
                    Codec.INT.fieldOf("lifetime").forGetter(p->p.lifetime),
                    Codec.BOOL.fieldOf("hasPhysics").forGetter(p->p.hasPhysics),
                    Codec.BOOL.fieldOf("randomRoll").forGetter(p->p.randomRoll),
                    Codec.INT.fieldOf("maxLightningLength").forGetter(p->p.maxLightningSegments)
            ).apply(builder,LightningParticleOptions::new)
    );

    public static final NetworkCodec<LightningParticleOptions> STREAM_CODEC = NetworkCodec.composite(
            ParticleProcessor.STREAM_CODEC,v->v.processor,
            NetworkCodec.FLOAT,v->v.quadSize,
            NetworkCodec.INT,v->v.r,
            NetworkCodec.INT,v->v.g,
            NetworkCodec.INT,v->v.b,
            NetworkCodec.INT,v->v.seed,
            NetworkCodec.INT,v->v.lifetime,
            NetworkCodec.BOOL,v->v.hasPhysics,
            NetworkCodec.BOOL,v->v.randomRoll,
            NetworkCodec.INT,v->v.maxLightningSegments,
            LightningParticleOptions::new
    );

    private ParticleProcessor<?> processor;
    private float quadSize = 0.2f;
    private int r = 255;
    private int g = 0;
    private int b = 0;

    private int seed = -1;
    private int lifetime = 60;
    private int maxLightningSegments = 4;

    private boolean hasPhysics = false;
    private boolean randomRoll = false;

    public LightningParticleOptions(ParticleProcessor<?> particleProcessor, float quadSize, int r, int g, int b,int seed,int lifetime,boolean hasPhysics, boolean randomRoll, int maxLightningSegments) {
        this.quadSize = quadSize;
        this.r = r;
        this.g = g;
        this.b = b;
        this.seed = seed;
        this.lifetime = lifetime;
        this.randomRoll = randomRoll;
        this.maxLightningSegments = maxLightningSegments;
        this.processor = particleProcessor;
    }

    public LightningParticleOptions(Builder builder) {
        this(builder.particleProcessor, builder.quadSize, builder.r, builder.g, builder.b, builder.seed, builder.lifetime, builder.hasPhysics,builder.randomRoll,builder.maxLightningSegments);
    }

    public ParticleProcessor<?> getProcessor() {
        return processor;
    }

    public int getMaxLightningSegments() {
        return maxLightningSegments;
    }

    public boolean isRandomRoll() {
        return randomRoll;
    }

    public int getR() {
        return r;
    }

    public int getG() {
        return g;
    }

    public int getB() {
        return b;
    }

    public float getQuadSize() {
        return quadSize;
    }

    public int getSeed() {
        return seed;
    }

    public int getLifetime() {
        return lifetime;
    }

    public boolean hasPhysics() {
        return hasPhysics;
    }


    @Override
    public ParticleType<?> getType() {
        return FDParticles.LIGHTNING_PARTICLE.get();
    }

    @Override
    public void writeToNetwork(FriendlyByteBuf buf) {
        STREAM_CODEC.toNetwork(buf,this);
    }

    @Override
    public String writeToString() {
        return "";
    }

    public static Builder builder(){
        return new Builder();
    }

    public static class Builder{

        private ParticleProcessor<?> particleProcessor = new EmptyParticleProcessor();
        private float quadSize = 0.2f;
        private int r = 255;
        private int g = 0;
        private int b = 0;

        private int seed = -1;
        private int lifetime = 60;
        private int maxLightningSegments = 4;

        private boolean hasPhysics = false;
        private boolean randomRoll = false;

        public static Builder start(){
            return new Builder();
        }

        public Builder particleProcessor(ParticleProcessor<?> particleProcessor){
            this.particleProcessor = particleProcessor;
            return this;
        }

        public Builder maxLightningSegments(int maxLightningSegments){
            this.maxLightningSegments = maxLightningSegments;
            return this;
        }

        public Builder randomRoll(boolean randomRoll){
            this.randomRoll = randomRoll;
            return this;
        }

        public Builder quadSize(float quadSize) {
            this.quadSize = quadSize;
            return this;
        }

        public Builder color(int r, int g, int b) {
            this.r = r;
            this.g = g;
            this.b = b;
            return this;
        }

        public Builder seed(int seed) {
            this.seed = seed;
            return this;
        }

        public Builder lifetime(int lifetime) {
            this.lifetime = lifetime;
            return this;
        }

        public Builder physics(boolean hasPhysics) {
            this.hasPhysics = hasPhysics;
            return this;
        }

        public LightningParticleOptions build(){
            return new LightningParticleOptions(this);
        }
    }


}
