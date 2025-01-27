package com.finderfeed.fdlib.systems.bedrock.animations.animation_system.tile.renderer;

import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.AnimatedObject;
import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.entity.renderer.FDEntityTransformation;
import com.finderfeed.fdlib.systems.bedrock.models.FDModelInfo;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.function.Function;
import java.util.function.Supplier;

public class FDBlockRenderLayerOptions<T extends BlockEntity & AnimatedObject> {

    public Supplier<FDModelInfo> layerModel;
    public RenderType renderType;
    public Function<T,Boolean> renderCondition = (entity)->true;
    public FDBlockEntityTransformation<T> transform = (entity, stack, pticks)->{};

    private FDBlockRenderLayerOptions(){
    }

    public static <T extends BlockEntity & AnimatedObject> FDBlockRenderLayerOptions.Builder<T> builder(){
        return new FDBlockRenderLayerOptions.Builder<>();
    }

    public static class Builder<T extends BlockEntity & AnimatedObject> {

        private Supplier<FDModelInfo> layerModel;
        private RenderType renderType;
        private Function<T,Boolean> renderCondition = (entity)->true;
        private FDBlockEntityTransformation<T> transform = (entity, stack, ticks)->{};

        public Builder(){}

        public FDBlockRenderLayerOptions.Builder<T> model(Supplier<FDModelInfo> infoSupplier){
            this.layerModel = infoSupplier;
            return this;
        }

        public FDBlockRenderLayerOptions.Builder<T> renderType(RenderType type){
            this.renderType = type;
            return this;
        }

        public FDBlockRenderLayerOptions.Builder<T> renderCondition(Function<T,Boolean> renderCondition){
            this.renderCondition = renderCondition;
            return this;
        }

        public FDBlockRenderLayerOptions.Builder<T> transformation(FDBlockEntityTransformation<T> transformation){
            this.transform = transformation;
            return this;
        }

        public FDBlockRenderLayerOptions<T> build(){
            if (layerModel == null) throw new RuntimeException("Model for render layer is not registered.");
            if (renderType == null) throw new RuntimeException("RenderType for render layer is not registered.");
            FDBlockRenderLayerOptions<T> layer = new FDBlockRenderLayerOptions<T>();
            layer.renderType = renderType;
            layer.layerModel = layerModel;
            layer.renderCondition = renderCondition;
            layer.transform = transform;
            return layer;
        }

    }


}