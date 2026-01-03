package com.finderfeed.fdlib.systems.bedrock.animations.animation_system.item.interfaces;

import com.finderfeed.fdlib.util.FDColor;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

@FunctionalInterface
public interface FDItemColor {

    FDColor color(ItemDisplayContext ctx, ItemStack itemStack);

}
