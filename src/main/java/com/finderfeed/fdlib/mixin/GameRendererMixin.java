package com.finderfeed.fdlib.mixin;


import com.finderfeed.fdlib.ClientMixinHandler;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.renderer.GameRenderer;
import org.joml.Matrix4f;
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


    @Inject(method = "renderItemInHand",at = @At("HEAD"))
    public void renderItemInHand(Camera p_109122_, float p_109123_, Matrix4f p_333953_, CallbackInfo ci){
        ClientMixinHandler.beforeHand();
    }

    @Inject(method = "renderLevel",at = @At("HEAD"))
    public void renderLevel(DeltaTracker p_348589_, CallbackInfo ci){
        ClientMixinHandler.beforeLevel();
    }




}
