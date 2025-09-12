package com.finderfeed.fdlib.systems.impact_frames;

import com.finderfeed.fdlib.systems.stream_codecs.NetworkCodec;

public class ImpactFrame {

    public static final NetworkCodec<ImpactFrame> STREAM_CODEC = NetworkCodec.composite(
            NetworkCodec.FLOAT,v->v.treshhold,
            NetworkCodec.FLOAT,v->v.treshholdLerp,
            NetworkCodec.INT,v->v.duration,
            NetworkCodec.BOOL,v->v.invert,
            ImpactFrame::new
    );

    private float treshhold;
    private float treshholdLerp;
    private int duration;
    private boolean invert;

    public ImpactFrame(float treshhold, float treshholdLerp,int duration,boolean invert) {
        this.treshhold = treshhold;
        this.treshholdLerp = treshholdLerp;
        this.invert = invert;
        this.duration = duration;
    }

    public ImpactFrame(ImpactFrame other){
        this(other.treshhold,other.treshholdLerp,other.duration,other.invert);
    }

    public ImpactFrame(){
        this(0.6f,0.05f,false);
    }



    public ImpactFrame(float treshhold, float treshholdLerp,boolean invert) {
        this(treshhold,treshholdLerp,1,invert);
    }

    public ImpactFrame(float treshhold) {
        this(treshhold,0.05f,false);
    }

    public ImpactFrame(boolean invert) {
        this(0.5f,0.05f,invert);
    }


    public boolean isInverted() {
        return invert;
    }

    public float getTreshhold() {
        return treshhold;
    }

    public float getTreshholdLerp() {
        return treshholdLerp;
    }

    public int getDuration() {
        return duration;
    }

    public ImpactFrame setInverted(boolean invert) {
        this.invert = invert;
        return this;
    }

    public ImpactFrame setTreshhold(float treshhold) {
        this.treshhold = treshhold;
        return this;
    }

    public ImpactFrame setTreshholdLerp(float treshholdLerp) {
        this.treshholdLerp = treshholdLerp;
        return this;
    }

    public ImpactFrame setDuration(int duration) {
        this.duration = duration;
        return this;
    }
}

