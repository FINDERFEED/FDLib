package com.finderfeed.fdlib.systems.bedrock.animations.animation_system.entity.renderer;

import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.AnimatedObject;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.Entity;

import java.util.ArrayList;
import java.util.List;

public class FDEntityRendererBuilder<T extends Entity & AnimatedObject> {

    private List<FDRenderLayerOptions<T>> layers = new ArrayList<>();

    public static <E extends Entity & AnimatedObject> FDEntityRendererBuilder<E> builder(){
        return new FDEntityRendererBuilder<E>();
    }

    public FDEntityRendererBuilder<T> addLayer(FDRenderLayerOptions<T> layer){
        this.layers.add(layer);
        return this;
    }


    public EntityRendererProvider<T> build(){
        return (context -> {
            return new FDEntityRenderer<>(context,layers);
        });
    }


}
