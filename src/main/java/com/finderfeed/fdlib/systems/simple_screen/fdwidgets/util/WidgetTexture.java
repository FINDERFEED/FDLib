package com.finderfeed.fdlib.systems.simple_screen.fdwidgets.util;

import net.minecraft.resources.ResourceLocation;

public class WidgetTexture {

    public ResourceLocation resourceLocation;
    public float xOffset;
    public float yOffset;

    public WidgetTexture(ResourceLocation resourceLocation, float xOffset, float yOffset) {
        this.resourceLocation = resourceLocation;
        this.xOffset = xOffset;
        this.yOffset = yOffset;
    }
    public WidgetTexture(ResourceLocation resourceLocation) {
        this.resourceLocation = resourceLocation;
        this.xOffset = 0;
        this.yOffset = 0;
    }

}
