package com.finderfeed.fdlib.systems.bedrock.animations.animation_system.entity;

import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.AnimatedObject;
import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.AnimationSystem;
import net.minecraft.world.entity.Entity;

public abstract class EntityAnimationSystem<T extends Entity & AnimatedObject> extends AnimationSystem {
    private T entity;
    protected EntityAnimationSystem(T entity){
        this.entity = entity;
    }

    public T getEntity() {
        return entity;
    }

    public ServersideEntityAnimationSystem<T> getAsServerside(){
        return (ServersideEntityAnimationSystem<T>) this;
    }

    public static <T extends Entity & AnimatedObject> EntityAnimationSystem<T> create(T entity){
        if (entity.level().isClientSide){
            return new ClientsideEntityAnimationSystem<>(entity);
        }else{
            return new ServersideEntityAnimationSystem<>(entity);
        }
    }

}
