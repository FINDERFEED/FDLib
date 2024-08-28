package com.finderfeed.fdlib.systems.bedrock.animations.animation_system;

import com.finderfeed.fdlib.systems.bedrock.models.ModelHaver;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.entity.Entity;

import java.util.HashMap;
import java.util.function.Function;

public class FDEntityRenderTypes<T extends Entity & ModelHaver & AnimatedObject> {

    public HashMap<String,Function<T, RenderType>> typeGetters = new HashMap<>();

    public FDEntityRenderTypes<T> addRenderType(String layerName, Function<T, RenderType> getter){
        this.typeGetters.put(layerName,getter);
        return this;
    }

    public RenderType getRenderType(String layer,T entity){
        return typeGetters.get(layer).apply(entity);
    }
}
