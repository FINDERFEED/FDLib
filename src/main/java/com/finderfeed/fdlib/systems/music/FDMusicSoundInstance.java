package com.finderfeed.fdlib.systems.music;

import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;

public class FDMusicSoundInstance extends SimpleSoundInstance {

    public float initialChannelVolume;

    public FDMusicSoundInstance(SoundEvent soundEvent, float initialChannelVolume) {
        super(soundEvent.getLocation(),
                SoundSource.MUSIC,
                1.0F,
                1.0F,
                SoundInstance.createUnseededRandom(),
                false,
                0,
                SoundInstance.Attenuation.NONE,
                0.0,
                0.0,
                0.0,
                true
        );
        this.initialChannelVolume = initialChannelVolume;
    }

}
