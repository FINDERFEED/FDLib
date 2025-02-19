package com.finderfeed.fdlib.mixin;

import com.finderfeed.fdlib.systems.cutscenes.CutsceneCameraHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LocalPlayer.class)
public class LocalPlayerMixin {

    @Inject(method = "isControlledCamera", at = @At("HEAD"), cancellable = true)
    public void isControlledCamera(CallbackInfoReturnable<Boolean> cir){
        if (CutsceneCameraHandler.isCutsceneActive()){
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "serverAiStep", at = @At("HEAD"))
    public void serverAIStep(CallbackInfo ci){
        if (CutsceneCameraHandler.isCutsceneActive()){
            CutsceneCameraHandler.nullifyInput((LocalPlayer) (Object) this);
        }
    }

}
