package com.finderfeed.fdlib.systems.screen.default_components.buttons;

import com.finderfeed.fdlib.systems.screen.FDScreen;
import com.finderfeed.fdlib.systems.screen.default_components.texture_components.Texture;
import net.minecraft.client.gui.GuiGraphics;

public abstract class TexturedButtonComponent extends ButtonComponent {

    public Texture hovered;
    public Texture unhovered;

    public TexturedButtonComponent(FDScreen screen, String uniqueId, float x, float y, float width, float height, OnFDButtonPress onPress) {
        super(screen, uniqueId, x, y, width, height, onPress);
    }


    @Override
    public void renderComponent(GuiGraphics graphics, float x, float y, float mx, float my, float partialTicks) {
        if (this.isHovered()){
            hovered.render(graphics,x,y,this.getWidth(),this.getHeight());
        }else{
            unhovered.render(graphics,x,y,this.getWidth(),this.getHeight());
        }
    }

    public void setHoveredTexture(Texture hovered) {
        this.hovered = hovered;
    }

    public void setUnhoveredTexture(Texture unhovered) {
        this.unhovered = unhovered;
    }


}
