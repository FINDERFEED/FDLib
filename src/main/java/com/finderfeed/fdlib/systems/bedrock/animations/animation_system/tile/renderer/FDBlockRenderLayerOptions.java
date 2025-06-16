package com.finderfeed.fdlib.systems.bedrock.animations.animation_system.tile.renderer;

import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.AnimatedObject;
import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.BoneTransformationController;
import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.RenderFunction;
import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.entity.renderer.FDEntityRenderLayerOptions;
import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.entity.renderer.FDEntityTransformation;
import com.finderfeed.fdlib.systems.bedrock.models.FDModelInfo;
import com.finderfeed.fdlib.util.FDColor;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.HashMap;
import java.util.function.Function;
import java.util.function.Supplier;

public class FDBlockRenderLayerOptions<T extends BlockEntity & AnimatedObject> {

    public Supplier<FDModelInfo> layerModel;
    public RenderFunction<T,RenderType> renderType;
    public Function<T,Boolean> renderCondition;
    public FDBlockEntityTransformation<T> transform;
    public RenderFunction<T, FDColor> layerColor;
    public HashMap<String, BoneTransformationController<T>> boneControllers;
    public int light = -1;

    private FDBlockRenderLayerOptions(){
    }

    public static <T extends BlockEntity & AnimatedObject> FDBlockRenderLayerOptions.Builder<T> builder(){
        return new FDBlockRenderLayerOptions.Builder<>();
    }

    public static class Builder<T extends BlockEntity & AnimatedObject> {

        private Supplier<FDModelInfo> layerModel;
        private RenderFunction<T,RenderType> renderType;
        private Function<T,Boolean> renderCondition = (entity)->true;
        private FDBlockEntityTransformation<T> transform = (entity, stack, ticks)->{};
        private RenderFunction<T, FDColor> layerColor = (tile,partialTicks)->new FDColor(1,1,1,1);
        public HashMap<String, BoneTransformationController<T>> boneControllers = new HashMap<>();
        private int light = -1;

        public Builder(){}

        public Builder<T> color(RenderFunction<T,FDColor> layerColorFunction){
            this.layerColor = layerColorFunction;
            return this;
        }

        public Builder<T> addBoneController(String bone, BoneTransformationController<T> boneController){
            this.boneControllers.put(bone, boneController);
            return this;
        }

        public Builder<T> color(float r, float g, float b, float a){
            return this.color((entity,partialTicks)->new FDColor(r,g,b,a));
        }

        public FDBlockRenderLayerOptions.Builder<T> model(Supplier<FDModelInfo> infoSupplier){
            this.layerModel = infoSupplier;
            return this;
        }

        public FDBlockRenderLayerOptions.Builder<T> renderType(RenderFunction<T,RenderType> type){
            this.renderType = type;
            return this;
        }

        public FDBlockRenderLayerOptions.Builder<T> renderType(RenderType type){
            return this.renderType((tile,pticks)->type);
        }

        public FDBlockRenderLayerOptions.Builder<T> renderCondition(Function<T,Boolean> renderCondition){
            this.renderCondition = renderCondition;
            return this;
        }

        public FDBlockRenderLayerOptions.Builder<T> transformation(FDBlockEntityTransformation<T> transformation){
            this.transform = transformation;
            return this;
        }

        public FDBlockRenderLayerOptions.Builder<T> light(int light){
            this.light = light;
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
            layer.layerColor = layerColor;
            layer.light = light;
            layer.boneControllers = boneControllers;
            return layer;
        }

    }


}