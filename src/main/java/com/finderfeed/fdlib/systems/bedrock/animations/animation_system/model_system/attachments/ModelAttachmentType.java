package com.finderfeed.fdlib.systems.bedrock.animations.animation_system.model_system.attachments;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public interface ModelAttachmentType<A extends ModelAttachment<A,T>,T extends ModelAttachmentData<A>> {

    A attachmentFromData(T data);

    A createInstance();

    StreamCodec<? super RegistryFriendlyByteBuf, T> dataStreamCodec();

    ModelAttachmentRenderer<A> renderer();

}
