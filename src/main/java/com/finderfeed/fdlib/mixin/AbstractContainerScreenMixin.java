package com.finderfeed.fdlib.mixin;

import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.item.AnimatedItem;
import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.item.FDItemAnimationHandler;
import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.item.animated_item.ItemStackInSlotContext;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractContainerScreen.class)
public class AbstractContainerScreenMixin {

    @Inject(method = "renderSlot", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/inventory/AbstractContainerScreen;renderSlotContents(Lnet/minecraft/client/gui/GuiGraphics;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/inventory/Slot;Ljava/lang/String;)V"))
    public void renderSlot(GuiGraphics p_281607_, Slot slot, CallbackInfo ci, @Local(ordinal = 0) ItemStack itemStack){
        if (itemStack.getItem() instanceof AnimatedItem){
            ItemStackInSlotContext context = new ItemStackInSlotContext(slot, itemStack);
            FDItemAnimationHandler.tellIAmCurrentlyRendering(context, true);
        }
    }

    @Inject(method = "renderSlot", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/inventory/AbstractContainerScreen;renderSlotContents(Lnet/minecraft/client/gui/GuiGraphics;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/inventory/Slot;Ljava/lang/String;)V", shift = At.Shift.AFTER))
    public void renderSlotAfter(GuiGraphics p_281607_, Slot slot, CallbackInfo ci){
        FDItemAnimationHandler.currentRenderedContext = null;
    }

}
