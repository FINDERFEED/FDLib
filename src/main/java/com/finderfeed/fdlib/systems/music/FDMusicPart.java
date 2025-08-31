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

    private FDMusic owner;

    private boolean finishedPlaying = false;

    public FDMusicPart(FDMusicPartData data, FDMusic owner){
        this.data = data;
        this.owner = owner;
    }

    public void tick(){
        this.deleteEndedSoundInstances();
    }

    public void renderTick(float pticks){
        this.manageLoopOrEndSound(pticks);
    }


    private void manageLoopOrEndSound(float pticks){
        if (!finishedPlaying){
            if (main == null){
                this.startNewSound(pticks);
            }

            var map = FDClientHelpers.getSoundEngine().instanceToChannel;

            if (!map.containsKey(main) && FDClientHelpers.getCurrentMusicVolume() > 0 && !Minecraft.getInstance().isPaused()) {
                this.startNewSound(pticks);
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

                        float playDuration = this.data.getPlayDuration();
                        if (currentPlaytime >= playDuration) {
                            if (this.data.shouldLoop()) {
                                this.startNewSound(pticks);
                            } else {
                                this.finishedPlaying = true;
                            }
                        }
                    }
                }));
            }


        }
    }

    protected void setAllSoundInstancesVolume(float volume){
        for (var instance : this.getSoundInstances()){
            var map = FDClientHelpers.getSoundEngine().instanceToChannel;
            ChannelAccess.ChannelHandle channelHandle = map.get(instance);
            if (channelHandle != null){
                channelHandle.execute(channel -> {
                    channel.setVolume(volume);
                });
            }
        }
        var map = FDClientHelpers.getSoundEngine().instanceToChannel;
        ChannelAccess.ChannelHandle channelHandle = map.get(main);
        if (channelHandle != null){
            channelHandle.execute(channel -> {
                channel.setVolume(volume);
            });
        }
    }


    private void startNewSound(float pticks){
        SoundInstance soundInstance = new FDMusicSoundInstance(this.data.getSoundEvent(), this.owner.getVolume(pticks) * FDClientHelpers.getCurrentMusicVolume());
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

    public boolean hasFinished(){
        return finishedPlaying;
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
