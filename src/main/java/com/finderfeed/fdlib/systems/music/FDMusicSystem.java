package com.finderfeed.fdlib.systems.music;

import com.finderfeed.fdlib.FDClientHelpers;
import com.finderfeed.fdlib.FDLib;
import com.finderfeed.fdlib.systems.music.data.FDMusicData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.ChannelAccess;
import net.minecraft.client.sounds.SoundEngine;
import net.minecraft.sounds.SoundSource;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.bus.api.SubscribeEvent;
import net.minecraftforge.fml.common.EventBusSubscriber;
import net.minecraftforge.neoforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.neoforge.client.event.ClientTickEvent;
import net.minecraftforge.neoforge.client.event.RenderFrameEvent;
import org.lwjgl.openal.AL10;
import org.lwjgl.openal.AL11;

import java.util.*;

@Mod.EventBusSubscriber(modid = FDLib.MOD_ID, value = Dist.CLIENT)
public class FDMusicSystem {

    private static final HashMap<UUID, FDMusic> ACTIVE_MUSIC = new HashMap<>();


    private static boolean musicWasPlaying = false;

    @SubscribeEvent
    public static void tickEvent(ClientTickEvent.Pre event){

        if (Minecraft.getInstance().isPaused()) return;

        muteAllMusicBesidesFDMusic();
        tickMusic();

    }

    private static void muteAllMusicBesidesFDMusic(){

        float volume = -1;

        if (!ACTIVE_MUSIC.isEmpty()) {
            musicWasPlaying = true;
            volume = 0;
        }else{
            if (musicWasPlaying){
                volume = FDClientHelpers.getCurrentMusicVolume();
                musicWasPlaying = false;
            }
        }

        if (volume != -1){
            float finalVolume = volume;
            musicWasPlaying = true;
            SoundEngine soundEngine = FDClientHelpers.getSoundEngine();
            var instanceToChannel = soundEngine.instanceToChannel;
            var instances = getAllFDSoundInstances();
            for (var i : instanceToChannel.entrySet()){
                SoundInstance soundInstance = i.getKey();

                if (soundInstance.getSource() != SoundSource.MUSIC) continue;

                if (!instances.contains(soundInstance)){
                    ChannelAccess.ChannelHandle channelHandle = i.getValue();
                    channelHandle.execute(channel -> {
                        channel.setVolume(finalVolume);
                    });
                }

            }
        }

    }

    public static FDMusic getMusic(UUID uuid){
        return ACTIVE_MUSIC.get(uuid);
    }

    private static List<SoundInstance> getAllFDSoundInstances(){
        List<SoundInstance> instances = new ArrayList<>();
        for (var inst : ACTIVE_MUSIC.values()){
            instances.addAll(inst.allSoundInstances());
        }
        return instances;
    }

    public static void endMusic(UUID uuid, int fadeOutTime){
        FDMusic fdMusic = ACTIVE_MUSIC.get(uuid);
        if (fdMusic != null){
            fdMusic.triggerMusicEnd(fadeOutTime);
        }
    }

    public static void addMusic(FDMusicData fdMusicData){
        if (!ACTIVE_MUSIC.containsKey(fdMusicData.getMusicSourceUUID())){
            ACTIVE_MUSIC.put(fdMusicData.getMusicSourceUUID(), new FDMusic(fdMusicData));
        }
    }


    private static void tickMusic(){

        var iterator = ACTIVE_MUSIC.entrySet().iterator();

        while (iterator.hasNext()){
            var pair = iterator.next();
            FDMusic music = pair.getValue();
            if (music.hasFinishedPlaying()){
                music.onFinishPlaying();
                iterator.remove();
            }else{
                music.tick();
            }
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



    //WTF
    @Mod.EventBusSubscriber(modid = FDLib.MOD_ID, value = Dist.CLIENT)
    public static class StreamingSourcesBufferLengthCache {

        public static final HashMap<Integer, Float> sourceToProcessedBufferSecondLength = new HashMap<>();

        @SubscribeEvent
        public static void deleteFinishedSources(RenderFrameEvent.Post event){
            var iterator = sourceToProcessedBufferSecondLength.entrySet().iterator();
            while (iterator.hasNext()){
                var pair = iterator.next();
                int source = pair.getKey();

                if (!AL11.alIsSource(source)){
                    iterator.remove();
                }else{
                    if (AL11.alGetSourcei(source, AL10.AL_SOURCE_STATE) == AL10.AL_STOPPED){
                        iterator.remove();
                    }
                }

            }
        }

        public static void onSoundEngineStop(){
            sourceToProcessedBufferSecondLength.clear();
        }

        public static void onChannelStop(int source){
            sourceToProcessedBufferSecondLength.remove(source);
        }

        public static void onChannelHandleRelease(int source){
            sourceToProcessedBufferSecondLength.remove(source);
        }

        public static void onProcessedBuffersRemoval(int source, int[] processedBuffers){
            float fullSeconds = 0;
            for (int buffer : processedBuffers){
                int bytes = AL11.alGetBufferi(buffer, AL10.AL_SIZE);
                int channels = AL11.alGetBufferi(buffer, AL10.AL_CHANNELS);
                int bits = AL11.alGetBufferi(buffer, AL10.AL_BITS);
                int frequency = AL11.alGetBufferi(buffer, AL10.AL_FREQUENCY);
                try {
                    int samples = bytes * 8 / (channels * bits);
                    float seconds = samples / (float) frequency;
                    fullSeconds += seconds;
                }catch (ArithmeticException e){
                    FDLib.LOGGER.warn("Tried to cache length in seconds of a streaming sound source but failed, tell author of FDLib to fix his shit. Buffer attributes - Bytes: %d, Channels: %d, Bits: %d, Frequency: %d"
                            .formatted(bytes, channels, bits, frequency));
                }
            }
            float currentLength = sourceToProcessedBufferSecondLength.computeIfAbsent(source,i->0f);
            sourceToProcessedBufferSecondLength.put(source, fullSeconds + currentLength);
        }

    }

}
