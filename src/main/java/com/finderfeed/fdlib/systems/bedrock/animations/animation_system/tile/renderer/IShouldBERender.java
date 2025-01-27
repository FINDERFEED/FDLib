package com.finderfeed.fdlib.systems.bedrock.animations.animation_system.tile.renderer;

import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.AnimatedObject;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec3;

@FunctionalInterface
public interface IShouldBERender<T extends BlockEntity & AnimatedObject> {

    boolean shouldRender(T blockEntity, Vec3 idk);

}
