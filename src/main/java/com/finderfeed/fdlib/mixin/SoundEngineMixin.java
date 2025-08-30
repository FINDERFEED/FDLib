package com.finderfeed.fdlib.mixin;

import com.finderfeed.fdlib.systems.music.FDMusicSoundInstance;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.ChannelAccess;
import net.minecraft.client.sounds.SoundEngine;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SoundEngine.class)
public class SoundEngineMixin {

    @Inject(method = "play", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/sounds/ChannelAccess$ChannelHandle;execute(Ljava/util/function/Consumer;)V", shift = At.Shift.AFTER))
    public void play(SoundInstance instance, CallbackInfo ci, @Local ChannelAccess.ChannelHandle channelHandle){
        if (instance instanceof FDMusicSoundInstance fdMusicSoundInstance){
            channelHandle.execute(channel -> {
                channel.setVolume(fdMusicSoundInstance.getVolume());
            });
        }
    }

}
