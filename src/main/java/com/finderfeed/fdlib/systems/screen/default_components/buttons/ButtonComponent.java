package com.finderfeed.fdlib.systems.screen.default_components.buttons;

import com.finderfeed.fdlib.systems.screen.FDScreen;
import com.finderfeed.fdlib.systems.screen.FDScreenComponent;
import org.lwjgl.glfw.GLFW;

public abstract class ButtonComponent extends FDScreenComponent {

    public OnFDButtonPress onPress;

    public ButtonComponent(FDScreen screen, String uniqueId, float x, float y, float width, float height, OnFDButtonPress onPress) {
        super(screen, uniqueId, x, y, width, height);
        this.onPress = onPress;
    }

    @Override
    public boolean mouseClicked(double mx, double my, int button) {
        if (button == GLFW.GLFW_MOUSE_BUTTON_1) {
            return onPress != null && onPress.onButtonPress(this.getScreen(), this, (float) mx, (float) my);
        }else{
            return false;
        }
    }

}
