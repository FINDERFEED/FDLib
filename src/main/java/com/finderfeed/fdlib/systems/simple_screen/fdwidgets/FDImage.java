package com.finderfeed.fdlib.systems.simple_screen.fdwidgets;

import com.finderfeed.fdlib.systems.simple_screen.FDWidget;
import com.finderfeed.fdlib.systems.simple_screen.fdwidgets.util.WidgetTexture;
import com.finderfeed.fdlib.util.rendering.FDRenderUtil;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;

public class FDImage extends FDWidget {

    private WidgetTexture texture;

    public FDImage(Screen screen, float x, float y, float width, float height, WidgetTexture texture) {
        super(screen, x, y, width, height);
        this.texture = texture;
    }

    @Override
    public void renderWidget(GuiGraphics graphics, float mx, float my, float pticks) {

        FDRenderUtil.bindTexture(texture.resourceLocation);
        FDRenderUtil.blitWithBlend(graphics.pose(),this.getX() - texture.xOffset,this.getY() - texture.yOffset, this.getWidth() + texture.xOffset * 2, this.getHeight() + texture.yOffset * 2,0,0,1f,1f,1f,1f,0,1f);

    }

    @Override
    public boolean onMouseClick(float mx, float my, int key) {
        return false;
    }

    @Override
    public boolean onMouseRelease(float mx, float my, int key) {
        return false;
    }

    @Override
    public boolean onMouseScroll(float mx, float my, float scrollY) {
        return false;
    }

    @Override
    public boolean onCharTyped(char character, int idk) {
        return false;
    }

    @Override
    public boolean onKeyPress(int keyCode, int scanCode, int modifiers) {
        return false;
    }

    @Override
    public boolean onKeyRelease(int keyCode, int scanCode, int modifiers) {
        return false;
    }
}
