package com.finderfeed.fdlib.systems.bedrock.animations.animation_system.tile.renderer;

import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.AnimatedObject;
import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.AnimationSystem;
import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.RenderFunction;
import com.finderfeed.fdlib.systems.bedrock.models.FDModel;
import com.finderfeed.fdlib.util.FDColor;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

public class FDBlockEntityRenderer<T extends BlockEntity & AnimatedObject> implements BlockEntityRenderer<T> {

    private IShouldBERender<T> shouldBERender;
    private IBERenderOffScreen<T> renderOffScreen;
    private List<FDBlockEntityRenderLayer<T>> layers;
    private FDFreeBERenderer<T> freeRender;
    private RenderFunction<BlockEntity, AABB> renderBox;

    public FDBlockEntityRenderer(BlockEntityRendererProvider.Context ctx,
                                 IShouldBERender<T> shouldBERender,
                                 IBERenderOffScreen<T> shouldRenderOffScreen,
                                 List<FDBlockRenderLayerOptions<T>> layers,
                                 FDFreeBERenderer<T> freeRender,
                                 RenderFunction<BlockEntity, AABB> renderBox
    ){

        this.freeRender = freeRender;
        this.shouldBERender = shouldBERender;
        this.renderOffScreen = shouldRenderOffScreen;
        this.layers = new ArrayList<>();
        this.renderBox = renderBox;
        for (FDBlockRenderLayerOptions<T> layer : layers){
            FDModel model = new FDModel(layer.layerModel.get());
            FDBlockEntityRenderLayer<T> l = new FDBlockEntityRenderLayer<>(model,layer.renderType,layer.renderCondition,layer.transform,layer.layerColor);
            this.layers.add(l);
        }

    }

    @Override
    public void render(T blockEntity, float pticks, PoseStack matrices, MultiBufferSource src, int light, int overlay) {
        if (blockEntity.getLevel() == null) return;
        matrices.pushPose();

        matrices.translate(0.5,0,0.5);

        this.applyAnimations(blockEntity,pticks,matrices,src,light,overlay);
        this.renderLayers(blockEntity,pticks,matrices,src,light,overlay);
        if (freeRender != null){
            freeRender.render(blockEntity,pticks,matrices,src,light,overlay);
        }
        matrices.popPose();
    }


    public void applyAnimations(T entity, float partialTicks, PoseStack matrices, MultiBufferSource src, int light,int overlay){
        AnimationSystem system = entity.getSystem();
        if (system == null) return;
        for (var layer : this.layers){
            FDModel model = layer.model();
            system.applyAnimations(model,partialTicks);
        }
    }

    public void renderLayers(T entity, float partialTicks, PoseStack matrices, MultiBufferSource src, int light,int overlay){
        for (var layer : this.layers){

            var condition = layer.renderCondition();
            if (!condition.apply(entity)) continue;

            matrices.pushPose();

            FDModel model = layer.model();
            RenderType type = layer.renderType().getValue(entity,partialTicks);
            VertexConsumer consumer = src.getBuffer(type);

            layer.matrixTransform().apply(entity,matrices,partialTicks);

            FDColor color = layer.color().getValue(entity,partialTicks);

            model.render(matrices,consumer,light, overlay,color.r,color.g,color.b,color.a);

            matrices.popPose();
        }
    }

    @Override
    public AABB getRenderBoundingBox(T blockEntity) {
        if (renderBox == null) {
            return BlockEntityRenderer.super.getRenderBoundingBox(blockEntity);
        }else{
            return renderBox.getValue(blockEntity, Minecraft.getInstance().getTimer().getGameTimeDeltaPartialTick(true));
        }
    }

    @Override
    public boolean shouldRender(T blockEntity, Vec3 idk) {
        return shouldBERender.shouldRender(blockEntity,idk);
    }

    @Override
    public boolean shouldRenderOffScreen(T renderOffScreen) {
        return this.renderOffScreen.shouldRenderOffScreen(renderOffScreen);
    }
}
