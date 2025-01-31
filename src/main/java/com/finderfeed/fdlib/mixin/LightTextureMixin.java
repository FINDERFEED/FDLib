package com.finderfeed.fdlib.mixin;

import com.finderfeed.fdlib.systems.impact_frames.ImpactFramesHandler;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.LightTexture;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LightTexture.class)
public class LightTextureMixin {


    @Inject(method = "clampColor",at = @At("HEAD"),cancellable = true)
    private static void clampColor(Vector3f light, CallbackInfo ci){
        ImpactFramesHandler.lightTextureMixin(light,ci);
    }
}
