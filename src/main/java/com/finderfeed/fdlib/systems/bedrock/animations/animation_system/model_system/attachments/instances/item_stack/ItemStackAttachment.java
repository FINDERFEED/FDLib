package com.finderfeed.fdlib.systems.bedrock.animations.animation_system.model_system.attachments.instances.item_stack;

import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.model_system.attachments.BaseModelAttachmentData;
import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.model_system.attachments.ModelAttachment;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.UnknownNullability;

public class ItemStackAttachment implements ModelAttachment<ItemStackAttachment, ItemStackAttachmentData> {

    private ItemStackAttachmentData data;

    public ItemStackAttachment(ItemStackAttachmentData itemStackAttachmentData){
        this.data = itemStackAttachmentData;
    }

    public ItemStack getItemStack() {
        return data.getItemStack();
    }

    public BaseModelAttachmentData getBaseModelAttachmentData() {
        return data.getBaseModelAttachmentData();
    }

    @Override
    public CompoundTag serializeNBT() {

        CompoundTag itemStackAttachment = new CompoundTag();

        CompoundTag stack = this.getItemStack().save(new CompoundTag());

        CompoundTag base = this.getBaseModelAttachmentData().serializeNBT();

        itemStackAttachment.put("item",stack);
        itemStackAttachment.put("base",base);

        return itemStackAttachment;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {

        CompoundTag item = nbt.getCompound("item");

        ItemStack itemStack = ItemStack.of(item);

        BaseModelAttachmentData baseModelAttachmentData = new BaseModelAttachmentData();
        baseModelAttachmentData.deserializeNBT(nbt.getCompound("base"));

        this.data = new ItemStackAttachmentData(baseModelAttachmentData,itemStack);

    }

    @Override
    public ItemStackAttachmentData attachmentData() {
        return data;
    }

}
