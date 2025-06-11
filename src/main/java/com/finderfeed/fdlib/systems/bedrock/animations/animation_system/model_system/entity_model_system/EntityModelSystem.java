package com.finderfeed.fdlib.systems.bedrock.animations.animation_system.model_system.entity_model_system;

import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.AnimatedObject;
import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.entity.ClientsideEntityAnimationSystem;
import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.entity.EntityAnimationSystem;
import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.entity.ServersideEntityAnimationSystem;
import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.model_system.ModelSystem;
import com.finderfeed.fdlib.systems.bedrock.models.FDModelInfo;
import net.minecraft.world.entity.Entity;

import java.util.UUID;

public abstract class EntityModelSystem<T extends Entity & AnimatedObject> extends ModelSystem {

    private T entity;

    protected EntityModelSystem(T entity) {
        super(EntityAnimationSystem.create(entity));
        this.entity = entity;
    }

    public T getEntity() {
        return entity;
    }

    public ServerEntityModelSystem<T> asServerside(){
        return (ServerEntityModelSystem<T>) this;
    }

    public static <T extends Entity & AnimatedObject> EntityModelSystem<T> create(T entity){
        if (entity.level().isClientSide){
            return new ClientEntityModelSystem<>(entity);
        }else{
            return new ServerEntityModelSystem<>(entity);
        }
    }

}
