package com.finderfeed.fdlib.systems.screen.default_components.buttons;

import com.finderfeed.fdlib.systems.screen.FDScreen;

public interface OnFDButtonPress {

    boolean onButtonPress(FDScreen screen,ButtonComponent button,float mx,float my);

}
