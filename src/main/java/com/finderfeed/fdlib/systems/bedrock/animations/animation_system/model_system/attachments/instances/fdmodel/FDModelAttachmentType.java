package com.finderfeed.fdlib.systems.bedrock.animations.animation_system.model_system.attachments.instances.fdmodel;

import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.model_system.attachments.ModelAttachmentRenderer;
import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.model_system.attachments.ModelAttachmentType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public class FDModelAttachmentType implements ModelAttachmentType<FDModelAttachment, FDModelAttachmentData> {
    @Override
    public FDModelAttachment attachmentFromData(FDModelAttachmentData data) {
        return new FDModelAttachment(data);
    }

    @Override
    public FDModelAttachment createInstance() {
        return new FDModelAttachment();
    }

    @Override
    public StreamCodec<? super RegistryFriendlyByteBuf, FDModelAttachmentData> dataStreamCodec() {
        return FDModelAttachmentData.STREAM_CODEC;
    }

    @Override
    public ModelAttachmentRenderer<FDModelAttachment> renderer() {
        return FDModelAttachmentRenderer.INSTANCE;
    }
}
