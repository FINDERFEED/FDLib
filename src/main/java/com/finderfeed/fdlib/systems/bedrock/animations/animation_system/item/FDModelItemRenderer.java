package com.finderfeed.fdlib.systems.bedrock.animations.animation_system.item;

import com.finderfeed.fdlib.systems.bedrock.models.FDModel;
import com.finderfeed.fdlib.util.rendering.FDRenderUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
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

        FDItemAnimationHandler.tellItemThatItIsAlive(stack);
        var animationSystem = FDItemAnimationHandler.getItemAnimationSystem(stack);

        this.initModelsIfNecessary();
        float scale = options.scale.apply(displayContext);

        if (displayContext == ItemDisplayContext.GUI){
            Vector3f translation = options.translation.apply(displayContext);
            matrices.translate(0.5 + translation.x, 0.25f + translation.y, 0.5 + translation.z);
            matrices.mulPose(Axis.XP.rotationDegrees(35));
            matrices.mulPose(Axis.YP.rotationDegrees(45 + options.rotation.apply(displayContext)));
        }else {
            Vector3f translation = options.translation.apply(displayContext);
            if (displayContext == ItemDisplayContext.GROUND) {
                matrices.translate(0.5 + translation.x, 0.3 + translation.y, 0.5 + translation.z);
            }else{
                matrices.translate(0.5 + translation.z, 0.5 + translation.y, 0.5 + translation.x);
            }
            matrices.mulPose(Axis.YP.rotationDegrees(options.rotation.apply(displayContext)));
        }
        matrices.scale(scale,scale,scale);

        for (int i = 0; i < models.size();i++){
            FDModel model = models.get(i);

            model.resetTransformations();
            animationSystem.applyAnimations(model, FDRenderUtil.getPartialTickWithPause());

            VertexConsumer vertex = buffer.getBuffer(options.renderTypes.get(i));

            model.render(matrices,vertex,packedLight,OverlayTexture.NO_OVERLAY,1f,1f,1f,1f);
        }
    }

    private void initModelsIfNecessary(){
        if (models == null){
            models = new ArrayList<>();
            for (var sup : this.options.modelInfos){
                models.add(new FDModel(sup.get()));
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


