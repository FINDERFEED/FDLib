package com.finderfeed.fdlib.systems.simple_screen.fdwidgets.util;

import net.minecraft.resources.ResourceLocation;

public class ButtonTexture {

    public ResourceLocation resourceLocation;
    public float xOffset;
    public float yOffset;

    public ButtonTexture(ResourceLocation resourceLocation, float xOffset, float yOffset) {
        this.resourceLocation = resourceLocation;
        this.xOffset = xOffset;
        this.yOffset = yOffset;
    }

}
