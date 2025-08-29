package com.finderfeed.fdlib;

import net.minecraft.client.Minecraft;
import net.minecraft.client.sounds.SoundEngine;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class FDClientHelpers {

    public static Level getClientLevel(){
        return Minecraft.getInstance().level;
    }

    public static Player getClientPlayer(){
        return Minecraft.getInstance().player;
    }

    public static SoundEngine getSoundEngine(){
        return Minecraft.getInstance().getSoundManager().soundEngine;
    }

    public static SoundManager getSoundManager(){
        return Minecraft.getInstance().getSoundManager();
    }

}
