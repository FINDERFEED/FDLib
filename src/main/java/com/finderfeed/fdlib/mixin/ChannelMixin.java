package com.finderfeed.fdlib.mixin;

import com.finderfeed.fdlib.systems.music.FDMusicSystem;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import com.mojang.blaze3d.audio.Channel;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Channel.class)
public class ChannelMixin {


    @Shadow @Final public int source;

    @Inject(method = "removeProcessedBuffers", at = @At(value = "INVOKE", target = "Lorg/lwjgl/openal/AL10;alSourceUnqueueBuffers(I[I)V", shift = At.Shift.AFTER))
    public void removeProcessedBuffers(CallbackInfoReturnable<Integer> cir, @Local int[] buffers){
        FDMusicSystem.StreamingSourcesBufferLengthCache.channelMixin(this.source, buffers);
    }


}
