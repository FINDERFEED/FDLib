package com.finderfeed.fdlib.systems.bedrock.animations.animation_system;

import com.finderfeed.fdlib.systems.FDRegistries;
import com.finderfeed.fdlib.systems.bedrock.animations.Animation;
import com.finderfeed.fdlib.systems.bedrock.animations.AnimationContext;
import com.finderfeed.fdlib.systems.bedrock.animations.TransitionAnimation;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

import java.util.Objects;
import java.util.function.Supplier;

public class AnimationTicker {


    public static final StreamCodec<FriendlyByteBuf,AnimationTicker> NO_NEXT_NETWORK_CODEC = StreamCodec.composite(
            ByteBufCodecs.FLOAT,ticker->ticker.elapsedTime,
            ByteBufCodecs.FLOAT,ticker->ticker.speedModifier,
            ByteBufCodecs.INT,ticker->ticker.toNullTransitionTime,
            ByteBufCodecs.BOOL,ticker->ticker.reversed,
            ByteBufCodecs.STRING_UTF8,ticker->ticker.loopMode.name(),
            ByteBufCodecs.STRING_UTF8,ticker->ticker.animation.getName().toString(),
            (elapsedTime,speedModifier,toNull,reversed,loopModeName,animationName)->{
                ResourceLocation location = ResourceLocation.tryParse(animationName);
                Animation animation = FDRegistries.ANIMATIONS.get(location);
                if (animation == null){
                    throw new RuntimeException("Unknown animation received from server: " + animationName);
                }
                Animation.LoopMode mode = Animation.LoopMode.valueOf(loopModeName);
                AnimationTicker ticker = new AnimationTicker(animation);
                ticker.loopMode = mode;
                ticker.reversed = reversed;
                ticker.elapsedTime = elapsedTime;
                ticker.speedModifier = speedModifier;
                ticker.toNullTransitionTime = toNull;
                return ticker;
            });

    public static final StreamCodec<FriendlyByteBuf, AnimationTicker> NETWORK_CODEC = new StreamCodec<FriendlyByteBuf, AnimationTicker>() {
        @Override
        public AnimationTicker decode(FriendlyByteBuf buf) {
            boolean hasNext = buf.readBoolean();
            AnimationTicker next = null;
            if (hasNext){
                next = NETWORK_CODEC.decode(buf);
            }
            AnimationTicker thisTicker = NO_NEXT_NETWORK_CODEC.decode(buf);
            thisTicker.next = next;
            return thisTicker;
        }

        @Override
        public void encode(FriendlyByteBuf buf, AnimationTicker ticker) {
            AnimationTicker next = ticker.next;
            buf.writeBoolean(next != null);
            if (next != null){
                NETWORK_CODEC.encode(buf,next);
            }
            NO_NEXT_NETWORK_CODEC.encode(buf, ticker);
        }
    };

    private AnimationTicker next = null;
    private float elapsedTime = 0;
    private float speedModifier = 1;
    private int toNullTransitionTime;
    private boolean reversed;
    private Animation.LoopMode loopMode;
    private Animation animation;


    public AnimationTicker(AnimationTicker other){
        this.elapsedTime = other.getElapsedTime();
        this.speedModifier = other.speedModifier;
        this.toNullTransitionTime = other.toNullTransitionTime;
        this.loopMode = other.loopMode;
        this.animation = other.getAnimation();
        this.reversed = other.reversed;
        this.next = other.next;
    }

    public AnimationTicker(Animation animation){
        this.animation = animation;
        this.toNullTransitionTime = animation.getAnimTime();
        this.loopMode = animation.getDefaultLoopMode();
    }

    public void tick(){
        if (this.getLoopMode() != Animation.LoopMode.LOOP) {
            elapsedTime = Mth.clamp(elapsedTime + speedModifier, 0, animation.getAnimTime());
        }else{
            elapsedTime = (elapsedTime + speedModifier);
            if (this.animation instanceof TransitionAnimation transitionAnimation && elapsedTime >= this.animation.getAnimTime()){
                this.animation = transitionAnimation.getTransitionTo();
            }
            elapsedTime = elapsedTime % this.animation.getAnimTime();
        }
    }

    public void resetTime(){
        elapsedTime = 0;
    }

    public void addVariables(AnimationContext context,float partialTicks){
        context.variables.put("query.anim_time",(elapsedTime + partialTicks * this.speedModifier) / 20f);
        context.variables.put("math.pi",(float) Math.PI);
    }

    public boolean hasEnded(){
        if (loopMode == Animation.LoopMode.LOOP){
            return false;
        }
        return elapsedTime == animation.getAnimTime();
    }

    public AnimationTicker getNext() {
        return next;
    }

    public boolean isReversed() {
        return reversed;
    }

    public Animation getAnimation() {
        return animation;
    }

    public float getElapsedTime() {
        return elapsedTime;
    }

    public float getTime(float partialTicks){

        partialTicks *= this.getSpeedModifier();

        if (loopMode != Animation.LoopMode.LOOP) {
            if (reversed) {
                return Mth.clamp(this.animation.getAnimTime() - this.getElapsedTime() - partialTicks, 0, this.animation.getAnimTime());
            } else {
                return Mth.clamp(this.getElapsedTime() + partialTicks, 0, this.animation.getAnimTime());
            }
        }else{
            if (reversed) {

                float time = this.animation.getAnimTime() - this.getElapsedTime() - partialTicks;

                if (time < 0){
                    return this.animation.getAnimTime() + time;
                }else{
                    return time;
                }

            } else {
                return (this.getElapsedTime() + partialTicks) % this.animation.getAnimTime();
            }
        }
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
    public static Builder builder(Supplier<Animation> animation){
        return new Builder(animation.get());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AnimationTicker that = (AnimationTicker) o;
        return this.animation.equals(that.animation)
                && that.toNullTransitionTime == this.toNullTransitionTime
                && this.loopMode == that.loopMode
                && Float.compare(this.speedModifier, that.speedModifier) == 0
                && (this.reversed == that.reversed)
                && Objects.equals(next,that.next);
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

        public Builder nextAnimation(AnimationTicker ticker){
            this.ticker.next = ticker;
            return this;
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
            this.ticker.elapsedTime = Mth.clamp(startTime,0,ticker.animation.getAnimTime());
            return this;
        }

        public Builder reversed(){
            ticker.reversed = true;
            return this;
        }

        public AnimationTicker build(){
            return ticker;
        }

    }
}
