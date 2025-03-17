package com.finderfeed.fdlib.systems.simple_screen.fdwidgets.text_block.interactions;

import com.finderfeed.fdlib.util.rendering.FDRenderUtil;

public class InteractionBox {

    public float x;
    public float y;
    public float width;
    public float height;

    public TextBlockEntryInteraction interaction;

    public InteractionBox(float x, float y, float width, float height, TextBlockEntryInteraction interaction) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.interaction = interaction;
    }

    public boolean isMouseInside(float mx,float my){
        return FDRenderUtil.isMouseInBounds(mx,my,x,y,width,height);
    }

}
