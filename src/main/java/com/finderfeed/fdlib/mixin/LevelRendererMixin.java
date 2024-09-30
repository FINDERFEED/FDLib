package com.finderfeed.fdlib.mixin;


import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.LightTexture;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelRenderer.class)
public class LevelRendererMixin {


    @Inject(method = "renderLevel",at = @At("HEAD"))
    public void renderLevel(DeltaTracker deltaTracker, boolean idk, Camera camera, GameRenderer renderer, LightTexture lightTexture, Matrix4f modelview, Matrix4f projection, CallbackInfo ci){
        ClientMixinHandler.renderLevel(deltaTracker, idk, camera, renderer, lightTexture, modelview, projection, ci);
    }

}
