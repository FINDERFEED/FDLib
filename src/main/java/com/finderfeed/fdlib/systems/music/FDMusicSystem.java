package com.finderfeed.fdlib.systems.music;

import com.finderfeed.fdlib.FDLib;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.sounds.SoundEngine;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import org.lwjgl.openal.AL10;
import org.lwjgl.openal.AL11;

@EventBusSubscriber(modid = FDLib.MOD_ID, value = Dist.CLIENT)
public class FDMusicSystem {

    @SubscribeEvent
    public static void tickEvent(ClientTickEvent.Pre event){

        if (Minecraft.getInstance().isPaused()) return;

        SoundEvent soundEvent = SoundEvents.ALLAY_AMBIENT_WITH_ITEM;

        SoundManager soundManager = Minecraft.getInstance().getSoundManager();

        SoundEngine soundEngine = soundManager.soundEngine;

        var instanceToChannel = soundEngine.instanceToChannel;

    }



}
