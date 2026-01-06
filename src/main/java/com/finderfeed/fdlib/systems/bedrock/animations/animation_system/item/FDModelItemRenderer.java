package com.finderfeed.fdlib.systems.bedrock.animations.animation_system.item;

import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.item.animated_item.EmptyItemStackContext;
import com.finderfeed.fdlib.systems.bedrock.models.FDModel;
import com.finderfeed.fdlib.util.FDColor;
import com.finderfeed.fdlib.util.rendering.FDRenderUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class FDModelItemRenderer extends BlockEntityWithoutLevelRenderer {

    public List<FDModel> models;

    public FDModelItemRendererOptions options;

    public FDModelItemRenderer(FDModelItemRendererOptions options) {
        super(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());
        this.options = options;
    }

    @Override
    public void renderByItem(ItemStack stack, ItemDisplayContext displayContext, PoseStack matrices, MultiBufferSource buffer, int packedLight, int packedOverlay) {


        var currentContext = FDItemAnimationHandler.currentRenderedContext;

        FDItemAnimationSystem animationSystem;

        if (currentContext != null){
            animationSystem = FDItemAnimationHandler.getItemAnimationSystem(currentContext);
        }else{
            var emptyContext = new EmptyItemStackContext(stack);
            FDItemAnimationHandler.tellIAmCurrentlyRendering(emptyContext, false);
            animationSystem = FDItemAnimationHandler.getItemAnimationSystem(emptyContext);
        }


        matrices.pushPose();
        this.initModelsIfNecessary();
        float scale = options.scale.apply(displayContext);

        if (displayContext == ItemDisplayContext.GUI){
            Vector3f translation = options.translation.apply(displayContext);
            matrices.translate(0.5 + translation.x, 0.25f + translation.y, 0.5 + translation.z);
            matrices.mulPose(Axis.XP.rotationDegrees(35));
            matrices.mulPose(Axis.YP.rotationDegrees(45));

            Vector3f rotation = options.rotation.apply(displayContext);
            matrices.mulPose(Axis.ZP.rotationDegrees(rotation.z));
            matrices.mulPose(Axis.YP.rotationDegrees(rotation.y));
            matrices.mulPose(Axis.XP.rotationDegrees(rotation.x));

        }else {
            Vector3f translation = options.translation.apply(displayContext);
            if (displayContext == ItemDisplayContext.GROUND) {
                matrices.translate(0.5 + translation.x, 0.3 + translation.y, 0.5 + translation.z);
            }else{
                matrices.translate(0.5 + translation.z, 0.5 + translation.y, 0.5 + translation.x);
            }

            Vector3f rotation = options.rotation.apply(displayContext);
            matrices.mulPose(Axis.ZP.rotationDegrees(rotation.z));
            matrices.mulPose(Axis.YP.rotationDegrees(rotation.y));
            matrices.mulPose(Axis.XP.rotationDegrees(rotation.x));

        }
        matrices.scale(scale,scale,scale);

        var fdoptions = this.options.fdItemModelOptions;

        for (int i = 0; i < models.size();i++){
            FDModel model = models.get(i);

            model.resetTransformations();
            animationSystem.applyAnimations(model, FDRenderUtil.tryGetPartialTickIgnorePause());

            var option = fdoptions.get(i);

            RenderType renderType = option.getRenderType().renderType(displayContext, stack);
            VertexConsumer vertex = buffer.getBuffer(renderType);

            FDColor color = option.getFdItemColor().color(displayContext, stack);

            model.render(matrices,vertex,packedLight,OverlayTexture.NO_OVERLAY,color.r,color.g,color.b,color.a);
        }
        matrices.popPose();

        if (options.freeItemRenderer != null){
            options.freeItemRenderer.render(stack, displayContext, matrices, buffer, packedLight, packedOverlay);
        }

    }

    private void initModelsIfNecessary(){
        if (models == null){
            models = new ArrayList<>();
            for (var sup : this.options.fdItemModelOptions){
                models.add(new FDModel(sup.getModelInfo().get()));
            }
        }
    }


    public static IClientItemExtensions createExtensions(FDModelItemRendererOptions options){
        return new IClientItemExtensions() {

            private FDModelItemRenderer renderer = new FDModelItemRenderer(options);

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return renderer;
            }

        };
    }

}


