package com.finderfeed.fdlib.mixin;

import com.finderfeed.fdlib.systems.post_shaders.FDPostShadersHandler;
import com.finderfeed.fdlib.systems.post_shaders.FDPostShadersReloadableResourceListener;
import net.minecraft.client.Minecraft;
import net.minecraft.client.main.GameConfig;
import net.minecraft.server.packs.resources.ReloadInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Minecraft.class)
public class MinecraftMixin {

    @Inject(method = "onGameLoadFinished", at = @At("TAIL"))
    public void init(CallbackInfo ci) {
        if (!FDPostShadersHandler.WAS_LOADED_ONCE) {
            FDPostShadersReloadableResourceListener.initializeShaders();
            FDPostShadersHandler.WAS_LOADED_ONCE = true;
        }
    }


}
