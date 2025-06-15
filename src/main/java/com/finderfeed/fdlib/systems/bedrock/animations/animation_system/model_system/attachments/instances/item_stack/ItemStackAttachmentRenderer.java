package com.finderfeed.fdlib.systems.bedrock.animations.animation_system.model_system.attachments.instances.item_stack;

import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.model_system.ModelSystem;
import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.model_system.attachments.BaseModelAttachmentData;
import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.model_system.attachments.ModelAttachmentRenderContext;
import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.model_system.attachments.ModelAttachmentRenderer;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.joml.Quaternionf;

public class ItemStackAttachmentRenderer extends ModelAttachmentRenderer<ItemStackAttachment> {

    public static final ItemStackAttachmentRenderer INSTANCE = new ItemStackAttachmentRenderer();

    @Override
    public void render(ItemStackAttachment attachment, ModelAttachmentRenderContext ctx, ModelSystem modelSystem, PoseStack poseStack, MultiBufferSource src, float partialTicks, int light, int overlay) {

        ItemStack itemStack = attachment.getItemStack();

        BaseModelAttachmentData data = attachment.getBaseModelAttachmentData();

        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();

        LivingEntity livingEntity = null;

        if (ctx.getEntity() instanceof LivingEntity livingEntity1){
            livingEntity = livingEntity1;
        }

        poseStack.pushPose();

        poseStack.translate(data.getTranslationX(),data.getTranslationY(),data.getTranslationZ());

        poseStack.mulPose(new Quaternionf().rotationZYX(data.getRotationZ(),data.getRotationY(),data.getRotationX()));

        poseStack.scale(data.getScaleX(),data.getScaleY(),data.getScaleZ());

        itemRenderer.render(itemStack, ItemDisplayContext.FIXED, false, poseStack, src, light, overlay, itemRenderer.getModel(itemStack,Minecraft.getInstance().level,livingEntity, 0));

        poseStack.popPose();
    }

}
