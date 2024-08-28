package com.finderfeed.fdlib.systems.screen.test;

import com.finderfeed.fdlib.systems.screen.FDScreen;
import com.finderfeed.fdlib.systems.screen.FDScreenComponent;
import com.finderfeed.fdlib.systems.screen.annotations.FDName;
import com.finderfeed.fdlib.systems.screen.annotations.VComponent;
import com.finderfeed.fdlib.systems.screen.default_components.buttons.BooleanVComponent;
import com.finderfeed.fdlib.systems.screen.default_components.value_components.FloatVComponent;
import com.finderfeed.fdlib.util.rendering.FDRenderUtil;
import net.minecraft.client.gui.GuiGraphics;

public class SimpleColoredSquare extends FDScreenComponent {

    @VComponent(FloatVComponent.class)
    @FDName("Red color")
    public float r = 0f;

    @VComponent(FloatVComponent.class)
    @FDName("Green color")
    public float g = 0f;

    @VComponent(FloatVComponent.class)
    @FDName("Blue color")
    public float b = 0f;

    @VComponent(BooleanVComponent.class)
    @FDName("Test boolean")
    public boolean test = true;


    public SimpleColoredSquare(FDScreen screen, String uniqueId, float x, float y, float width, float height) {
        super(screen, uniqueId, x, y, width, height);
    }

    @Override
    public void renderComponent(GuiGraphics graphics, float x, float y, float mx, float my, float partialTicks) {
        if (this.isHovered()) {
            FDRenderUtil.fill(graphics.pose(), x, y, this.getWidth(), this.getHeight(), r + 0.1f, g + 0.1f, b + 0.1f, 1f);
        }else{
            FDRenderUtil.fill(graphics.pose(), x, y, this.getWidth(), this.getHeight(), r, g, b, 1f);
        }
    }
}
