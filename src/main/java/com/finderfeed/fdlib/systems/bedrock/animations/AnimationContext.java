package com.finderfeed.fdlib.systems.bedrock.animations;

import com.finderfeed.fdlib.shunting_yard.sy_base.ExpressionContext;

public class AnimationContext extends ExpressionContext {

    private Animation animation;

    public AnimationContext(Animation animation){
        super(true);
        this.animation = animation;
    }
    public AnimationContext(Animation animation,boolean init){
        super(init);
        this.animation = animation;
    }
    public AnimationContext(){
        super(false);
    }


    public void setAnimation(Animation animation) {
        this.animation = animation;
    }

    public Animation getAnimation() {
        return animation;
    }
}
