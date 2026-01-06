package com.finderfeed.fdlib.systems.bedrock.animations.animation_system.item.animated_item;

import net.minecraft.world.item.ItemStack;

import java.util.Objects;

public abstract class AnimatedItemStackContext {

    private ItemStack itemStack;

    public AnimatedItemStackContext(ItemStack itemStack){
        this.itemStack = itemStack;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AnimatedItemStackContext that = (AnimatedItemStackContext) o;
        return itemStack.getItem() == that.itemStack.getItem();
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(itemStack.getItem());
    }

}
