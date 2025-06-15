package com.finderfeed.fdlib.systems.bedrock.animations.animation_system.model_system.attachments;

import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.model_system.ModelSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;

public abstract class ModelAttachmentRenderer<T extends ModelAttachment<T,?>> {

    public abstract void render(T attachment, ModelAttachmentRenderContext ctx, ModelSystem modelSystem, PoseStack poseStack, MultiBufferSource src, float partialTicks, int light, int overlay);

}
