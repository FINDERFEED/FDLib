package com.finderfeed.fdlib.systems.bedrock.animations.animation_system.model_system.attachments;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.NetworkCodec;

public interface ModelAttachmentType<A extends ModelAttachment<A,T>,T extends ModelAttachmentData<A>> {

    A attachmentFromData(T data);

    A createInstance();

    NetworkCodec<? super FriendlyByteBuf, T> dataNetworkCodec();

    ModelAttachmentRenderer<A> renderer();

}
