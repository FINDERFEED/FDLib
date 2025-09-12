package com.finderfeed.fdlib.systems.bedrock.animations.animation_system.model_system.attachments;

import com.finderfeed.fdlib.systems.stream_codecs.NetworkCodec;

public interface ModelAttachmentType<A extends ModelAttachment<A,T>,T extends ModelAttachmentData<A>> {

    A attachmentFromData(T data);

    A createInstance();

    NetworkCodec<T> dataNetworkCodec();

    ModelAttachmentRenderer<A> renderer();

}
