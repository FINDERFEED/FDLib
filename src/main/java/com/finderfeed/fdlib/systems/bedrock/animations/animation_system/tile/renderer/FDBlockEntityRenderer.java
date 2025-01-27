package com.finderfeed.fdlib.systems.bedrock.animations.animation_system.tile.renderer;

import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.AnimatedObject;
import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.AnimationSystem;
import com.finderfeed.fdlib.systems.bedrock.models.FDModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

public class FDBlockEntityRenderer<T extends BlockEntity & AnimatedObject> implements BlockEntityRenderer<T> {

    private IShouldBERender<T> shouldBERender;
    private IBERenderOffScreen<T> renderOffScreen;
    private List<FDBlockEntityRenderLayer<T>> layers;
    private FDFreeBERenderer<T> freeRender;

    public FDBlockEntityRenderer(BlockEntityRendererProvider.Context ctx,
                                 IShouldBERender<T> shouldBERender,
                                 IBERenderOffScreen<T> shouldRenderOffScreen,
                                 List<FDBlockRenderLayerOptions<T>> layers,
                                 FDFreeBERenderer<T> freeRender
    ){

        this.freeRender = freeRender;
        this.shouldBERender = shouldBERender;
        this.renderOffScreen = shouldRenderOffScreen;
        this.layers = new ArrayList<>();

        for (FDBlockRenderLayerOptions<T> layer : layers){
            FDModel model = new FDModel(layer.layerModel.get());
            FDBlockEntityRenderLayer<T> l = new FDBlockEntityRenderLayer<>(model,layer.renderType,layer.renderCondition,layer.transform);
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
            RenderType type = layer.renderType();
            VertexConsumer consumer = src.getBuffer(type);

            layer.matrixTransform().apply(entity,matrices,partialTicks);

            model.render(matrices,consumer,light, overlay,1f,1f,1f,1f);

            matrices.popPose();
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
