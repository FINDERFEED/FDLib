package com.finderfeed.fdlib.systems.bedrock.animations.animation_system.model_system.attachments.instances.particle;

import com.finderfeed.fdlib.init.FDModelAttachmentTypes;
import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.model_system.attachments.ModelAttachmentData;
import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.model_system.attachments.ModelAttachmentType;
import com.finderfeed.fdlib.util.FDByteBufCodecs;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import org.joml.Vector3f;

public class FDParticleAttachmentData implements ModelAttachmentData<FDParticleAttachment> {

    public static final StreamCodec<RegistryFriendlyByteBuf, FDParticleAttachmentData> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VECTOR3F, v->v.offset,
            FDParticleAttachmentData::new
    );

    public Vector3f offset;

    public FDParticleAttachmentData(Vector3f offset) {
        this.offset = offset;
    }

    @Override
    public ModelAttachmentType<FDParticleAttachment, ?> type() {
        return FDModelAttachmentTypes.PARTICLE_ATTACHMENT.get();
    }

}
