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
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;

public class FDButton extends FDWidget {

    protected OnFDWidgetClick click;
    protected OnFDWidgetHover hover;
    protected Component text;
    protected int textWidth;
    protected boolean textDrawShadow;
    protected float textScale;
    protected FDButtonTextures buttonTextures;
    protected float xTextOffset = 0;
    protected float yTextOffset = 0;
    protected SoundEvent sound;

    public FDButton(Screen screen, float x, float y, float width, float height) {
        super(screen, x, y, width, height);
    }

    public FDButton setSound(SoundEvent sound){
        this.sound = sound;
        return this;
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

    public FDButton setText(Component text, int textWidth, float textScale, boolean textDrawShadow, float xTextOffset, float yTextOffset){
        this.text = text;
        this.textWidth = textWidth;
        this.textDrawShadow = textDrawShadow;
        this.textScale = textScale;
        this.xTextOffset = xTextOffset;
        this.yTextOffset = yTextOffset;
        return this;
    }

    @Override
    public void renderWidget(GuiGraphics graphics, float mx, float my, float pticks) {

        if (buttonTextures != null){
            var texture = this.buttonTextures.getButtonTexture(this.isHovered());
            FDRenderUtil.bindTexture(texture.resourceLocation);
            FDRenderUtil.blitWithBlend(graphics.pose(),this.getX() - texture.xOffset,this.getY() - texture.yOffset,this.getWidth() + texture.xOffset * 2,this.getHeight() + texture.yOffset * 2,0,0,1,1,1,1,0,1f);
        }

        if (text != null){
            Font font = Minecraft.getInstance().font;
            var texts = font.split(text,Math.round(textWidth / textScale));
            float height = texts.size() * font.lineHeight * textScale;

            float yStart = this.getY() + this.getHeight() / 2 - height / 2;

            int i = 0;

            for (var text : texts){

                float xStart = -font.width(text) * textScale / 2 + this.getX() + this.getWidth() / 2;

                FDRenderUtil.renderScaledText(graphics,text,xTextOffset + xStart,yTextOffset + yStart + i * font.lineHeight * textScale,textScale,textDrawShadow,0xffffff);

                i++;
            }
        }

        if (this.isHovered() && this.hover != null){
            hover.hoverOver(this,graphics,mx,my,pticks);
        }
    }

    @Override
    public boolean onMouseClick(float mx, float my, int key) {
        boolean result = click != null && click.click(this, mx, my, key);
        if (result && sound != null){
            Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(sound,1f,1f));
        }
        return result;
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
