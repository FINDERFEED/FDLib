package com.finderfeed.fdlib.systems.simple_screen.fdwidgets.interfaces;

import com.finderfeed.fdlib.systems.simple_screen.FDWidget;

@FunctionalInterface
public interface OnFDWidgetClick {

    boolean click(FDWidget button, float mx, float my, int key);

}
