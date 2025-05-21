package com.finderfeed.fdlib;

import com.finderfeed.fdlib.systems.config.Comment;
import com.finderfeed.fdlib.systems.config.ConfigValue;
import com.finderfeed.fdlib.systems.config.ReflectiveJsonConfig;
import net.minecraft.resources.ResourceLocation;

public class FDClientConfig extends ReflectiveJsonConfig {

    @ConfigValue
    @Comment("Impact Frames - a fast flash of black-and-white-light on impacts.")
    public boolean impactFramesEnabled = true;

    public FDClientConfig() {
        super(FDLib.location("fdlib_client"));
    }

    @Override
    public boolean isClientside() {
        return true;
    }

}
