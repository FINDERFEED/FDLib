package com.finderfeed.fdlib.systems.simple_screen;

import com.finderfeed.fdlib.data_structures.ObjectHolder;
import com.finderfeed.fdlib.util.rendering.FDRenderUtil;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;

import java.util.HashMap;
import java.util.LinkedHashMap;

public abstract class FDWidget implements GuiEventListener, Renderable {

    private float x;
    private float y;
    private float width;
    private float height;

    private boolean focused = false;

    private boolean active = true;

    private boolean hovered = false;

    private HashMap<String, FDWidget> children = new LinkedHashMap<>();

    public FDWidget(float x,float y,float width,float height){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public abstract void renderWidget(GuiGraphics graphics, float mx, float my, float pticks);

    public void renderChildren(GuiGraphics graphics, float mx, float my, float pticks){
        boolean isChildHovered = false;

        for (var child : children.values()){
            if (child.isActive() && FDRenderUtil.isMouseInBounds(mx,my,child.getX(),child.getY(),child.getWidth(),child.getHeight())){
                isChildHovered = true;
            }
            child.render(graphics,(int)mx,(int)my,pticks);
        }

        if (!isChildHovered){
            this.hovered = FDRenderUtil.isMouseInBounds(mx,my,this.getX(),this.getY(),this.getWidth(),this.getHeight());
        }else{
            hovered = false;
        }
    }

    @Override
    public void render(GuiGraphics graphics, int mx, int my, float pticks) {

        if (!isActive()) return;

        this.renderWidget(graphics,mx,my,pticks);

        this.renderChildren(graphics,mx,my,pticks);

    }

    public void tickChildren(){
        for (var child : children.values()){
            child.tick();
        }
    }

    public void tick(){
        this.tickChildren();
    }


    public void addChild(String id, FDWidget widget){
        this.children.put(id,widget);
    }

    public FDWidget getChild(String id){
        return this.children.get(id);
    }

    public abstract boolean onMouseClick(float mx,float my,int key);
    public abstract boolean onMouseRelease(float mx,float my,int key);
    public abstract boolean onMouseScroll(float mx,float my, float scrollX,float scrollY);
    public abstract boolean onCharTyped(char character, int idk);
    public abstract boolean onKeyPress(int keyCode, int scanCode, int modifiers);
    public abstract boolean onKeyRelease(int keyCode, int scanCode, int modifiers);

    @Override
    public final boolean keyPressed(int keyCode,int scanCode, int modifiers) {

        if (!isActive()) return false;

        if (this.isFocused()){
            return this.onKeyPress(keyCode,scanCode,modifiers);
        }else{
            boolean result = false;
            for (FDWidget widget : children.values()){
                result = widget.keyPressed(keyCode,scanCode,modifiers) || result;
            }
            return result;
        }
    }

    @Override
    public final boolean keyReleased(int keyCode,int scanCode, int modifiers) {

        if (!isActive()) return false;

        if (this.isFocused()){
            return this.onKeyRelease(keyCode,scanCode,modifiers);
        }else{
            boolean result = false;
            for (FDWidget widget : children.values()){
                result = widget.keyReleased(keyCode,scanCode,modifiers) || result;
            }
            return result;
        }
    }

    @Override
    public final boolean charTyped(char character, int idk) {

        if (!isActive()) return false;

        boolean result = this.isFocused();

        if (this.isFocused()){
            result = this.onCharTyped(character,idk);
        }else{
            for (var child : children.values()){
                child.charTyped(character,idk);
            }
        }

        return result;
    }

    private void setFocusStateFor(FDWidget widget){
        this.setFocused(false);
        for (FDWidget w : children.values()){
            w.setFocusStateFor(widget);
        }
        if (widget == this){
            this.setFocused(true);
        }
    }

    private boolean mouseClick(double mx, double my, int key, ObjectHolder<FDWidget> result){

        if (!isActive()) return false;

        boolean childWasClicked = false;

        if (FDRenderUtil.isMouseInBounds((float)mx,(float)my,this.x,this.y,this.width,this.height)){

            for (FDWidget widget : this.children.values()){
                if (widget.mouseClick(mx,my,key,result)){
                    childWasClicked = true;
                    break;
                }
            }
            if (!childWasClicked){
                boolean res = this.onMouseClick((float) mx,(float) my,key);
                if (res){
                    result.setValue(this);
                }
                return res;
            }
        }

        return childWasClicked;
    }

    @Override
    public final boolean mouseClicked(double mx, double my, int key) {

        if (!isActive()) return false;

        ObjectHolder<FDWidget> result = new ObjectHolder<>(null);
        this.mouseClick(mx,my,key,result);

        FDWidget clickedWidget = result.getValue();
        if (clickedWidget != null) {
            this.setFocusStateFor(clickedWidget);
        }
        return clickedWidget != null;
    }

    private boolean mouseRelease(double mx, double my, int key, ObjectHolder<FDWidget> result){

        if (!isActive()) return false;
        boolean childWasClicked = false;
        if (FDRenderUtil.isMouseInBounds((float)mx,(float)my,this.x,this.y,this.width,this.height)){
            for (FDWidget widget : this.children.values()){
                if (widget.mouseRelease(mx,my,key,result)){
                    childWasClicked = true;
                    break;
                }
            }
            if (!childWasClicked){
                boolean res = this.onMouseRelease((float) mx,(float) my,key);
                if (res){
                    result.setValue(this);
                }
                return res;
            }
        }
        return childWasClicked;
    }

    @Override
    public final boolean mouseReleased(double mx, double my, int key) {

        if (!isActive()) return false;

        ObjectHolder<FDWidget> result = new ObjectHolder<>(null);
        this.mouseRelease(mx,my,key,result);

        FDWidget clickedWidget = result.getValue();
        if (clickedWidget != null) {
            this.setFocusStateFor(clickedWidget);
        }
        return clickedWidget != null;
    }

    private boolean scroll(double mx, double my, double scrollX, double scrollY, ObjectHolder<FDWidget> interactedWidget){

        if (!isActive()) return false;

        boolean childWasScrolled = false;

        if (FDRenderUtil.isMouseInBounds((float)mx,(float)my,this.x,this.y,this.width,this.height)){
            for (FDWidget widget : this.children.values()){
                if (widget.scroll(mx,my,scrollX,scrollY,interactedWidget)){
                    childWasScrolled = true;
                    break;
                }
            }
            if (!childWasScrolled){
                boolean result = this.onMouseScroll((float) mx,(float) my, (float) scrollX,(float) scrollY);
                if (result){
                    interactedWidget.setValue(this);
                }
                return result;
            }
        }
        return childWasScrolled;
    }

    @Override
    public final boolean mouseScrolled(double mx, double my, double scrollX, double scrollY) {

        if (!isActive()) return false;

        ObjectHolder<FDWidget> result = new ObjectHolder<>(null);

        this.scroll(mx,my,scrollX,scrollY,result);

        FDWidget scrolledWidget = result.getValue();
        if (scrolledWidget != null) {
            this.setFocusStateFor(scrolledWidget);
        }
        return scrolledWidget != null;
    }

    @Override
    public final boolean isMouseOver(double mx, double my) {

        if (!isActive()) return false;

        boolean childUnderMouse = false;

        if (FDRenderUtil.isMouseInBounds((float)mx,(float)my,this.x,this.y,this.width,this.height)){
            for (FDWidget widget : this.children.values()){
                if (widget.isMouseOver(mx,my)){
                    childUnderMouse = true;
                    break;
                }
            }
            if (!childUnderMouse){
                return true;
            }
        }

        return childUnderMouse;
    }


    public void setX(float x) {

        float d = x - this.x;
        for (var child : children.values()){
            child.setX(child.getX() + d);
        }

        this.x = x;
    }

    public void setY(float y) {

        float d = y - this.y;
        for (var child : children.values()){
            child.setY(child.getY() + d);
        }

        this.y = y;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isHovered() {
        return hovered;
    }

    public void unfocusChildren(){
        for (var child : this.children.values()){
            child.setFocused(false);
        }
    }

    @Override
    public void setFocused(boolean focused) {
        this.focused = focused;
    }

    @Override
    public boolean isFocused() {
        return focused;
    }


}
