package com.finderfeed.fdlib.systems.simple_screen.fdwidgets;

import com.finderfeed.fdlib.systems.simple_screen.FDWidget;
import com.finderfeed.fdlib.util.rendering.FDRenderUtil;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;

public class TestButtonWidget extends FDWidget {

    public TestButtonWidget(Screen owner,float x, float y, float width, float height) {
        super(owner,x, y, width, height);
    }

    @Override
    public void renderWidget(GuiGraphics graphics, float mx, float my, float pticks) {
        FDRenderUtil.fill(graphics.pose(),this.getX() - 2,this.getY() - 2,this.getWidth() + 4,this.getHeight() + 4,1f,1f,1f,1f);
        if (this.isHovered()){
            FDRenderUtil.fill(graphics.pose(),this.getX(),this.getY(),this.getWidth(),this.getHeight(),1f,0,0,1f);
        }else if (this.isFocused()){
            FDRenderUtil.fill(graphics.pose(),this.getX(),this.getY(),this.getWidth(),this.getHeight(),0,1f,1f,1f);
        }else{
            FDRenderUtil.fill(graphics.pose(),this.getX(),this.getY(),this.getWidth(),this.getHeight(),0,0,1f,1f);
        }
    }

    @Override
    public boolean onMouseClick(float mx, float my, int key) {
        return true;
    }

    @Override
    public boolean onMouseRelease(float mx, float my, int key) {
        return false;
    }

    @Override
    public boolean onMouseScroll(float mx, float my, float scrollY) {

        for (var child : children.values()){
            child.setY(child.getY() + scrollY);
        }

        return true;
    }

    @Override
    public boolean onCharTyped(char character, int idk) {
        return false;
    }

    @Override
    public boolean onKeyPress(int keyCode, int scanCode, int modifiers) {
        return false;
    }

    @Override
    public boolean onKeyRelease(int keyCode, int scanCode, int modifiers) {
        return false;
    }
}
