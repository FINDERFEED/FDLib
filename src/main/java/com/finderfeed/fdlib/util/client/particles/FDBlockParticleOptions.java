package com.finderfeed.fdlib.util.client.particles;

import com.finderfeed.fdlib.init.FDParticles;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class FDBlockParticleOptions implements ParticleOptions {

    public static Codec<FDBlockParticleOptions> CODEC = RecordCodecBuilder.create(p->p.group(
                    BlockState.CODEC.fieldOf("state").forGetter(opt->opt.state),
                    Codec.INT.fieldOf("lifetime").forGetter(opt->opt.lifetime),
                    Codec.FLOAT.fieldOf("state").forGetter(opt->opt.quadSizeMultiplier)
            ).apply(p,FDBlockParticleOptions::new)
    );
    public static MapCodec<FDBlockParticleOptions> MAP_CODEC = CODEC.xmap(a->a,b->b).fieldOf("options");

    public static StreamCodec<FriendlyByteBuf,FDBlockParticleOptions> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.idMapper(Block.BLOCK_STATE_REGISTRY),t->t.state,
            ByteBufCodecs.INT,t->t.lifetime,
            ByteBufCodecs.FLOAT,t->t.quadSizeMultiplier,
            FDBlockParticleOptions::new
    );

    public BlockState state;
    public int lifetime;
    public float quadSizeMultiplier;

    public FDBlockParticleOptions(BlockState state,int lifetime,float quadSizeMultiplier){
        this.state = state;
        this.lifetime = lifetime;
        this.quadSizeMultiplier = quadSizeMultiplier;
    }

    @Override
    public ParticleType<?> getType() {
        return FDParticles.TERRAIN_PARTICLE.get();
    }

    public static Builder builder(){
        return new Builder();
    }

    public static class Builder{

        private FDBlockParticleOptions options = new FDBlockParticleOptions(Blocks.STONE.defaultBlockState(),60,1f);

        public Builder(){}


        public Builder quadSizeMultiplier(float mult){
            this.options.quadSizeMultiplier = mult;
            return this;
        }

        public Builder lifetime(int lifetime){
            this.options.lifetime = lifetime;
            return this;
        }

        public Builder state(Block state){
            return this.state(state.defaultBlockState());
        }

        public Builder state(BlockState state){
            this.options.state = state;
            return this;
        }

        public FDBlockParticleOptions build(){
            return options;
        }



    }
}
