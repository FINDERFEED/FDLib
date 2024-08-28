package com.finderfeed.fdlib.systems.screen.default_components.misc;

import com.finderfeed.fdlib.systems.screen.FDScreen;
import com.finderfeed.fdlib.systems.screen.FDScreenComponent;
import net.minecraft.client.gui.GuiGraphics;
import org.lwjgl.glfw.GLFW;

public class DraggableComponent extends FDScreenComponent {

    public FDScreenComponent component;

    public DraggableComponent(FDScreenComponent toDrag,FDScreen screen, String uniqueId, float x, float y, float width, float height) {
        super(screen, uniqueId, x, y, width, height);
        this.component = toDrag;
    }

    @Override
    public void renderComponent(GuiGraphics graphics, float x, float y, float mx, float my, float partialTicks) {

    }

    @Override
    public boolean mouseDragged(double mx, double my, int button, double dx, double dy) {
        if (button == GLFW.GLFW_MOUSE_BUTTON_1) {
            component.setX(component.getX() + (float) dx);
            component.setY(component.getY() + (float) dy);
            return true;
        }
        return false;
    }

    @Override
    public boolean mouseClicked(double mx, double my, int button) {
        return button == GLFW.GLFW_MOUSE_BUTTON_1;
    }

    @Override
    public boolean mouseReleased(double p_94753_, double p_94754_, int button) {
        return button == GLFW.GLFW_MOUSE_BUTTON_1;
    }
}
