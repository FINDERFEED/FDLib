package com.finderfeed.fdlib.systems.bedrock.animations.animation_system;

public interface AnimatedObject {

    AnimationSystem getSystem();

    default void tickAnimationSystem(){
        this.getSystem().tick();
    }
}
