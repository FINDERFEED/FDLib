package com.finderfeed.fdlib.systems.bedrock.animations.animation_system.entity.entity_types.renderers;

import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.AnimatedObject;
import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.AnimationSystem;
import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.FDEntityRenderTypes;
import com.finderfeed.fdlib.systems.bedrock.models.FDModel;
import com.finderfeed.fdlib.systems.bedrock.models.ModelHaver;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

import java.util.function.Function;

public abstract class FDEntityRenderer<T extends Entity & ModelHaver & AnimatedObject> extends EntityRenderer<T> {

    private Function<T, RenderType> typeGetter;
    private FDEntityRenderTypes<T> layerTypes;

    public FDEntityRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.typeGetter = (entity)->RenderType.entityTranslucent(this.getTextureLocation(entity));
        this.layerTypes = new FDEntityRenderTypes<>();
    }

    public FDEntityRenderer(EntityRendererProvider.Context context,FDEntityRenderTypes<T> types) {
        super(context);
        this.typeGetter = (entity)->RenderType.entityTranslucent(this.getTextureLocation(entity));
        this.layerTypes = types;
    }

    public FDEntityRenderer(EntityRendererProvider.Context context,Function<T, RenderType> typeGetter) {
        super(context);
        this.typeGetter = typeGetter;
    }

    public FDEntityRenderer(EntityRendererProvider.Context context, Function<T, RenderType> typeGetter, FDEntityRenderTypes<T> getters) {
        super(context);
        this.typeGetter = typeGetter;
        this.layerTypes = getters;
    }


    @Override
    public void render(T entity, float p_114486_, float partialTicks, PoseStack matrices, MultiBufferSource src, int light) {
        super.render(entity, p_114486_, partialTicks, matrices, src, light);
        int overlay = OverlayTexture.NO_OVERLAY;
        if (entity instanceof LivingEntity living){
            overlay = LivingEntityRenderer.getOverlayCoords(living, p_114486_);
        }

        this.applyAnimations(entity,entity.getSystem(),entity.getModel(),partialTicks);
        this.renderModel(entity,entity.getModel(),partialTicks,matrices,src,overlay,light);
        this.renderLayers(entity,entity.getModel(),partialTicks,matrices,src,overlay,light);

    }

    public void applyAnimations(T entity,AnimationSystem system,FDModel model,float partialTicks){
        system.applyAnimations(model,partialTicks);
        var layerMap = entity.getLayers();
        for (var layer : layerMap.values()){
            system.applyAnimations(layer,partialTicks);
        }
    }

    public void renderModel(T entity,FDModel model,float partialTicks,PoseStack matrices,MultiBufferSource src,int overlay,int light) {
        RenderType renderType = this.renderType().apply(entity);
        model.render(matrices, src.getBuffer(renderType), light, overlay, 1f, 1f, 1f, 1f);
    }

    public void renderLayers(T entity,FDModel model,float partialTicks,PoseStack matrices,MultiBufferSource src,int overlay,int light) {
        RenderType renderType;
        var layerMap = entity.getLayers();
        for (var layer : layerMap.entrySet()){
            renderType = this.layerTypes.getRenderType(layer.getKey(),entity);
            FDModel md = layer.getValue();
            md.render(matrices,src.getBuffer(renderType),light,overlay,1f,1f,1f,1f);
        }
    }

    public Function<T, RenderType> renderType(){
        return typeGetter;
    }






    public static <T extends Entity & ModelHaver & AnimatedObject> EntityRendererProvider<T> createRenderer(ResourceLocation defaultTexture){
        return (ctx)->{
            return new FDEntityRenderer<T>(ctx) {
                @Override
                public ResourceLocation getTextureLocation(T p_114482_) {
                    return defaultTexture;
                }
            };
        };
    }

    public static <T extends Entity & ModelHaver & AnimatedObject> EntityRendererProvider<T> createRenderer(ResourceLocation defaultTexture,FDEntityRenderTypes<T> types){
        return (ctx)->{
            return new FDEntityRenderer<T>(ctx,types) {
                @Override
                public ResourceLocation getTextureLocation(T p_114482_) {
                    return defaultTexture;
                }
            };
        };
    }

    public static <T extends Entity & ModelHaver & AnimatedObject> EntityRendererProvider<T> createRenderer(Function<T, RenderType> typeGetter){
        return (ctx)->{
            return new FDEntityRenderer<T>(ctx,typeGetter) {
                @Override
                public ResourceLocation getTextureLocation(T p_114482_) {
                    return TextureAtlas.LOCATION_BLOCKS;
                }
            };
        };
    }

    public static <T extends Entity & ModelHaver & AnimatedObject> EntityRendererProvider<T> createRenderer(Function<T, RenderType> typeGetter, FDEntityRenderTypes<T> types){
        return (ctx)->{
            return new FDEntityRenderer<T>(ctx,typeGetter,types) {
                @Override
                public ResourceLocation getTextureLocation(T p_114482_) {
                    return TextureAtlas.LOCATION_BLOCKS;
                }
            };
        };
    }

}
