package com.finderfeed.fdlib.systems.bedrock.animations;

import com.finderfeed.fdlib.shunting_yard.sy_base.ExpressionContext;

public class AnimationContext extends ExpressionContext {

    private Animation animation;
    private Animation.LoopMode currentLoopMode;

    public AnimationContext(Animation animation, Animation.LoopMode currentLoopMode){
        super(true);
        this.animation = animation;
        this.currentLoopMode = currentLoopMode;
    }
    public AnimationContext(Animation animation,boolean init){
        super(init);
        this.animation = animation;
    }
    public AnimationContext(){
        super(false);
    }


    public Animation.LoopMode getCurrentLoopMode() {
        return currentLoopMode;
    }

    public void setCurrentLoopMode(Animation.LoopMode currentLoopMode) {
        this.currentLoopMode = currentLoopMode;
    }

    public void setAnimation(Animation animation) {
        this.animation = animation;
    }

    public Animation getAnimation() {
        return animation;
    }
}
