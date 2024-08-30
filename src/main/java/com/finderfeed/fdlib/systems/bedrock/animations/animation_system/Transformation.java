package com.finderfeed.fdlib.systems.bedrock.animations.animation_system;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.entity.Entity;

@FunctionalInterface
public interface Transformation<T extends Entity & AnimatedObject> {

    void apply(T entity, PoseStack stack,float partialTicks);

}
