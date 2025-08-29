package com.finderfeed.fdlib.systems.music;

import com.finderfeed.fdlib.FDClientHelpers;
import com.finderfeed.fdlib.FDLib;
import com.finderfeed.fdlib.systems.music.data.FDMusicData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.ChannelAccess;
import net.minecraft.client.sounds.SoundEngine;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RenderFrameEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import org.lwjgl.openal.AL10;
import org.lwjgl.openal.AL11;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@EventBusSubscriber(modid = FDLib.MOD_ID, value = Dist.CLIENT)
public class FDMusicSystem {

    private static final HashMap<UUID, FDMusic> ACTIVE_MUSIC = new HashMap<>();

    private static final int MAX_MUSIC_FADE_IN_OUT = 30;

    private int fadeInOutTicker = MAX_MUSIC_FADE_IN_OUT;

    @SubscribeEvent
    public static void tickEvent(ClientTickEvent.Pre event){

        if (Minecraft.getInstance().isPaused()) return;

        muteAllMusicBesidesFDMusic();
        tickMusic();

    }

    private static void muteAllMusicBesidesFDMusic(){
        if (!ACTIVE_MUSIC.isEmpty()) {
            SoundEngine soundEngine = FDClientHelpers.getSoundEngine();
            var instanceToChannel = soundEngine.instanceToChannel;
            var instances = getAllFDSoundInstances();
            for (var i : instanceToChannel.entrySet()){
                SoundInstance soundInstance = i.getKey();

                if (soundInstance.getSource() != SoundSource.MUSIC) continue;

                if (!instances.contains(soundInstance)){
                    ChannelAccess.ChannelHandle channelHandle = i.getValue();
                    channelHandle.execute(channel -> {
                        channel.setVolume(0);
                    });
                }

            }
        }
    }

    private static List<SoundInstance> getAllFDSoundInstances(){
        List<SoundInstance> instances = new ArrayList<>();
        for (var inst : ACTIVE_MUSIC.values()){
            instances.addAll(inst.allSoundInstances());
        }
        return instances;
    }



    public static void addMusic(FDMusicData fdMusicData){
        if (!ACTIVE_MUSIC.containsKey(fdMusicData.getMusicSourceUUID())){
            ACTIVE_MUSIC.put(fdMusicData.getMusicSourceUUID(), new FDMusic(fdMusicData));
        }
    }


    private static void tickMusic(){
        for (var music : ACTIVE_MUSIC.values()){
            music.tick();
        }
    }

    @SubscribeEvent
    public static void clearSounds(ClientPlayerNetworkEvent.LoggingOut event){
        ACTIVE_MUSIC.clear();
    }

    @SubscribeEvent
    public static void renderTickEvent(RenderFrameEvent.Pre event){
        musicRenderTick(event.getPartialTick().getGameTimeDeltaPartialTick(false));
    }

    public static void musicRenderTick(float pticks){
        for (var music : ACTIVE_MUSIC.values()){
            music.renderTick(pticks);
        }
    }



}
