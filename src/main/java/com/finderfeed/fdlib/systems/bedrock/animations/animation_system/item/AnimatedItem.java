package com.finderfeed.fdlib.systems.bedrock.animations.animation_system.item;

import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.item.animated_item.AnimatedItemStackContext;
import net.minecraft.world.item.ItemStack;

public interface AnimatedItem {

    void animatedItemTick(AnimatedItemStackContext itemStack);

}
