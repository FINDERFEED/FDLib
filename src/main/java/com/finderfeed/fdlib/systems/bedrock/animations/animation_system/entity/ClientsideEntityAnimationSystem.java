package com.finderfeed.fdlib.systems.bedrock.animations.animation_system.entity;

import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.AnimatedObject;
import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.AnimationTicker;
import net.minecraft.world.entity.Entity;

public class ClientsideEntityAnimationSystem<T extends Entity & AnimatedObject> extends EntityAnimationSystem<T> {
    protected ClientsideEntityAnimationSystem(T entity) {
        super(entity);
    }

    @Override
    public void onAnimationStart(String name, AnimationTicker ticker) {

    }

    @Override
    public void onAnimationStop(String name) {

    }

    @Override
    public void onFreeze(boolean state) {

    }

    @Override
    public void onVariableAdded(String name, float variable) {

    }
}
