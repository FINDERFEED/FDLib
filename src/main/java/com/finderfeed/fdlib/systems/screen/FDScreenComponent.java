package com.finderfeed.fdlib.systems.screen;

import com.finderfeed.fdlib.systems.screen.annotations.FDColor;
import com.finderfeed.fdlib.systems.screen.annotations.FDName;
import com.finderfeed.fdlib.systems.screen.annotations.VComponent;
import com.finderfeed.fdlib.systems.screen.default_components.buttons.selector_button.EnumSelectorButton;
import com.finderfeed.fdlib.systems.screen.default_components.value_components.FloatVComponent;
import com.finderfeed.fdlib.util.rendering.FDRenderUtil;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.events.GuiEventListener;

public abstract class FDScreenComponent implements GuiEventListener {

    private FDScreen screen;
    private String uniqueId;
    private boolean focused;
    private boolean hovered;


    @VComponent(FloatVComponent.class)
    @FDColor(r = 0.25f, g = 0.25f, b = 0.25f, a = 1f)
    private float x;

    @VComponent(FloatVComponent.class)
    @FDColor(r = 0.25f, g = 0.25f, b = 0.25f, a = 1f)
    private float y;

    @VComponent(FloatVComponent.class)
    @FDColor(r = 0.25f, g = 0.25f, b = 0.25f, a = 1f)
    @FDName("Width")
    private float width;

    @VComponent(FloatVComponent.class)
    @FDColor(r = 0.25f, g = 0.25f, b = 0.25f, a = 1f)
    @FDName("Height")
    private float height;

    private boolean removed;

    @VComponent(EnumSelectorButton.class)
    @FDName("Rendering anchor")
    private Anchor anchor = Anchor.TOP_LEFT;

    private FDSCChildren children;
    private FDScreenComponent parent;


    public FDScreenComponent(FDScreen screen,String uniqueId,float x,float y,float width,float height){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.uniqueId = uniqueId;
        this.screen = screen;
        this.children = new FDSCChildren(this,screen);
    }

    /**
     * @param x - global x for rendering (matrix transformations do some strange things to text rendering to here we are)
     * @param y - global y for rendering
     * @param mx - mouse x relative to this component
     * @param my - mouse y relative to this component
     */
    public abstract void renderComponent(GuiGraphics graphics,float x,float y,float mx,float my,float partialTicks);

    /**
     * Is being called when all children were rendered
     */
    public void postRenderComponent(GuiGraphics graphics, float x, float y, float mx, float my, float partialTicks){}

    public void tick(){
        this.children.tick();
    }

    @Override
    public void setFocused(boolean focused) {
        this.focused = focused;
        this.onFocused(focused);
    }

    public void onFocused(boolean state){

    }

    @Override
    public boolean isFocused() {
        return focused;
    }


    public void setParent(FDScreenComponent parent) {
        this.parent = parent;
    }

    public void setX(float x) {
        this.x = x;
    }

    /**
     * @param mx - local mouse pos
     * @param my - local mouse pos
     */
    public boolean isMouseOver(float mx,float my){
        boolean result = FDRenderUtil.isMouseInBounds(mx,my,0,0,this.getWidth(),this.getHeight());
        return result;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void setHeight(float height) {
        float prev = this.height;
        this.height = height;
        if (this.parent != null) this.parent.onChildHeightChanged(this,prev,height);
    }

    public void setHeightWithoutUpdate(float height){
        this.height = height;
    }

    public void setWidth(float width) {
        float prev = this.width;
        this.width = width;
        if (this.parent != null) this.parent.onChildWidthChanged(this,prev,width);
    }
    public void setWidthWithoutUpdate(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public float getWidth() {
        return width;
    }

    public float getY() {
        return y;
    }

    public float getX() {
        return x;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public FDScreen getScreen() {
        return screen;
    }

    public FDScreenComponent getParent() {
        return parent;
    }

    public FDSCChildren getChildren() {
        return children;
    }

    public void setRemoved(boolean removed) {
        this.removed = removed;
    }

    public boolean isRemoved() {
        return removed;
    }

    public void setHovered(boolean hovered) {
        this.hovered = hovered;
        this.onHovered(hovered);
    }

    public void onHovered(boolean state){

    }

    public boolean isHovered() {
        return hovered;
    }

    public Anchor getAnchor() {
        return anchor;
    }

    public void setAnchor(Anchor anchor) {
        this.anchor = anchor;
    }

    public void onChildAdded(FDScreenComponent component){

    }

    public void onChildRemoved(FDScreenComponent component){

    }

    public void onChildWidthChanged(FDScreenComponent child,float previous,float current){

    }

    public void onChildHeightChanged(FDScreenComponent child,float previous,float current){

    }
}
