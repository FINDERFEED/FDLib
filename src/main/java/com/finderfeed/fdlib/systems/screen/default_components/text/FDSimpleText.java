package com.finderfeed.fdlib.systems.screen.default_components.text;

import com.finderfeed.fdlib.systems.screen.FDScreen;
import com.finderfeed.fdlib.systems.screen.FDScreenComponent;
import com.finderfeed.fdlib.util.rendering.FDRenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

public class FDSimpleText extends FDScreenComponent {

    private Component text;
    private float scale;

    public FDSimpleText(FDScreen screen, String uniqueId, float x, float y, Component text,float scale) {
        super(screen, uniqueId, x, y, 0, 0);
        this.text = text;
        this.scale = scale;
        Font font = Minecraft.getInstance().font;
        float lineHeight = font.lineHeight * scale;
        this.setHeight(lineHeight + scale * 2);
        this.setWidth(font.width(text) * scale);
    }

    @Override
    public void renderComponent(GuiGraphics graphics, float x, float y, float mx, float my, float partialTicks) {
        FDRenderUtil.renderScaledText(graphics,text,x + scale,y + scale,scale,false,0xffffff);
    }
}
