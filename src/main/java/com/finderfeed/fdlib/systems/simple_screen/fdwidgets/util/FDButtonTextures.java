package com.finderfeed.fdlib.systems.simple_screen.fdwidgets.util;

public class FDButtonTextures {

    private WidgetTexture hovered;
    private WidgetTexture unhovered;

    public FDButtonTextures(WidgetTexture unhovered, WidgetTexture hovered){
        this.hovered = hovered;
        this.unhovered = unhovered;
    }

    public FDButtonTextures(WidgetTexture tex){
        this.hovered = tex;
        this.unhovered = tex;
    }

    public WidgetTexture getButtonTexture(boolean hovered){
        return hovered ? this.hovered : unhovered;
    }

}
