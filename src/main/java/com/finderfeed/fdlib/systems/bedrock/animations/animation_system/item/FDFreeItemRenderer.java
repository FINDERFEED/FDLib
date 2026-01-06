package com.finderfeed.fdlib.systems.bedrock.animations.animation_system.item;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public interface FDFreeItemRenderer {


    void render(ItemStack stack, ItemDisplayContext displayContext, PoseStack matrices, MultiBufferSource buffer, int packedLight, int packedOverlay);


}
