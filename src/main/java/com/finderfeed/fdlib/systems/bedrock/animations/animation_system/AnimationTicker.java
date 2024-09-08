package com.finderfeed.fdlib.systems.bedrock.animations.animation_system;

import com.finderfeed.fdlib.systems.FDRegistries;
import com.finderfeed.fdlib.systems.bedrock.animations.Animation;
import com.finderfeed.fdlib.systems.bedrock.animations.AnimationContext;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

import java.util.Objects;

public class AnimationTicker {

    public static final StreamCodec<FriendlyByteBuf,AnimationTicker> NETWORK_CODEC = StreamCodec.composite(
            ByteBufCodecs.FLOAT,ticker->ticker.elapsedTime,
            ByteBufCodecs.FLOAT,ticker->ticker.speedModifier,
            ByteBufCodecs.INT,ticker->ticker.toNullTransitionTime,
            ByteBufCodecs.STRING_UTF8,ticker->ticker.loopMode.name(),
            ByteBufCodecs.STRING_UTF8,ticker->ticker.animation.getName().toString(),
            (elapsedTime,speedModifier,toNull,loopModeName,animationName)->{
                ResourceLocation location = ResourceLocation.tryParse(animationName);
                Animation animation = FDRegistries.ANIMATIONS.get(location);
                if (animation == null){
                    throw new RuntimeException("Unknown animation received from server: " + animationName);
                }
                Animation.LoopMode mode = Animation.LoopMode.valueOf(loopModeName);
                AnimationTicker ticker = new AnimationTicker(animation);
                ticker.loopMode = mode;
                ticker.elapsedTime = elapsedTime;
                ticker.speedModifier = speedModifier;
                ticker.toNullTransitionTime = toNull;
                return ticker;
            });
    private float elapsedTime = 0;
    private float speedModifier = 1;
    private int toNullTransitionTime;
    private Animation.LoopMode loopMode;
    private Animation animation;


    public AnimationTicker(AnimationTicker other){
        this.elapsedTime = other.getElapsedTime();
        this.speedModifier = other.speedModifier;
        this.toNullTransitionTime = other.toNullTransitionTime;
        this.loopMode = other.loopMode;
        this.animation = other.getAnimation();
    }

    public AnimationTicker(Animation animation){
        this.animation = animation;
        this.toNullTransitionTime = 0;
        this.loopMode = animation.getDefaultLoopMode();
    }

    public void tick(){
        elapsedTime = Mth.clamp(elapsedTime + speedModifier,0,animation.getAnimTime());
    }

    public void resetTime(){
        elapsedTime = 0;
    }

    public void addVariables(AnimationContext context,float partialTicks){
        context.variables.put("query.anim_time",(elapsedTime + partialTicks * this.speedModifier) / 20f);
        context.variables.put("math.pi",(float) Math.PI);
    }

    public boolean hasEnded(){
        return elapsedTime == animation.getAnimTime();
    }

    public Animation getAnimation() {
        return animation;
    }

    public float getElapsedTime() {
        return elapsedTime;
    }

    public float getSpeedModifier() {
        return speedModifier;
    }

    public int getToNullTransitionTime() {
        return toNullTransitionTime;
    }

    public Animation.LoopMode getLoopMode() {
        return loopMode;
    }

    public void setAnimation(Animation animation) {
        this.animation = animation;
    }

    public void setSpeedModifier(float speedModifier) {
        this.speedModifier = speedModifier;
    }

    public void setLoopMode(Animation.LoopMode loopMode) {
        this.loopMode = loopMode;
    }

    public void setToNullTransitionTime(int toNullTransitionTime) {
        this.toNullTransitionTime = toNullTransitionTime;
    }

    public static Builder builder(Animation animation){
        return new Builder(animation);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AnimationTicker that = (AnimationTicker) o;
        boolean a1 = this.animation.equals(that.animation);
        boolean a2 = that.toNullTransitionTime == this.toNullTransitionTime;
        boolean a3 = this.loopMode == that.loopMode;
        boolean a4 = Float.compare(this.speedModifier, that.speedModifier) == 0;
        return this.animation.equals(that.animation)
                && that.toNullTransitionTime == this.toNullTransitionTime
                && this.loopMode == that.loopMode
                && Float.compare(this.speedModifier, that.speedModifier) == 0
                ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(elapsedTime, speedModifier, toNullTransitionTime, loopMode, animation);
    }

    public static class Builder {

        private AnimationTicker ticker;

        public Builder(Animation animation){
            this.ticker = new AnimationTicker(animation);
        }

        public Builder setLoopMode(Animation.LoopMode mode){
            this.ticker.loopMode = mode;
            return this;
        }

        public Builder setToNullTransitionTime(int time){
            this.ticker.toNullTransitionTime = time;
            return this;
        }

        public Builder setSpeed(float modifier){
            this.ticker.speedModifier = modifier;
            return this;
        }

        public Builder startTime(float startTime){
            this.ticker.elapsedTime = startTime;
            return this;
        }

        public AnimationTicker build(){
            return ticker;
        }

    }
}
