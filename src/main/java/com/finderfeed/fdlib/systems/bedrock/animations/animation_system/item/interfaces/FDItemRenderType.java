package com.finderfeed.fdlib.systems.bedrock.animations.animation_system.item.interfaces;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

@FunctionalInterface
public interface FDItemRenderType {

    RenderType renderType(ItemDisplayContext context, ItemStack itemStack);

}
