package com.finderfeed.fdlib.systems.bedrock.animations.animation_system.model_system.attachments.instances.item_stack;

import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.model_system.attachments.BaseModelAttachmentData;
import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.model_system.attachments.ModelAttachmentRenderer;
import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.model_system.attachments.ModelAttachmentType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.NetworkCodec;
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
    public NetworkCodec<? super FriendlyByteBuf, ItemStackAttachmentData> dataNetworkCodec() {
        return ItemStackAttachmentData.CODEC;
    }

    @Override
    public ModelAttachmentRenderer<ItemStackAttachment> renderer() {
        return ItemStackAttachmentRenderer.INSTANCE;
    }

}
