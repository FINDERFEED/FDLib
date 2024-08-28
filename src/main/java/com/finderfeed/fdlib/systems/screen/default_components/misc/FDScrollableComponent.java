package com.finderfeed.fdlib.systems.screen.default_components.misc;

import com.finderfeed.fdlib.systems.screen.FDScreen;
import com.finderfeed.fdlib.systems.screen.FDScreenComponent;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.util.Mth;

public abstract class FDScrollableComponent extends FDScreenComponent {

    public float scrollX;
    public float scrollY;

    public FDScrollableComponent(FDScreen screen, String uniqueId, float x, float y, float width, float height) {
        super(screen, uniqueId, x, y, width, height);
    }

    @Override
    public void renderComponent(GuiGraphics graphics, float x, float y, float mx, float my, float partialTicks) {}


    @Override
    public void setHeight(float height) {
        super.setHeight(height);
        this.scrollX = 0;
        this.scrollY = 0;
    }

    @Override
    public void setWidth(float width) {
        super.setWidth(width);
        this.scrollX = 0;
        this.scrollY = 0;
    }

    @Override
    public boolean mouseScrolled(double mx, double my, double dx, double dy) {
        if (Screen.hasShiftDown()){
            float prev = this.scrollX;
            float maxScroll =  this.getMaxScrollX();
            this.scrollX = Mth.clamp(this.scrollX - (float) dy * this.getScrollSpeed(),0,maxScroll);
            if (this.scrollX != prev){
                this.onHorizontalScroll(this.scrollX - prev);
                return true;
            }else{
                if (scrollX == 0 || scrollX == maxScroll){
                    return this.lockScrollWhenAtMaximum();
                }
                return false;
            }
        }else{
            float prev = this.scrollY;
            float maxScroll =  this.getMaxScrollY();
            this.scrollY = Mth.clamp(this.scrollY - (float) dy * this.getScrollSpeed(),0,maxScroll);
            if (this.scrollY != prev){
                this.onVerticalScroll(this.scrollY - prev);
                return true;
            }else{
                if (scrollY == 0 || scrollY == maxScroll){
                    return this.lockScrollWhenAtMaximum();
                }
                return false;
            }
        }
    }

    public boolean lockScrollWhenAtMaximum(){
        return false;
    }

    public void onVerticalScroll(float delta){

    }

    public void onHorizontalScroll(float delta){

    }
    public abstract float getScrollSpeed();
    public abstract float getMaxScrollX();
    public abstract float getMaxScrollY();

}
