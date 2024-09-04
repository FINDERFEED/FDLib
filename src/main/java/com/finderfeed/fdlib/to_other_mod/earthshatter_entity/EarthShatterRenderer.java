package com.finderfeed.fdlib.to_other_mod.earthshatter_entity;

import com.finderfeed.fdlib.util.rendering.FDRenderUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.client.model.data.ModelData;

public class EarthShatterRenderer extends EntityRenderer<EarthShatterEntity> {

    public EarthShatterRenderer(EntityRendererProvider.Context ctx) {
        super(ctx);
    }

    @Override
    public void render(EarthShatterEntity entity, float idk, float pticks, PoseStack matrices, MultiBufferSource src, int light) {
        BlockRenderDispatcher renderer = Minecraft.getInstance().getBlockRenderer();
        BlockState state = entity.getBlockState();

        matrices.pushPose();

        Vec3 dir = entity.getShatterDirection();

        matrices.translate(0.5,0.5,0.5);
        matrices.scale(1.1f,1.1f,1.1f);
        FDRenderUtil.applyMovementMatrixRotations(matrices,dir);
        matrices.translate(-0.5,-0.5,-0.5);


        BlockPos pos = entity.getOnPos().above(2);
        light = LightTexture.pack(this.getBlockLightLevel(entity, pos), this.getSkyLightLevel(entity, pos));


        renderer.renderSingleBlock(state,matrices,src,light,OverlayTexture.NO_OVERLAY, ModelData.EMPTY, null);

        matrices.popPose();
    }

    @Override
    public ResourceLocation getTextureLocation(EarthShatterEntity p_114482_) {
        return TextureAtlas.LOCATION_BLOCKS;
    }
}
