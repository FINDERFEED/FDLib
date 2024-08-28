package com.finderfeed.fdlib.systems.screen;

import com.finderfeed.fdlib.systems.screen.default_components.EmptyComponent;
import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class FDScreen extends Screen {

    private HashMap<String,FDScreenComponent> componentLookup = new HashMap<>();
    private List<FDScreenComponent> screenLayers = new ArrayList<>();
    private FDScreenComponent focused = null;
    private FDScreenComponent hovered = null;
    protected float mx;
    protected float my;

    public FDScreen() {
        super(Component.empty());
    }


    @Override
    protected void init() {
        super.init();
        componentLookup.clear();
        screenLayers.clear();
        Window window = Minecraft.getInstance().getWindow();
        EmptyComponent component = new EmptyComponent(this,"main",0,0,
                window.getGuiScaledWidth(),
                window.getGuiScaledHeight()
        );
        this.screenLayers.add(component);
        this.componentLookup.put("main",component);
    }

    @Override
    public void tick() {
        super.tick();
        boolean wasHovered = false;
        for (FDScreenComponent component : screenLayers){
            component.tick();
            if (!wasHovered){
                wasHovered = decideHoveredComponent(component,mx,my);
            }
        }
    }

    private boolean decideHoveredComponent(FDScreenComponent component,float mx,float my){
        if (component.isMouseOver(mx, my)) {
            var children = component.getChildren().getChildren();
            for (int i = children.size() - 1;i >= 0;i--) {
                FDScreenComponent child = children.get(i);
                Anchor anchor = child.getAnchor();
                float chx = anchor.xTransform.apply(component,child.getX());
                float chy = anchor.yTransform.apply(component,child.getY());
                float cmx = mx - chx;
                float cmy = my - chy;
                if (decideHoveredComponent(child,cmx,cmy)) {
                    return true;
                }
            }
            if (hovered != component){
                if (hovered != null) {
                    hovered.setHovered(false);
                }
                component.setHovered(true);
                hovered = component;
            }
            return true;
        }else{
            return false;
        }
    }

    @Override
    public void render(GuiGraphics graphics, int mx, int my, float partialTicks) {
        this.mx = mx;
        this.my = my;
        for (FDScreenComponent component : screenLayers) {
            this.renderComponent(component, graphics, 0, 0, mx, my, partialTicks);
        }
    }


    public void renderComponent(FDScreenComponent component,GuiGraphics graphics,float x,float y,float mx,float my,float partialTicks){
        component.renderComponent(graphics, x, y, mx, my, partialTicks);
        PoseStack matrices = graphics.pose();
        matrices.pushPose();
        matrices.translate(0,0,10);
        for (FDScreenComponent child : component.getChildren()){
            float cx = x + child.getX();
            float cy = y + child.getY();
            float cmx = mx - child.getX();
            float cmy = my - child.getY();
            Anchor anchor = child.getAnchor();
            cx = anchor.xTransform.apply(component,cx);
            cy = anchor.yTransform.apply(component,cy);
            this.renderComponent(child,graphics,cx,cy,cmx,cmy,partialTicks);
        }
        matrices.popPose();
        component.postRenderComponent(graphics, x, y, mx, my, partialTicks);
    }


    @Override
    public boolean mouseClicked(double mx, double my, int button) {
        for (FDScreenComponent component : screenLayers) {
            if (clickRecursively(component, (float) mx, (float) my, button)) {
                return true;
            }
        }
        if (focused != null) {
            this.focused.setFocused(false);
            this.focused = null;
        }
        return false;
    }

    protected boolean clickRecursively(FDScreenComponent component,float mx,float my,int button){
        if (component.isMouseOver(mx, my)) {
            for (FDScreenComponent child : component.getChildren()) {
                Anchor anchor = child.getAnchor();
                float chx = anchor.xTransform.apply(component,child.getX());
                float chy = anchor.yTransform.apply(component,child.getY());
                float cmx = mx - chx;
                float cmy = my - chy;
                if (clickRecursively(child, cmx, cmy, button)) {
                    return true;
                }
            }
            if (component.mouseClicked(mx, my, button)) {
                if (this.focused != null && this.focused != component) this.focused.setFocused(false);
                component.setFocused(true);
                this.focused = component;
                return true;
            } else {
                return false;
            }
        }else{
            return false;
        }
    }

    @Override
    public void mouseMoved(double mx, double my) {
        if (this.focused != null){
            float accumulatedX = 0;
            float accumulatedY = 0;
            FDScreenComponent component = this.focused;
            while (component != null){
                accumulatedX += component.getX();
                accumulatedY += component.getY();
                component = component.getParent();
            }
            this.focused.mouseMoved(mx - accumulatedX,my - accumulatedY);
        }
    }

    @Override
    public boolean mouseDragged(double mx, double my, int button, double leftRight, double upDown) {
        if (focused != null && focused.getParent() != null){
            Anchor anchor = focused.getAnchor();
            float cmx = anchor.xTransform.apply(focused.getParent(), focused.getX());
            float cmy = anchor.yTransform.apply(focused.getParent(), focused.getY());
            FDScreenComponent parent = focused.getParent();
            while (parent != null && parent.getParent() != null){
                anchor = parent.getAnchor();
                float acmx = anchor.xTransform.apply(parent.getParent(), parent.getX());
                float acmy = anchor.yTransform.apply(parent.getParent(), parent.getY());
                cmx += acmx;
                cmy += acmy;
                parent = parent.getParent();
            }
            float fmx = (float) (mx - cmx);
            float fmy = (float) (my - cmy);
            return focused.mouseDragged(fmx,fmy,button,leftRight,upDown);
        }else{
            return false;
        }
    }

    @Override
    public boolean mouseReleased(double mx, double my, int action) {
        if (focused != null && focused.getParent() != null){
            Anchor anchor = focused.getAnchor();
            float cmx = anchor.xTransform.apply(focused.getParent(), focused.getX());
            float cmy = anchor.yTransform.apply(focused.getParent(), focused.getY());
            FDScreenComponent parent = focused.getParent();
            while (parent != null && parent.getParent() != null){
                anchor = parent.getAnchor();
                float acmx = anchor.xTransform.apply(parent.getParent(), parent.getX());
                float acmy = anchor.yTransform.apply(parent.getParent(), parent.getY());
                cmx += acmx;
                cmy += acmy;
                parent = parent.getParent();
            }
            float fmx = (float) (mx - cmx);
            float fmy = (float) (my - cmy);
            return focused.mouseReleased(fmx,fmy,action);
        }else{
            return false;
        }
    }

    @Override
    public boolean mouseScrolled(double mx, double my, double xd, double yd) {
        for (FDScreenComponent component : screenLayers) {
            if (scrollRecursively(component, (float) mx, (float) my, (float) xd, (float) yd)) {
                return true;
            }
        }
        return false;
    }

    protected boolean scrollRecursively(FDScreenComponent component,float mx,float my,float xd,float yd){
        for (FDScreenComponent child : component.getChildren()){
            Anchor anchor = child.getAnchor();
            float chx = anchor.xTransform.apply(component,child.getX());
            float chy = anchor.yTransform.apply(component,child.getY());
            float cmx = mx - chx;
            float cmy = my - chy;
            if (scrollRecursively(child,cmx,cmy,xd,yd)){
                return true;
            }
        }
        if (component.isMouseOver(mx,my) && component.mouseScrolled(mx,my,xd,yd)){
            return true;
        }else{
            return false;
        }
    }


    @Override
    public boolean charTyped(char character, int button) {
        return focused != null && focused.charTyped(character, button);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (focused != null && focused.keyPressed(keyCode,scanCode,modifiers)){
            return true;
        }else{
            return super.keyPressed(keyCode,scanCode,modifiers);
        }
    }

    @Override
    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        return focused != null && focused.keyReleased(keyCode,scanCode,modifiers);
    }



    public FDScreenComponent getComponentById(String uniqueId){
        return componentLookup.get(uniqueId);
    }

    public void addComponent(FDScreenComponent component){
        this.componentLookup.put(component.getUniqueId(),component);
        if (component.getParent() == null) {
            FDScreenComponent main = componentLookup.get("main");
            main.getChildren().setAsChild(component);
            component.setParent(main);
        }
    }

    public void removeComponent(String id){
        FDScreenComponent component = this.getComponentById(id);
        if (component != null){
            this.componentLookup.remove(id);
            FDScreenComponent parent = component.getParent();
            if (parent != null){
                parent.getChildren().removeChild(id);
            }
        }
    }

    public List<FDScreenComponent> getScreenLayers() {
        return screenLayers;
    }

}
