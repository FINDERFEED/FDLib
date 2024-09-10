package com.finderfeed.fdlib.systems.bedrock.animations.animation_system.entity.renderer;

import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.AnimatedObject;
import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.AnimationSystem;
import com.finderfeed.fdlib.systems.bedrock.models.FDModel;
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

    public List<ReadyLayer<T>> layers;
    public IShouldRender<T> shouldRender;


    public FDEntityRenderer(EntityRendererProvider.Context ctx,IShouldRender<T> shouldRender, List<FDRenderLayerOptions<T>> layerDefinitions) {
        super(ctx);
        this.shouldRender = shouldRender;
        this.layers = new ArrayList<>();
        for (FDRenderLayerOptions<T> layer : layerDefinitions){
            FDModel model = new FDModel(layer.layerModel.get());
            ReadyLayer<T> l = new ReadyLayer<>(model,layer.renderType,layer.renderCondition,layer.transform);
            this.layers.add(l);
        }
    }

    @Override
    public void render(T entity, float idk, float partialTicks, PoseStack matrices, MultiBufferSource src, int light) {
        super.render(entity, idk, partialTicks, matrices, src, light);

        this.applyAnimations(entity,idk,partialTicks,matrices,src,light);
        this.renderLayers(entity,idk,partialTicks,matrices,src,light);
    }

    public void applyAnimations(T entity, float idk, float partialTicks, PoseStack matrices, MultiBufferSource src, int light){
        AnimationSystem system = entity.getSystem();
        for (var layer : this.layers){
            FDModel model = layer.model();
            system.applyAnimations(model,partialTicks);
            model.main.addYRot(-idk);
        }
    }

    public void renderLayers(T entity, float idk, float partialTicks, PoseStack matrices, MultiBufferSource src, int light){
        for (var layer : this.layers){

            var condition = layer.renderCondition();
            if (!condition.apply(entity)) continue;

            matrices.pushPose();

            FDModel model = layer.model();
            RenderType type = layer.renderType();
            VertexConsumer consumer = src.getBuffer(type);

            int overlay = OverlayTexture.NO_OVERLAY;
            if (entity instanceof LivingEntity livingEntity){
                overlay = LivingEntityRenderer.getOverlayCoords(livingEntity,0);
            }

            layer.matrixTransform().apply(entity,matrices,partialTicks);

            model.render(matrices,consumer,light, overlay,1f,1f,1f,1f);

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
