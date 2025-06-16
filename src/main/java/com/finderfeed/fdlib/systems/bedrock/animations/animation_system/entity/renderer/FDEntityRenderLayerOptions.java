package com.finderfeed.fdlib.systems.bedrock.animations.animation_system.entity.renderer;

import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.AnimatedObject;
import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.BoneTransformationController;
import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.RenderFunction;
import com.finderfeed.fdlib.systems.bedrock.models.FDModelInfo;
import com.finderfeed.fdlib.util.FDColor;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.entity.Entity;

import java.util.HashMap;
import java.util.function.Function;
import java.util.function.Supplier;

public class FDEntityRenderLayerOptions<T extends Entity & AnimatedObject> {

    public Supplier<FDModelInfo> layerModel;
    public RenderFunction<T,RenderType> renderType;
    public Function<T,Boolean> renderCondition;
    public FDEntityTransformation<T> transform;
    public RenderFunction<T,FDColor> layerColor;
    public HashMap<String, BoneTransformationController<T>> boneControllers;
    public int light = -1;
    public boolean ignoreHurtOverlay;


    private FDEntityRenderLayerOptions(){
    }

    public static <T extends Entity & AnimatedObject> Builder<T> builder(){
        return new Builder<>();
    }

    public static class Builder<T extends Entity & AnimatedObject> {

        private Supplier<FDModelInfo> layerModel;
        private RenderFunction<T,RenderType> renderType;
        private Function<T,Boolean> renderCondition = (entity)->true;
        private FDEntityTransformation<T> transform = (entity, stack, ticks)->{};
        private RenderFunction<T,FDColor> layerColor = (entity,partialTicks)->new FDColor(1,1,1,1);
        public HashMap<String, BoneTransformationController<T>> boneControllers = new HashMap<>();
        private boolean ignoreHurtOverlay = false;
        private int light = -1;

        public Builder(){}

        public Builder<T> addBoneController(String bone, BoneTransformationController<T> controller){
            this.boneControllers.put(bone, controller);
            return this;
        }

        public Builder<T> color(RenderFunction<T,FDColor> layerColorFunction){
            this.layerColor = layerColorFunction;
            return this;
        }

        public Builder<T> ignoreHurtOverlay(boolean state){
            this.ignoreHurtOverlay = state;
            return this;
        }


        public Builder<T> light(int light){
            this.light = light;
            return this;
        }

        public Builder<T> color(float r,float g,float b,float a){
            return this.color((entity,partialTicks)->new FDColor(r,g,b,a));
        }

        public Builder<T> model(Supplier<FDModelInfo> infoSupplier){
            this.layerModel = infoSupplier;
            return this;
        }

        public Builder<T> renderType(RenderFunction<T,RenderType> type){
            this.renderType = type;
            return this;
        }

        public Builder<T> renderType(RenderType type){
            return this.renderType((tile,partialTicks)->type);
        }

        public Builder<T> renderCondition(Function<T,Boolean> renderCondition){
            this.renderCondition = renderCondition;
            return this;
        }

        public Builder<T> transformation(FDEntityTransformation<T> transformation){
            this.transform = transformation;
            return this;
        }

        public FDEntityRenderLayerOptions<T> build(){
            if (layerModel == null) throw new RuntimeException("Model for render layer is not registered.");
            if (renderType == null) throw new RuntimeException("RenderType for render layer is not registered.");
            FDEntityRenderLayerOptions<T> layer = new FDEntityRenderLayerOptions<T>();
            layer.renderType = renderType;
            layer.layerModel = layerModel;
            layer.renderCondition = renderCondition;
            layer.transform = transform;
            layer.layerColor = layerColor;
            layer.ignoreHurtOverlay = ignoreHurtOverlay;
            layer.light = light;
            layer.boneControllers = boneControllers;
            return layer;
        }

    }


}
