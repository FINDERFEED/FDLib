package com.finderfeed.fdlib.systems.screen.default_components.buttons.selector_button;

import com.finderfeed.fdlib.systems.screen.FDScreen;
import com.finderfeed.fdlib.systems.screen.ValueComponent;
import com.finderfeed.fdlib.systems.screen.default_components.buttons.OnFDButtonPress;
import com.finderfeed.fdlib.systems.screen.default_components.misc.FDVerticalComponentContainer;
import com.finderfeed.fdlib.util.rendering.FDRenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.util.Mth;

import java.lang.reflect.Field;
import java.util.List;

public abstract class MainSelectorButton<T> extends ValueComponent<T> {

    protected Field field;
    protected T value;
    protected FDVerticalComponentContainer buttonContainer;
    protected float r = 0.25f;
    protected float g = 0.25f;
    protected float b = 0.25f;
    protected float a = 1f;

    public MainSelectorButton(Field field,FDScreen screen, String uniqueId) {
        super(screen, uniqueId, 0,0,0,12);
        this.field = field;

    }

    @Override
    public void renderComponent(GuiGraphics graphics, float x, float y, float mx, float my, float partialTicks) {
        if (this.isHovered()) {
            FDRenderUtil.fill(graphics.pose(), x, y, this.getWidth(), this.getHeight(), r + 0.05f, g + 0.05f, b + 0.05f, a);
        }else{
            FDRenderUtil.fill(graphics.pose(), x, y, this.getWidth(), this.getHeight(), r, g, b, a);
        }
        String str = this.valueToString(value);
        Font font = Minecraft.getInstance().font;
        float w = font.width(str);
        float mod = Mth.clamp((this.getWidth() - 2) / w,0,1);
        FDRenderUtil.renderCenteredText(graphics,x + this.getWidth()/2,y + 2,mod,false,str,0xffffff);
    }

    @Override
    public void setValue(T value) {
        this.value = value;
    }


    @Override
    public boolean mouseClicked(double mx, double my, int button) {
        if (buttonContainer == null) {
            this.initChoiceButtons();
        }else{
            this.destroyChoiceButtons();
        }
        return true;
    }


    protected void initChoiceButtons(){
        if (buttonContainer == null) {
            List<T> objects = this.values();
            float btnHeight = this.getHeight();
            float sizeOfChoiceWindow = Math.min(objects.size(), 3) * btnHeight;
            FDVerticalComponentContainer container = new FDVerticalComponentContainer(this.getScreen(),this.getUniqueId() + "_container",0,this.getHeight(),sizeOfChoiceWindow);

            int i = 0;
            for (T s : objects){
                OnFDButtonPress press = (screen,button,mx,my)->{
                    container.setRemoved(true);
                    this.setValue(((SelectorButton<T>) button).value);
                    return true;
                };
                String name = this.valueToString(s);
                SelectorButton<T> button = new SelectorButton<>(s,name,this.getScreen(),
                        this.getUniqueId()+"_button_" + i,
                        0,0,this.getWidth(),btnHeight,press
                );
                button.r = (float) Mth.clamp(this.r + 0.1 * ((i + 1) % 2),0,1);
                button.g = (float) Mth.clamp(this.g + 0.1 * ((i + 1) % 2),0,1);
                button.b = (float) Mth.clamp(this.b + 0.1 * ((i + 1) % 2),0,1);
                container.getChildren().setAsChild(button);
                i++;
            }
            this.getChildren().setAsChild(container);
            this.buttonContainer = container;
        }
    }

    protected void destroyChoiceButtons(){
        if (buttonContainer != null){
            buttonContainer.setRemoved(true);
            buttonContainer = null;
        }
    }

    @Override
    public boolean isMouseOver(float mx, float my) {
        if (buttonContainer != null){
            return FDRenderUtil.isMouseInBounds(mx,my,0,0,
                    this.getWidth(),
                    this.getHeight() + this.buttonContainer.getHeight()
            );
        }
        return super.isMouseOver(mx, my);
    }

    @Override
    public void onFocused(boolean state) {
        super.onFocused(state);
        if (!state){
            this.destroyChoiceButtons();
        }
    }

    @Override
    public boolean isWidthFixed() {
        return false;
    }

    public abstract String valueToString(T value);

    public abstract List<T> values();
}
