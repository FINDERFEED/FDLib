package com.finderfeed.fdlib.systems.simple_screen.fdwidgets;

import com.finderfeed.fdlib.systems.simple_screen.FDWidget;
import com.finderfeed.fdlib.systems.simple_screen.fdwidgets.interfaces.OnFDWidgetClick;
import com.finderfeed.fdlib.systems.simple_screen.fdwidgets.interfaces.OnFDWidgetHover;
import com.finderfeed.fdlib.systems.simple_screen.fdwidgets.util.FDButtonTextures;
import com.finderfeed.fdlib.util.rendering.FDRenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class FDButton extends FDWidget {

    protected OnFDWidgetClick click;
    protected OnFDWidgetHover hover;
    protected Component text;
    protected int textWidth;
    protected boolean textDrawShadow;
    protected float textScale;
    protected FDButtonTextures buttonTextures;

    public FDButton(Screen screen, float x, float y, float width, float height) {
        super(screen, x, y, width, height);
    }

    public FDButton setTexture(FDButtonTextures texture){
        this.buttonTextures = texture;
        return this;
    }

    public FDButton setOnClickAction(OnFDWidgetClick click){
        this.click = click;
        return this;
    }

    public FDButton setOnHoverAction(OnFDWidgetHover hoverAction){
        this.hover = hoverAction;
        return this;
    }

    public FDButton setText(Component text, int textWidth, float textScale, boolean textDrawShadow){
        this.text = text;
        this.textWidth = textWidth;
        this.textDrawShadow = textDrawShadow;
        this.textScale = textScale;
        return this;
    }

    @Override
    public void renderWidget(GuiGraphics graphics, float mx, float my, float pticks) {

        if (buttonTextures != null){
            var texture = this.buttonTextures.getButtonTexture(this.isHovered());
            FDRenderUtil.bindTexture(texture);
            FDRenderUtil.blitWithBlend(graphics.pose(),this.getX(),this.getY(),this.getWidth(),this.getHeight(),0,0,1,1,1,1,0,1f);
        }

        if (text != null){
            Font font = Minecraft.getInstance().font;
            var texts = font.split(text,Math.round(textWidth / textScale));
            float height = texts.size() * font.lineHeight * textScale;

            float yStart = this.getY() + this.getHeight() / 2 - height / 2;

            int i = 0;

            for (var text : texts){

                float xStart = -font.width(text) * textScale / 2 + this.getX() + this.getWidth() / 2;

                FDRenderUtil.renderScaledText(graphics,text,xStart,yStart + i * font.lineHeight * textScale,textScale,textDrawShadow,0xffffff);

                i++;
            }
        }

        if (this.isHovered() && this.hover != null){
            hover.hoverOver(this,graphics,mx,my,pticks);
        }
    }

    @Override
    public boolean onMouseClick(float mx, float my, int key) {

        return click != null && click.click(this, mx, my, key);
    }

    @Override
    public boolean onMouseRelease(float mx, float my, int key) {
        return false;
    }

    @Override
    public boolean onMouseScroll(float mx, float my, float scrollX, float scrollY) {
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
