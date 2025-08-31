package com.finderfeed.fdlib.systems.bedrock.animations.animation_system.entity.renderer;

import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.AnimatedObject;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.Entity;

import java.util.ArrayList;
import java.util.List;

public class FDEntityRendererBuilder<T extends Entity & AnimatedObject> {

    private List<FDEntityRenderLayerOptions<T>> layers = new ArrayList<>();
    private IShouldEntityRender<T> shouldRender = null;
    private FDEntityIgnoreYaw<T> ignoreYaw = null;
    private FDFreeEntityRenderer<T> freeEntityRenderer;

    public static <E extends Entity & AnimatedObject> FDEntityRendererBuilder<E> builder(){
        return new FDEntityRendererBuilder<E>();
    }

    public FDEntityRendererBuilder<T> addLayer(FDEntityRenderLayerOptions<T> layer){
        this.layers.add(layer);
        return this;
    }

    public FDEntityRendererBuilder<T> freeRender(FDFreeEntityRenderer<T> freeEntityRenderer){
        this.freeEntityRenderer = freeEntityRenderer;
        return this;
    }

    public FDEntityRendererBuilder<T> shouldRender(IShouldEntityRender<T> shouldRender){
        this.shouldRender = shouldRender;
        return this;
    }

    public FDEntityRendererBuilder<T> ignoreYaw(FDEntityIgnoreYaw<T> ignoreYaw){
        this.ignoreYaw = ignoreYaw;
        return this;
    }

    public EntityRendererProvider<T> build(){
        return (context -> {
            return new FDEntityRenderer<>(context,ignoreYaw,shouldRender,layers,freeEntityRenderer);
        });
    }


}
