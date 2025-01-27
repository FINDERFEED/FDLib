package com.finderfeed.fdlib.systems.bedrock.animations.animation_system.tile.renderer;

import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.AnimatedObject;
import net.minecraft.world.level.block.entity.BlockEntity;

@FunctionalInterface
public interface IBERenderOffScreen<T extends BlockEntity & AnimatedObject> {

    boolean shouldRenderOffScreen(T blockEntity);


}
