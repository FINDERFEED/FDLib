package com.finderfeed.fdlib.systems.bedrock.animations.animation_system.model_system.attachments.instances.item_stack;

import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.model_system.attachments.BaseModelAttachmentData;
import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.model_system.attachments.ModelAttachmentRenderer;
import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.model_system.attachments.ModelAttachmentType;
import com.finderfeed.fdlib.systems.stream_codecs.NetworkCodec;
import net.minecraft.world.item.ItemStack;

public class ItemStackAttachmentType implements ModelAttachmentType<ItemStackAttachment, ItemStackAttachmentData> {

    @Override
    public ItemStackAttachment attachmentFromData(ItemStackAttachmentData data) {
        ItemStackAttachment itemStackAttachment = new ItemStackAttachment(data);
        return itemStackAttachment;
    }

    @Override
    public ItemStackAttachment createInstance() {
        return new ItemStackAttachment(new ItemStackAttachmentData(new BaseModelAttachmentData(), ItemStack.EMPTY));
    }

    @Override
    public NetworkCodec<ItemStackAttachmentData> dataNetworkCodec() {
        return ItemStackAttachmentData.CODEC;
    }

    @Override
    public ModelAttachmentRenderer<ItemStackAttachment> renderer() {
        return ItemStackAttachmentRenderer.INSTANCE;
    }

}
