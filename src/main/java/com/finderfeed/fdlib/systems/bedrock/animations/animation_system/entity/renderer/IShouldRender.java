package com.finderfeed.fdlib.systems.bedrock.animations.animation_system.entity.renderer;

import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.AnimatedObject;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.world.entity.Entity;

@FunctionalInterface
public interface IShouldRender<T extends Entity & AnimatedObject> {

    boolean shouldRender(T entity, Frustum frustum, double x, double y, double z);

}
