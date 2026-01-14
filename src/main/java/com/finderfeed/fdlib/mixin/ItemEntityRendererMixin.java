package com.finderfeed.fdlib.mixin;

import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.item.AnimatedItem;
import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.item.FDItemAnimationHandler;
import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.item.animated_item.ItemEntityStackContext;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemEntityRenderer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemEntityRenderer.class)
public class ItemEntityRendererMixin {

    @Inject(method = "render(Lnet/minecraft/world/entity/item/ItemEntity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/ItemRenderer;render(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemDisplayContext;ZLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;IILnet/minecraft/client/resources/model/BakedModel;)V"))
    public void render(ItemEntity itemEntity, float p_115037_, float p_115038_, PoseStack p_115039_, MultiBufferSource p_115040_, int p_115041_, CallbackInfo ci, @Local ItemStack itemStack){
        if (itemStack.getItem() instanceof AnimatedItem){
            ItemEntityStackContext context = new ItemEntityStackContext(itemEntity, itemStack);
            FDItemAnimationHandler.tellIAmCurrentlyRendering(context, true);
        }
    }

    @Inject(method = "render(Lnet/minecraft/world/entity/item/ItemEntity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/ItemRenderer;render(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemDisplayContext;ZLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;IILnet/minecraft/client/resources/model/BakedModel;)V", shift = At.Shift.AFTER))
    public void renderAfter(ItemEntity itemEntity, float p_115037_, float p_115038_, PoseStack p_115039_, MultiBufferSource p_115040_, int p_115041_, CallbackInfo ci, @Local ItemStack itemStack){
        FDItemAnimationHandler.currentRenderedContext = null;
    }

}
