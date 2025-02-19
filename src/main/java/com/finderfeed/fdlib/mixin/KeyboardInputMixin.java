package com.finderfeed.fdlib.mixin;

import com.finderfeed.fdlib.systems.cutscenes.CutsceneCameraHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.KeyboardInput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KeyboardInput.class)
public class KeyboardInputMixin {

    @Inject(method = "tick",at = @At("TAIL"), cancellable = true)
    public void tick(boolean p_234118_, float p_234119_, CallbackInfo ci){
        if (CutsceneCameraHandler.isCutsceneActive() && Minecraft.getInstance().player != null){

            CutsceneCameraHandler.nullifyInput(Minecraft.getInstance().player);

            ci.cancel();
        }
    }

}
