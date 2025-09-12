package com.finderfeed.fdlib.systems.bedrock.animations.animation_system.model_system.attachments;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;

public interface ModelAttachment<M extends ModelAttachment<M,T>, T extends ModelAttachmentData<M>> extends INBTSerializable<CompoundTag> {

    T attachmentData();

}
