package com.finderfeed.fdlib.systems.bedrock.animations;

public class TransitionAnimation extends Animation {

    private Animation transitionTo;

    public TransitionAnimation(Animation transitionTo){
        this.transitionTo = transitionTo;
    }

    public Animation getTransitionTo() {
        return transitionTo;
    }
}
