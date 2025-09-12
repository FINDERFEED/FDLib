package com.finderfeed.fdlib.systems.bedrock.animations.animation_system.model_system.attachments.instances.item_stack;

import com.finderfeed.fdlib.init.FDModelAttachmentTypes;
import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.model_system.attachments.BaseModelAttachmentData;
import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.model_system.attachments.ModelAttachmentData;
import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.model_system.attachments.ModelAttachmentType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.NetworkCodec;
import net.minecraft.network.codec.NetworkCodec;
import net.minecraft.world.item.ItemStack;

public class ItemStackAttachmentData implements ModelAttachmentData<ItemStackAttachment> {

    public static final NetworkCodec<? super FriendlyByteBuf, ItemStackAttachmentData> CODEC = NetworkCodec.composite(
            BaseModelAttachmentData.CODEC,v->v.baseModelAttachmentData,
            ItemStack.STREAM_CODEC,v->v.itemStack,
            ItemStackAttachmentData::new
    );

    private ItemStack itemStack;
    private BaseModelAttachmentData baseModelAttachmentData;

    public ItemStackAttachmentData(BaseModelAttachmentData baseModelAttachmentData, ItemStack itemStack){
        this.itemStack = itemStack;
        this.baseModelAttachmentData = baseModelAttachmentData;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public BaseModelAttachmentData getBaseModelAttachmentData() {
        return baseModelAttachmentData;
    }

    @Override
    public ModelAttachmentType<ItemStackAttachment, ?> type() {
        return FDModelAttachmentTypes.ITEM_STACK_MODEL_ATTACHMENT.get();
    }

}
