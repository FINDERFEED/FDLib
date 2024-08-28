package com.finderfeed.fdlib.systems.screen.default_components.misc;

import com.finderfeed.fdlib.systems.screen.FDScreen;
import com.finderfeed.fdlib.systems.screen.FDScreenComponent;
import com.finderfeed.fdlib.util.rendering.FDRenderUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.util.Mth;

public class FDVerticalComponentContainer extends FDScrollableComponent {

    public static final float SCROLLER_WIDTH = 4;
    public float maxScroll = 0;
    public float distBetweenChildren = 0;
    public int scrollerTicker = 0;
    public static final int MAX_TIME = 20;

    public FDVerticalComponentContainer(FDScreen screen, String uniqueId, float x, float y,float height) {
        super(screen, uniqueId, x, y, 0,height);
    }


    @Override
    public void renderComponent(GuiGraphics graphics, float x, float y, float mx, float my, float partialTicks) {
        FDRenderUtil.Scissor.pushScissors(x, y, this.getWidth(), this.getHeight());
    }

    @Override
    public void postRenderComponent(GuiGraphics graphics, float x, float y, float mx, float my, float partialTicks) {
        float max = this.getMaxScrollY();
        if (max != 0) {
            PoseStack matrices = graphics.pose();
            matrices.pushPose();
            matrices.translate(0,0,100);
            float a = 0.25f + 0.5f * (1 - (float) Math.pow(1 - scrollerTicker / (float) MAX_TIME,4));
            FDRenderUtil.renderScrollBar(graphics.pose(),
                    x + this.getWidth() - SCROLLER_WIDTH,
                    y,
                    SCROLLER_WIDTH,
                    this.getHeight(),
                    this.scrollY,
                    max,
                    0.15f,
                    0.15f,
                    0.15f,
                    a,
                    0.5f,
                    0.5f,
                    0.5f,
                    a
            );
            matrices.popPose();
        }
        FDRenderUtil.Scissor.popScissors();

    }

    public void replaceChildren(){
        float accumulatedH = -scrollY;
        for (FDScreenComponent component : this.getChildren()){
            component.setY(accumulatedH);
            accumulatedH += (component.getHeight() + distBetweenChildren);
        }
    }

    public void recalculateMaxScroll(){
        float h = 0;
        for (FDScreenComponent component : this.getChildren()){
            h += component.getHeight() + distBetweenChildren;
        }
        h = Mth.clamp(h - this.getHeight(),0,Integer.MAX_VALUE);
        this.maxScroll = h;
    }

    public float calculateWidth(){
        float m = 0;
        for (FDScreenComponent component : this.getChildren()){
            m = Math.max(component.getWidth(),m);
        }
        return m;
    }

    @Override
    public void onChildAdded(FDScreenComponent component) {
        this.recalculateMaxScroll();
        if (component.getWidth() > this.getWidth()){
            this.setWidth(component.getWidth());
        }
        this.replaceChildren();
    }

    @Override
    public void onChildRemoved(FDScreenComponent component) {
        this.recalculateMaxScroll();
        this.setWidth(this.calculateWidth());
        this.replaceChildren();
    }

    @Override
    public void onChildWidthChanged(FDScreenComponent child, float previous, float current) {
        super.onChildWidthChanged(child, previous, current);
        this.setWidth(this.calculateWidth());
    }

    @Override
    public void onChildHeightChanged(FDScreenComponent child, float previous, float current) {
        super.onChildHeightChanged(child, previous, current);
        this.recalculateMaxScroll();
        this.scrollY = 0;
        this.scrollX = 0;
    }

    @Override
    public void tick() {
        super.tick();
        scrollerTicker = Mth.clamp(scrollerTicker - 1,0,MAX_TIME);
    }

    @Override
    public void setHeight(float height) {
        super.setHeight(height);
        this.recalculateMaxScroll();
    }

    @Override
    public void setWidth(float width) {
        super.setWidth(width);
    }

    @Override
    public void onVerticalScroll(float delta) {
        this.replaceChildren();
        this.scrollerTicker = MAX_TIME;
    }

    @Override
    public boolean lockScrollWhenAtMaximum() {
        return true;
    }

    @Override
    public float getScrollSpeed() {
        return 10;
    }

    @Override
    public float getMaxScrollX() {
        return 0;
    }

    @Override
    public float getMaxScrollY() {
        return maxScroll;
    }
}
