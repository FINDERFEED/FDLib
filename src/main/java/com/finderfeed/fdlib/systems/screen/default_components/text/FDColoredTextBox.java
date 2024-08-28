package com.finderfeed.fdlib.systems.screen.default_components.text;

import com.finderfeed.fdlib.systems.screen.FDEditorComponent;
import com.finderfeed.fdlib.systems.screen.FDScreen;
import com.finderfeed.fdlib.systems.screen.annotations.FDColor;
import com.finderfeed.fdlib.util.rendering.FDRenderUtil;
import net.minecraft.client.gui.GuiGraphics;

import java.lang.annotation.Annotation;

public abstract class FDColoredTextBox<T> extends FDTextBox<T> {

    public float r = 0;
    public float g = 0;
    public float b = 0;
    public float a = 1;


    public FDColoredTextBox(FDScreen screen, String uniqueId, float x, float y, float width, float height) {
        super(screen, uniqueId, x, y, width, height);
    }

    @Override
    public void renderBackground(GuiGraphics graphics, float x, float y, float mx, float my, float partialTicks) {
        FDRenderUtil.fill(graphics.pose(),x,y,this.getWidth(),this.getHeight(),r,g,b,a);
    }

    @Override
    public void applyOptions(FDEditorComponent owner, Annotation annotation) {
        super.applyOptions(owner,annotation);
        if (annotation instanceof FDColor color){
            this.r = color.r();
            this.g = color.g();
            this.b = color.b();
            this.a = color.a();
        }
    }
}
