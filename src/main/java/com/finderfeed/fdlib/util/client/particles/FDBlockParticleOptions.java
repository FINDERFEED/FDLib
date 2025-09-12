package com.finderfeed.fdlib.util.client.particles;

import com.finderfeed.fdlib.init.FDParticles;
import com.finderfeed.fdlib.systems.stream_codecs.NetworkCodec;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class FDBlockParticleOptions implements ParticleOptions {

    public static ParticleOptions.Deserializer<FDBlockParticleOptions> DESERIALIZER = new Deserializer<FDBlockParticleOptions>() {
        @Override
        public FDBlockParticleOptions fromCommand(ParticleType<FDBlockParticleOptions> p_123733_, StringReader p_123734_) throws CommandSyntaxException {
            return new FDBlockParticleOptions(Blocks.MAGMA_BLOCK.defaultBlockState(), 10, 1f);
        }

        @Override
        public FDBlockParticleOptions fromNetwork(ParticleType<FDBlockParticleOptions> p_123735_, FriendlyByteBuf p_123736_) {
            return STREAM_CODEC.fromNetwork(p_123736_);
        }
    };

    public static Codec<FDBlockParticleOptions> CODEC = RecordCodecBuilder.create(p->p.group(
                    BlockState.CODEC.fieldOf("state").forGetter(opt->opt.state),
                    Codec.INT.fieldOf("lifetime").forGetter(opt->opt.lifetime),
                    Codec.FLOAT.fieldOf("state").forGetter(opt->opt.quadSizeMultiplier)
            ).apply(p,FDBlockParticleOptions::new)
    );
    public static MapCodec<FDBlockParticleOptions> MAP_CODEC = CODEC.xmap(a->a,b->b).fieldOf("options");

    public static NetworkCodec<FDBlockParticleOptions> STREAM_CODEC = NetworkCodec.composite(
            NetworkCodec.idMapper(Block.BLOCK_STATE_REGISTRY),t->t.state,
            NetworkCodec.INT,t->t.lifetime,
            NetworkCodec.FLOAT,t->t.quadSizeMultiplier,
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

    @Override
    public void writeToNetwork(FriendlyByteBuf p_123732_) {
        STREAM_CODEC.toNetwork(p_123732_, this);
    }

    @Override
    public String writeToString() {
        return "";
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
