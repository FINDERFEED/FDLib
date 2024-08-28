package com.finderfeed.fdlib.systems.screen.default_components.buttons;

import com.finderfeed.fdlib.systems.screen.FDEditorComponent;
import com.finderfeed.fdlib.systems.screen.FDScreen;
import com.finderfeed.fdlib.systems.screen.ValueComponent;
import com.finderfeed.fdlib.util.rendering.FDRenderUtil;
import net.minecraft.client.gui.GuiGraphics;
import org.lwjgl.glfw.GLFW;

import java.lang.annotation.Annotation;

public class BooleanVComponent extends ValueComponent<Boolean> {

    public boolean state;

    public BooleanVComponent(FDScreen screen, String uniqueId) {
        super(screen, uniqueId, 0, 0, 12,12);
    }

    @Override
    public void renderComponent(GuiGraphics graphics, float x, float y, float mx, float my, float partialTicks) {
        FDRenderUtil.fill(graphics.pose(),x,y,this.getWidth(),this.getHeight(),0.25f,0.25f,0.25f,1f);
        if (state) {
            FDRenderUtil.fill(graphics.pose(), x + 2, y + 2, this.getWidth() - 4, this.getHeight() - 4, 0.75f, 0.75f, 0.75f, 1f);
        }
    }


    @Override
    public boolean mouseClicked(double p_94737_, double p_94738_, int btn) {
        if (btn == GLFW.GLFW_MOUSE_BUTTON_1){
            state = !state;
            return true;
        }
        return false;
    }

    @Override
    public boolean isWidthFixed() {
        return true;
    }

    @Override
    public void setValue(Boolean value) {
        this.state = value;
    }

    @Override
    public Boolean getValue() {
        return state;
    }

    @Override
    public void applyOptions(FDEditorComponent owner, Annotation annotation) {

    }

}
