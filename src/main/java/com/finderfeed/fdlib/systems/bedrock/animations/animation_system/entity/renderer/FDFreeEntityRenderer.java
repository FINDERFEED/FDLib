package com.finderfeed.fdlib.systems.bedrock.animations.animation_system.entity.renderer;

import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.AnimatedObject;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.entity.Entity;

@FunctionalInterface
public interface FDFreeEntityRenderer<T extends Entity & AnimatedObject> {

    void render(T entity, float yaw, float partialTicks, PoseStack matrices, MultiBufferSource src, int light);

}
