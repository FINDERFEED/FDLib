package com.finderfeed.fdlib.systems.impact_frames;

public class ImpactFrame {

    private float treshhold;
    private float treshholdLerp;
    private boolean invert;

    public ImpactFrame(ImpactFrame other){
        this(other.treshhold,other.treshholdLerp,other.invert);
    }

    public ImpactFrame(){
        this(0.6f,0.05f,false);
    }

    public ImpactFrame(float treshhold, float treshholdLerp,boolean invert) {
        this.treshhold = treshhold;
        this.treshholdLerp = treshholdLerp;
        this.invert = invert;
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
}

