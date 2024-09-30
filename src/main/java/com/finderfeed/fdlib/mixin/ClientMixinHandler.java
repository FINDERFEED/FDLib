package com.finderfeed.fdlib.mixin;

import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LightTexture;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public class ClientMixinHandler {



    public static void renderLevel(DeltaTracker deltaTracker, boolean idk, Camera camera, GameRenderer renderer, LightTexture lightTexture, Matrix4f modelview, Matrix4f projection, CallbackInfo ci){
        
    }

}
