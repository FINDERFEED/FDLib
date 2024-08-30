package com.finderfeed.fdlib.systems.bedrock.animations.animation_system.entity.renderer;

import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.AnimatedObject;
import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.Transformation;
import com.finderfeed.fdlib.systems.bedrock.models.FDModelInfo;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.entity.Entity;

import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class FDRenderLayerOptions<T extends Entity & AnimatedObject> {

    public Supplier<FDModelInfo> layerModel;
    public RenderType renderType;
    public Function<T,Boolean> renderCondition = (entity)->true;
    public Transformation<T> transform = (entity, stack, pticks)->{};

    private FDRenderLayerOptions(){
    }

    public static <T extends Entity & AnimatedObject> Builder<T> builder(){
        return new Builder<>();
    }

    public static class Builder<T extends Entity & AnimatedObject> {

        private Supplier<FDModelInfo> layerModel;
        private RenderType renderType;
        private Function<T,Boolean> renderCondition = (entity)->true;
        private Transformation<T> transform = (entity,stack,ticks)->{};

        public Builder(){}

        public Builder<T> model(Supplier<FDModelInfo> infoSupplier){
            this.layerModel = infoSupplier;
            return this;
        }

        public Builder<T> renderType(RenderType type){
            this.renderType = type;
            return this;
        }

        public Builder<T> renderCondition(Function<T,Boolean> renderCondition){
            this.renderCondition = renderCondition;
            return this;
        }

        public Builder<T> transformation(Transformation<T> transformation){
            this.transform = transformation;
            return this;
        }

        public FDRenderLayerOptions<T> build(){
            if (layerModel == null) throw new RuntimeException("Model for render layer is not registered.");
            if (renderType == null) throw new RuntimeException("RenderType for render layer is not registered.");
            FDRenderLayerOptions<T> layer = new FDRenderLayerOptions<T>();
            layer.renderType = renderType;
            layer.layerModel = layerModel;
            layer.renderCondition = renderCondition;
            layer.transform = transform;
            return layer;
        }

    }


}
