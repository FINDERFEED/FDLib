package com.finderfeed.fdlib.systems.screen.default_components.buttons.selector_button;

import com.finderfeed.fdlib.systems.screen.FDScreen;
import com.finderfeed.fdlib.systems.screen.default_components.buttons.ButtonComponent;
import com.finderfeed.fdlib.systems.screen.default_components.buttons.OnFDButtonPress;
import com.finderfeed.fdlib.util.rendering.FDRenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.util.Mth;

public class SelectorButton<T> extends ButtonComponent {

    protected T value;
    protected String name;
    protected float r = 0.25f;
    protected float g = 0.25f;
    protected float b = 0.25f;
    protected float a = 1f;

    public SelectorButton(T value,String name,FDScreen screen, String uniqueId, float x, float y, float width, float height, OnFDButtonPress onPress) {
        super(screen, uniqueId, x, y, width, height, onPress);
        this.name = name;
        this.value = value;
    }

    @Override
    public void renderComponent(GuiGraphics graphics, float x, float y, float mx, float my, float partialTicks) {
        if (this.isHovered()) {
            FDRenderUtil.fill(graphics.pose(), x, y, this.getWidth(), this.getHeight(), r + 0.05f, g + 0.05f, b + 0.05f, a);
        }else{
            FDRenderUtil.fill(graphics.pose(), x, y, this.getWidth(), this.getHeight(), r, g, b, a);
        }
        Font font = Minecraft.getInstance().font;
        float w = font.width(name);
        float mod = Mth.clamp((this.getWidth() - 2) / w,0,1);
        FDRenderUtil.renderCenteredText(graphics,x + this.getWidth() / 2,y + 2,mod,false,name,0xffffff);
    }
}
