package com.finderfeed.fdlib.systems.bedrock.animations.animation_system.entity.renderer;

import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.AnimatedObject;
import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.AnimationSystem;
import com.finderfeed.fdlib.systems.bedrock.models.FDModel;
import com.finderfeed.fdlib.util.FDColor;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

import java.util.ArrayList;
import java.util.List;

public class FDEntityRenderer<T extends Entity & AnimatedObject> extends EntityRenderer<T> {

    public List<FDEntityRenderLayer<T>> layers;
    public IShouldEntityRender<T> shouldRender;
    public FDFreeEntityRenderer<T> freeRender;


    public FDEntityRenderer(EntityRendererProvider.Context ctx, IShouldEntityRender<T> shouldRender, List<FDEntityRenderLayerOptions<T>> layerDefinitions,FDFreeEntityRenderer<T> freeEntityRenderer) {
        super(ctx);
        this.freeRender = freeEntityRenderer;
        this.shouldRender = shouldRender;
        this.layers = new ArrayList<>();
        for (FDEntityRenderLayerOptions<T> layer : layerDefinitions){
            FDModel model = new FDModel(layer.layerModel.get());
            FDEntityRenderLayer<T> l = new FDEntityRenderLayer<>(model,layer.renderType,layer.renderCondition,layer.transform,layer.layerColor);
            this.layers.add(l);
        }
    }

    @Override
    public void render(T entity, float yaw, float partialTicks, PoseStack matrices, MultiBufferSource src, int light) {
        super.render(entity, yaw, partialTicks, matrices, src, light);

        this.applyAnimations(entity,yaw,partialTicks,matrices,src,light);
        this.renderLayers(entity,yaw,partialTicks,matrices,src,light);

        if (freeRender != null){
            freeRender.render(entity,yaw,partialTicks,matrices,src,light);
        }
    }

    public void applyAnimations(T entity, float yaw, float partialTicks, PoseStack matrices, MultiBufferSource src, int light){
        AnimationSystem system = entity.getSystem();
        for (var layer : this.layers){
            FDModel model = layer.model();
            system.applyAnimations(model,partialTicks);
            model.main.addYRot(-yaw);
        }
    }

    public void renderLayers(T entity, float idk, float partialTicks, PoseStack matrices, MultiBufferSource src, int light){
        for (var layer : this.layers){

            var condition = layer.renderCondition();
            if (!condition.apply(entity)) continue;

            matrices.pushPose();

            FDModel model = layer.model();
            RenderType type = layer.renderType().getValue(entity,partialTicks);
            VertexConsumer consumer = src.getBuffer(type);

            int overlay = OverlayTexture.NO_OVERLAY;
            if (entity instanceof LivingEntity livingEntity){
                overlay = LivingEntityRenderer.getOverlayCoords(livingEntity,0);
            }

            layer.matrixTransform().apply(entity,matrices,partialTicks);

            FDColor color = layer.color().getValue(entity,partialTicks);

            model.render(matrices,consumer,light, overlay,color.r,color.g,color.b,color.a);

            matrices.popPose();
        }
    }

    @Override
    public ResourceLocation getTextureLocation(T p_114482_) {
        return TextureAtlas.LOCATION_BLOCKS;
    }

    @Override
    public boolean shouldRender(T entity, Frustum frustum, double x, double y, double z) {
        if (shouldRender != null){
            return shouldRender.shouldRender(entity,frustum,x,y,z);
        }
        return super.shouldRender(entity, frustum, x, y, z);
    }

}
