package com.finderfeed.fdlib.mixin;


import com.finderfeed.fdlib.ClientMixinHandler;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class GameRendererMixin {

    @Inject(method = "bobHurt",at = @At("HEAD"))
    public void bobHurt(PoseStack matrices, float pticks, CallbackInfo ci){
        ClientMixinHandler.bobHurt(matrices,pticks);
    }

}
