package com.finderfeed.fdlib.systems.simple_screen.fdwidgets.util;

import net.minecraft.resources.ResourceLocation;

public class FDButtonTextures {

    private ButtonTexture hovered;
    private ButtonTexture unhovered;

    public FDButtonTextures(ButtonTexture unhovered,ButtonTexture hovered){
        this.hovered = hovered;
        this.unhovered = unhovered;
    }

    public FDButtonTextures(ButtonTexture tex){
        this.hovered = tex;
        this.unhovered = tex;
    }

    public ButtonTexture getButtonTexture(boolean hovered){
        return hovered ? this.hovered : unhovered;
    }

}
