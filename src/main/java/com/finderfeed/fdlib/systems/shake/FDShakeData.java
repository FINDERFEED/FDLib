package com.finderfeed.fdlib.systems.shake;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.NetworkCodec;
import net.minecraft.network.codec.NetworkCodec;

public class FDShakeData {

    public static final NetworkCodec<FDShakeData> STREAM_CODEC = NetworkCodec.composite(
            NetworkCodec.INT,v->v.inTime,
            NetworkCodec.INT,v->v.stayTime,
            NetworkCodec.INT,v->v.outTime,
            NetworkCodec.FLOAT,v->v.amplitude,
            NetworkCodec.FLOAT,v->v.frequency,
            (inTime,stayTime,outTime,amplitude,frequency)->{
                FDShakeData data = new FDShakeData();
                data.inTime = inTime;
                data.stayTime = stayTime;
                data.outTime = outTime;
                data.amplitude = amplitude;
                data.frequency = frequency;
                return data;
            }
    );

    private int inTime = 2;
    private int stayTime = 2;
    private int outTime = 2;
    private float amplitude = 0.1f;
    private float frequency = 1;



    public static Builder builder(){
        return new Builder();
    }

    public float getAmplitude() {
        return amplitude;
    }

    public int getOutTime() {
        return outTime;
    }

    public int getStayTime() {
        return stayTime;
    }

    public int getInTime() {
        return inTime;
    }

    public int duration(){
        return outTime + inTime + stayTime;
    }

    public float getFrequency() {
        return frequency;
    }

    public static class Builder {

        private FDShakeData data = new FDShakeData();

        public Builder inTime(int inTime){
            data.inTime = inTime;
            return this;
        }

        public Builder outTime(int outTime){
            data.outTime = outTime;
            return this;
        }

        public Builder stayTime(int stayTime){
            data.stayTime = stayTime;
            return this;
        }

        public Builder amplitude(float amplitude){
            data.amplitude = amplitude;
            return this;
        }
        public Builder frequency(float frequency){
            data.frequency = frequency;
            return this;
        }

        public FDShakeData build(){
            return data;
        }


    }


}

