package com.finderfeed.fdlib.systems.bedrock.animations.animation_system.entity.renderer;

import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.AnimatedObject;
import net.minecraft.world.entity.Entity;

@FunctionalInterface
public interface FDEntityIgnoreYaw<T extends Entity & AnimatedObject> {

    boolean ignoreYaw(T entity, float pticks);

}
