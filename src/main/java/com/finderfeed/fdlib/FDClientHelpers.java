package com.finderfeed.fdlib;

import net.minecraft.client.Minecraft;
import net.minecraft.world.level.Level;

public class FDClientHelpers {

    public static Level getClientLevel(){
        return Minecraft.getInstance().level;
    }

}
