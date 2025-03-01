package com.finderfeed.fdlib.systems.simple_screen.fdwidgets.interfaces;

import com.finderfeed.fdlib.systems.simple_screen.FDWidget;
import net.minecraft.client.gui.GuiGraphics;

@FunctionalInterface
public interface OnFDWidgetHover {

    void hoverOver(FDWidget widget, GuiGraphics graphics, float mx, float my, float pticks);

}
