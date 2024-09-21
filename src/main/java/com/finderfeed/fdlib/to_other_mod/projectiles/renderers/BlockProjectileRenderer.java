package com.finderfeed.fdlib.to_other_mod.projectiles.renderers;

import com.finderfeed.fdlib.to_other_mod.projectiles.BlockProjectile;
import com.finderfeed.fdlib.util.math.FDMathUtil;
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

public class BlockProjectileRenderer extends EntityRenderer<BlockProjectile> {



    public BlockProjectileRenderer(EntityRendererProvider.Context p_174008_) {
        super(p_174008_);
    }

    @Override
    public void render(BlockProjectile entity, float yaw, float pticks, PoseStack matrices, MultiBufferSource src, int light) {
        BlockState state = entity.getBlockState();
        Vec3 oldDirection = entity.previousDirection.normalize();
        Vec3 currentDirection = entity.getLookDirection().normalize();

        var v = FDMathUtil.interpolateVectors(oldDirection,currentDirection,pticks).normalize();

        BlockPos pos = entity.getOnPos().above(2);
        light = LightTexture.pack(this.getBlockLightLevel(entity, pos), this.getSkyLightLevel(entity, pos));
        BlockRenderDispatcher renderer = Minecraft.getInstance().getBlockRenderer();
        matrices.pushPose();
        matrices.translate(-0.5,0,-0.5);
        matrices.translate(0.5,0.5,0.5);
//        FDRenderUtil.applyMovementMatrixRotations(matrices,v);
        matrices.mulPose(entity.currentRotation);
        matrices.translate(-0.5,-0.5,-0.5);

        renderer.renderSingleBlock(state,matrices,src,light, OverlayTexture.NO_OVERLAY, ModelData.EMPTY,null);
        matrices.popPose();

    }

    @Override
    public ResourceLocation getTextureLocation(BlockProjectile p_114482_) {
        return TextureAtlas.LOCATION_BLOCKS;
    }
}
