package com.finderfeed.fdlib.mixin;

import com.finderfeed.fdlib.systems.music.FDMusicSystem;
import com.mojang.blaze3d.audio.Channel;
import net.minecraft.client.sounds.ChannelAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

@Mixin(ChannelAccess.ChannelHandle.class)
public class ChannelAccessMixin {

    @Shadow @Nullable private Channel channel;

    @Inject(method = "release", at = @At("HEAD"))
    public void release(CallbackInfo ci) {
        if (channel != null){
            FDMusicSystem.StreamingSourcesBufferLengthCache.onChannelHandleRelease(channel.source);
        }
    }

}
