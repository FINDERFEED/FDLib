package com.finderfeed.fdlib.systems.music;

import com.finderfeed.fdlib.FDClientHelpers;
import com.finderfeed.fdlib.init.FDSounds;
import com.finderfeed.fdlib.systems.music.data.FDMusicPartData;
import com.finderfeed.fdlib.util.math.FDMathUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.ChannelAccess;
import net.minecraft.client.sounds.SoundEngine;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.sounds.SoundSource;
import org.lwjgl.openal.AL11;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class FDMusicPart {

    private FDMusicPartData data;

    private List<SoundInstance> oldSoundInstances = new ArrayList<>();
    private SoundInstance main;

    private int currentFadeTime = -1;
    private int fadeInTicker = -1;
    private int fadeOutTicker = -1;
    private float oldVolume = 1;
    private float currentVolume = 1;
    private float volumeStamp = 1;

    private boolean finishOnFadeOut;

    private boolean finishedPlaying = false;

    public FDMusicPart(FDMusicPartData data){
        this.data = data;
        if (data.getDefaultFadeInTime() == 0){
            currentVolume = 1;
            volumeStamp = 1;
        }else{
            this.triggerFadeIn(data.getDefaultFadeInTime());
            oldVolume = 0;
            currentVolume = 0;
            volumeStamp = 0;
        }
    }

    public void tick(){
        this.deleteEndedSoundInstances();
        if (!finishedPlaying) {
            this.tickFades();
        }
    }

    public void renderTick(float pticks){
        float volume = Minecraft.getInstance().options.getSoundSourceVolume(SoundSource.MUSIC);
        this.setSoundInstancesVolume(FDMathUtil.lerp(oldVolume,currentVolume,pticks) * volume);
        this.manageLoopOrEndSound();
    }


    private void manageLoopOrEndSound(){
        if (!finishedPlaying){
            if (main == null){
                this.startNewSound();
            }

            var map = FDClientHelpers.getSoundEngine().instanceToChannel;

            if (!map.containsKey(main) && FDClientHelpers.getCurrentMusicVolume() > 0 && !Minecraft.getInstance().isPaused()) {
                this.startNewSound();
            }

            ChannelAccess.ChannelHandle channelHandle = map.get(main);

            if (channelHandle != null) {
                channelHandle.execute((channel -> {
                    if (!finishedPlaying) {
                        int source = channel.source;
                        float currentPlaytime = AL11.alGetSourcef(source, AL11.AL_SEC_OFFSET);

                        var m = FDMusicSystem.StreamingSourcesBufferLengthCache.sourceToProcessedBufferSecondLength;
                        if (m.containsKey(source)){
                            currentPlaytime += m.get(source);
                        }

                        System.out.println(currentPlaytime);

                        float playDuration = this.data.getPlayDuration();
                        if (currentPlaytime >= playDuration) {
                            if (this.data.shouldLoop()) {
                                this.startNewSound();
                            } else {
                                this.finishedPlaying = true;
                            }
                        }
                    }
                }));
            }


        }
    }

    private void tickFades(){
        this.oldVolume = currentVolume;
        if (fadeInTicker != -1){
            float p = (float) fadeInTicker / currentFadeTime;
            this.currentVolume = FDMathUtil.lerp(this.volumeStamp, 1, 1 - p);
            this.fadeInTicker--;
        }else if (fadeOutTicker != -1){
            float p = (float) fadeOutTicker / currentFadeTime;
            this.currentVolume = FDMathUtil.lerp(this.volumeStamp, 0, 1 - p);
            this.fadeOutTicker--;
            if (this.fadeOutTicker == -1 && this.finishOnFadeOut){
                this.finishedPlaying = true;
                SoundManager soundManager = FDClientHelpers.getSoundManager();
                for (var inst : this.oldSoundInstances){
                    soundManager.stop(inst);
                }
                this.oldSoundInstances.clear();
            }
        }
    }



    private void startNewSound(){
        SoundInstance soundInstance = SimpleSoundInstance.forMusic(this.data.getSoundEvent());
        if (main != null) {
            this.oldSoundInstances.add(main);
        }
        this.main = soundInstance;
        SoundManager soundManager = Minecraft.getInstance().getSoundManager();
        soundManager.play(soundInstance);
    }

    private void deleteEndedSoundInstances(){
        Iterator<SoundInstance> iterator = this.oldSoundInstances.iterator();
        while (iterator.hasNext()){
            SoundInstance soundInstance = iterator.next();
            SoundManager soundManager = FDClientHelpers.getSoundManager();
            if (!soundManager.isActive(soundInstance)){
                iterator.remove();
            }
        }
    }

    private void setSoundInstancesVolume(float volume){
        SoundEngine soundEngine = FDClientHelpers.getSoundEngine();
        for (var instances : soundEngine.instanceToChannel.entrySet()){
            SoundInstance key = instances.getKey();
            if (this.oldSoundInstances.contains(key) || key == main){
                ChannelAccess.ChannelHandle channelHandle = instances.getValue();
                channelHandle.execute(channel -> {
                    channel.setVolume(volume);
                });
            }
        }
    }

    public boolean hasFinished(){
        return finishedPlaying;
    }

    public void triggerFadeOut(int fadeOutTime, boolean finishOnFadeOut){
        volumeStamp = currentVolume;
        fadeOutTicker = fadeOutTime;
        fadeInTicker = -1;
        currentFadeTime = fadeOutTime;
        this.finishOnFadeOut = finishOnFadeOut;
    }

    public void triggerFadeIn(int fadeInTime) {
        if (!finishOnFadeOut) {
            volumeStamp = currentVolume;
            fadeOutTicker = -1;
            fadeInTicker = fadeInTime;
            currentFadeTime = fadeInTime;
        }
    }

    public void end(){
    }

    public FDMusicPartData getData() {
        return data;
    }

    public List<SoundInstance> getSoundInstances() {
        List<SoundInstance> c = new ArrayList<>(oldSoundInstances);
        if (main != null) {
            c.add(main);
        }
        return c;
    }


}
