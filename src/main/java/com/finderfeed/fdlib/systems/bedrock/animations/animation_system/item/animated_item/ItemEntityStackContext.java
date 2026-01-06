package com.finderfeed.fdlib.systems.bedrock.animations.animation_system.item.animated_item;

import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;

import java.util.Objects;

public class ItemEntityStackContext extends AnimatedItemStackContext{

    private ItemEntity itemEntity;

    public ItemEntityStackContext(ItemEntity itemEntity, ItemStack itemStack) {
        super(itemStack);
        this.itemEntity = itemEntity;
    }


    public ItemEntity getItemEntity() {
        return itemEntity;
    }

    @Override
    public ItemStack getItemStack() {
        return super.getItemStack();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ItemEntityStackContext that = (ItemEntityStackContext) o;
        return Objects.equals(itemEntity, that.itemEntity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), itemEntity);
    }

}
