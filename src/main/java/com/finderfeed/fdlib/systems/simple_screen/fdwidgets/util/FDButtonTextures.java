package com.finderfeed.fdlib.systems.simple_screen.fdwidgets.util;

import net.minecraft.resources.ResourceLocation;

public class FDButtonTextures {

    private ResourceLocation hovered;
    private ResourceLocation unhovered;

    public FDButtonTextures(ResourceLocation unhovered,ResourceLocation hovered){
        this.hovered = hovered;
        this.unhovered = unhovered;
    }

    public FDButtonTextures(ResourceLocation tex){
        this.hovered = tex;
        this.unhovered = tex;
    }

    public ResourceLocation getButtonTexture(boolean hovered){
        return hovered ? this.hovered : unhovered;
    }

}
