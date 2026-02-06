package com.finderfeed.fdlib.systems.bedrock.animations.animation_system.model_system.attachments.instances.particle;

import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.model_system.attachments.ModelAttachmentRenderer;
import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.model_system.attachments.ModelAttachmentType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public class FDParticleAttachmentType implements ModelAttachmentType<FDParticleAttachment, FDParticleAttachmentData> {

    @Override
    public FDParticleAttachment attachmentFromData(FDParticleAttachmentData data) {
        return new FDParticleAttachment(data);
    }

    @Override
    public FDParticleAttachment createInstance() {
        return new FDParticleAttachment();
    }

    @Override
    public StreamCodec<? super RegistryFriendlyByteBuf, FDParticleAttachmentData> dataStreamCodec() {
        return FDParticleAttachmentData.STREAM_CODEC;
    }

    @Override
    public ModelAttachmentRenderer<FDParticleAttachment> renderer() {
        return FDParticleAttachmentRenderer.INSTANCE;
    }

}
