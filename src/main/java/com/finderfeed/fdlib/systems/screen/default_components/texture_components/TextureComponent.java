package com.finderfeed.fdlib.systems.screen.default_components.texture_components;

import com.finderfeed.fdlib.systems.screen.FDScreen;
import com.finderfeed.fdlib.systems.screen.FDScreenComponent;
import net.minecraft.client.gui.GuiGraphics;

public class TextureComponent extends FDScreenComponent {

    public Texture texture;

    public TextureComponent(FDScreen screen, String uniqueId, float x, float y, float width, float height) {
        super(screen, uniqueId, x, y, width, height);
    }

    @Override
    public void renderComponent(GuiGraphics graphics, float x, float y, float mx, float my, float partialTicks) {
        texture.render(graphics,x,y,this.getWidth(),this.getHeight());
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    public Texture getTexture() {
        return texture;
    }
}
