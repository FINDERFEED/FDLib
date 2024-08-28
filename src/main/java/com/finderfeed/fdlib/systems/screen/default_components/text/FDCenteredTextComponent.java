package com.finderfeed.fdlib.systems.screen.default_components.text;

import com.finderfeed.fdlib.systems.screen.FDScreen;
import com.finderfeed.fdlib.util.rendering.FDRenderUtil;
import net.minecraft.client.gui.GuiGraphics;

public class FDCenteredTextComponent extends FDTextComponent {

    public FDCenteredTextComponent(FDScreen screen, String uniqueId, float x, float y, float width, float height) {
        super(screen, uniqueId, x, y, width, height);
    }

    @Override
    public void renderComponent(GuiGraphics graphics, float x, float y, float mx, float my, float partialTicks) {
        FDRenderUtil.renderFullyCenteredText(graphics,x,y,Math.round(this.getWidth()),textScale,false,0xffffff,this.component);
    }
}
