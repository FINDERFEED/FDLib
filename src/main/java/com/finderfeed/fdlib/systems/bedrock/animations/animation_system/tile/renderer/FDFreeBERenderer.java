package com.finderfeed.fdlib.systems.bedrock.animations.animation_system.tile.renderer;

import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.AnimatedObject;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.level.block.entity.BlockEntity;

@FunctionalInterface
public interface FDFreeBERenderer<T extends BlockEntity & AnimatedObject> {

    void render(T blockEntity, float pticks, PoseStack matrices, MultiBufferSource src, int light, int overlay);

}
