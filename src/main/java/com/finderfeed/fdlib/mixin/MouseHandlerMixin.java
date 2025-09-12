package com.finderfeed.fdlib.mixin;

import com.finderfeed.fdlib.systems.cutscenes.CutsceneCameraHandler;
import net.minecraft.client.MouseHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MouseHandler.class)
public class MouseHandlerMixin {

    @Inject(method = "turnPlayer", at = @At("HEAD"), cancellable = true)
    public void turnPlayer(CallbackInfo ci){
        if (CutsceneCameraHandler.isCutsceneActive()){
            ci.cancel();
        }
    }

}
