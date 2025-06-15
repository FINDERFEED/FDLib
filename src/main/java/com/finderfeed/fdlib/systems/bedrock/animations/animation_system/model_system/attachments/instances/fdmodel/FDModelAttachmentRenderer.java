package com.finderfeed.fdlib.systems.bedrock.animations.animation_system.model_system.attachments.instances.fdmodel;

import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.model_system.ModelSystem;
import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.model_system.attachments.BaseModelAttachmentData;
import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.model_system.attachments.ModelAttachmentRenderContext;
import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.model_system.attachments.ModelAttachmentRenderer;
import com.finderfeed.fdlib.systems.bedrock.models.FDModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import org.joml.Quaternionf;

public class FDModelAttachmentRenderer extends ModelAttachmentRenderer<FDModelAttachment> {

    public static final FDModelAttachmentRenderer INSTANCE = new FDModelAttachmentRenderer();

    @Override
    public void render(FDModelAttachment attachment, ModelAttachmentRenderContext ctx, ModelSystem modelSystem, PoseStack poseStack, MultiBufferSource src, float partialTicks, int light, int overlay) {

        FDModel fdModel = attachment.getFdModel();

        modelSystem.getAnimationSystem().applyAnimations(fdModel, partialTicks);

        RenderType renderType = attachment.getRenderType().factory().create(attachment.getTexture());

        VertexConsumer vertexConsumer = src.getBuffer(renderType);

        BaseModelAttachmentData data = attachment.getData();

        poseStack.pushPose();

        poseStack.translate(data.getTranslationX(),data.getTranslationY(),data.getTranslationZ());

        poseStack.mulPose(new Quaternionf().rotationZYX(data.getRotationZ(),data.getRotationY(),data.getRotationX()));

        poseStack.scale(data.getScaleX(),data.getScaleY(),data.getScaleZ());

        fdModel.render(poseStack, vertexConsumer, light, overlay, 1f, 1f, 1f, 1f);

        poseStack.popPose();
    }

}
