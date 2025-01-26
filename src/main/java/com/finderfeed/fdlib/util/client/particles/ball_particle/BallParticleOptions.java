package com.finderfeed.fdlib.util.client.particles.ball_particle;

import com.finderfeed.fdlib.init.FDParticles;
import com.finderfeed.fdlib.systems.particle.EmptyParticleProcessor;
import com.finderfeed.fdlib.systems.particle.ParticleProcessor;
import com.finderfeed.fdlib.util.FDByteBufCodecs;
import com.finderfeed.fdlib.util.FDCodecs;
import com.finderfeed.fdlib.util.FDColor;
import com.finderfeed.fdlib.util.client.particles.options.AlphaOptions;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public class BallParticleOptions implements ParticleOptions {

    public static final Codec<BallParticleOptions> CODEC = RecordCodecBuilder.create(p->p.group(
            ParticleProcessor.CODEC.fieldOf("processor").forGetter(v->v.particleProcessor),
            AlphaOptions.CODEC.fieldOf("scalingOptions").forGetter(v->v.scalingOptions),
            FDCodecs.COLOR.fieldOf("color").forGetter(v->v.color),
            Codec.FLOAT.fieldOf("friction").forGetter(v->v.friction),
            Codec.BOOL.fieldOf("hasPhysics").forGetter(v->v.hasPhysics),
            Codec.FLOAT.fieldOf("size").forGetter(v->v.size)
    ).apply(p,(processor,scaling,color,friction,physics,size)->{
        BallParticleOptions ballParticleOptions = new BallParticleOptions();
        ballParticleOptions.size = size;
        ballParticleOptions.particleProcessor = processor;
        ballParticleOptions.color = color;
        ballParticleOptions.friction = friction;
        ballParticleOptions.hasPhysics = physics;
        ballParticleOptions.scalingOptions = scaling;
        return ballParticleOptions;
    }));

    public static final MapCodec<BallParticleOptions> MAP_CODEC = CODEC.fieldOf("options");

    public static final StreamCodec<FriendlyByteBuf,BallParticleOptions> STREAM_CODEC = StreamCodec.composite(
            ParticleProcessor.STREAM_CODEC,v->v.particleProcessor,
            AlphaOptions.STREAM_CODEC,v->v.scalingOptions,
            FDByteBufCodecs.COLOR,v->v.color,
            ByteBufCodecs.FLOAT,v->v.friction,
            ByteBufCodecs.BOOL,v->v.hasPhysics,
            ByteBufCodecs.FLOAT,v->v.size,
            (processor,scaling,color,friction,physics,size)->{
                BallParticleOptions ballParticleOptions = new BallParticleOptions();
                ballParticleOptions.size = size;
                ballParticleOptions.particleProcessor = processor;
                ballParticleOptions.color = color;
                ballParticleOptions.friction = friction;
                ballParticleOptions.hasPhysics = physics;
                ballParticleOptions.scalingOptions = scaling;
                return ballParticleOptions;
            }
    );


    public ParticleProcessor<?> particleProcessor = new EmptyParticleProcessor();
    public AlphaOptions scalingOptions = new AlphaOptions();
    public FDColor color = new FDColor(1f,1f,1f,1f);
    public float friction = 1f;
    public boolean hasPhysics = false;
    public float size = 0.25f;


    public static Builder builder(){
        return new Builder();
    }

    @Override
    public ParticleType<?> getType() {
        return FDParticles.BALL_PARTICLE.get();
    }

    public static class Builder {

        private BallParticleOptions options = new BallParticleOptions();

        public Builder(){}

        public Builder particleProcessor(ParticleProcessor<?> particleProcessor){
            this.options.particleProcessor = particleProcessor;
            return this;
        }

        public Builder scalingOptions(int in,int stay,int out){
            this.options.scalingOptions.stayTime = stay;
            this.options.scalingOptions.inTime = in;
            this.options.scalingOptions.outTime = out;
            return this;
        }

        public Builder in(int in){
            this.options.scalingOptions.inTime = in;
            return this;
        }

        public Builder out(int out){
            this.options.scalingOptions.outTime = out;
            return this;
        }

        public Builder stay(int stay){
            this.options.scalingOptions.stayTime = stay;
            return this;
        }

        public Builder friction(float fricton){
            this.options.friction = fricton;
            return this;
        }

        public Builder size(float size){
            this.options.size = size;
            return this;
        }

        public Builder physics(boolean physcis){
            this.options.hasPhysics = physcis;
            return this;
        }

        public Builder color(float r, float g, float b){
            options.color.r = r;
            options.color.g = g;
            options.color.b = b;
            return this;
        }
        public Builder color(int r, int g, int b){
            options.color.r = r/255f;
            options.color.g = g/255f;
            options.color.b = b/255f;
            return this;
        }

        public Builder color(float r, float g, float b, float a){
            options.color.r = r;
            options.color.g = g;
            options.color.b = b;
            options.color.a = a;
            return this;
        }

        public BallParticleOptions build(){
            return options;
        }


    }


}
