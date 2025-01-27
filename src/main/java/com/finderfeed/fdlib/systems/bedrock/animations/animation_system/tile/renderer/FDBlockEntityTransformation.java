package com.finderfeed.fdlib.systems.bedrock.animations.animation_system.tile.renderer;

import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.AnimatedObject;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.level.block.entity.BlockEntity;

@FunctionalInterface
public interface FDBlockEntityTransformation <T extends BlockEntity & AnimatedObject> {

    void apply(T blockEntity, PoseStack stack, float partialTicks);

}
