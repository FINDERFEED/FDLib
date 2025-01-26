package com.finderfeed.fdlib.util.client.particles.options;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public class AlphaOptions {

    public static StreamCodec<FriendlyByteBuf,AlphaOptions> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT,v->v.inTime,
            ByteBufCodecs.INT,v->v.outTime,
            ByteBufCodecs.INT,v->v.stayTime,
            ByteBufCodecs.FLOAT,v->v.maxAlpha,
            (inTime,outTime,stayTime,maxAlpha)->{
                AlphaOptions options = new AlphaOptions();
                options.inTime = inTime;
                options.outTime = outTime;
                options.stayTime = stayTime;
                options.maxAlpha = maxAlpha;
                return options;
            }
    );

    public static Codec<AlphaOptions> CODEC = RecordCodecBuilder.create(p->p.group(
            Codec.INT.fieldOf("inTime").forGetter(v->v.inTime),
            Codec.INT.fieldOf("stayTime").forGetter(v->v.stayTime),
            Codec.INT.fieldOf("outTime").forGetter(v->v.outTime),
            Codec.FLOAT.fieldOf("maxAlpha").forGetter(v->v.maxAlpha)
    ).apply(p,(inTime,stayTime,outTime,maxAlpha)->{
        AlphaOptions options = new AlphaOptions();
        options.inTime = inTime;
        options.outTime = outTime;
        options.stayTime = stayTime;
        options.maxAlpha = maxAlpha;
        return options;
    }));

    public int inTime = 0;
    public int outTime = 0;
    public int stayTime = 0;
    public float maxAlpha = 1;

    public AlphaOptions(){
    }

    public boolean isEmpty(){
        return inTime == 0 && outTime == 0 && stayTime == 0;
    }

    public int fullTime(){
        return this.inTime + this.outTime + this.stayTime;
    }

    public static Builder builder(){
        return new Builder();
    }

    public static class Builder {

        private AlphaOptions alphaOptions = new AlphaOptions();

        public Builder(){}

        public Builder in(int inTime){
            this.alphaOptions.inTime = inTime;
            return this;
        }
        public Builder out(int outTime){
            this.alphaOptions.outTime = outTime;
            return this;
        }
        public Builder stay(int stayTime){
            this.alphaOptions.stayTime = stayTime;
            return this;
        }
        public Builder maxAlpha(float maxAlpha){
            this.alphaOptions.maxAlpha = maxAlpha;
            return this;
        }

        public AlphaOptions build(){
            return alphaOptions;
        }


    }

}
