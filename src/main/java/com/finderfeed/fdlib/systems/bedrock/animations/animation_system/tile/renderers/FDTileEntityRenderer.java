package com.finderfeed.fdlib.systems.bedrock.animations.animation_system.tile.renderers;

import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.AnimatedObject;
import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.AnimationSystem;
import com.finderfeed.fdlib.systems.bedrock.models.FDModel;
import com.finderfeed.fdlib.systems.bedrock.models.ModelHaver;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.function.Function;

public abstract class FDTileEntityRenderer<T extends BlockEntity & AnimatedObject & ModelHaver> implements BlockEntityRenderer<T> {

    private Function<T,RenderType> typeGetter;

    public FDTileEntityRenderer(BlockEntityRendererProvider.Context context, Function<T, RenderType> renderType){
        this.typeGetter = renderType;
    }

    @Override
    public void render(T tile, float partialTicks, PoseStack matrices, MultiBufferSource src, int light, int overlay) {
        this.applyAnimations(tile,partialTicks,matrices,src,light,overlay);
        this.renderModel(tile,partialTicks,matrices,src,light,overlay);
    }

    public void applyAnimations(T tile, float partialTicks, PoseStack matrices, MultiBufferSource src, int light, int overlay){
        FDModel model = tile.getModel();
        AnimationSystem system = tile.getSystem();
        system.applyAnimations(model,partialTicks);
    }

    public void renderModel(T tile, float partialTicks, PoseStack matrices, MultiBufferSource src, int light, int overlay){
        FDModel model = tile.getModel();
        RenderType type = this.renderType().apply(tile);
        model.render(matrices,src.getBuffer(type),light,overlay,1f,1f,1f,1f);
    }

    public Function<T, RenderType> renderType() {
        return typeGetter;
    }
}
