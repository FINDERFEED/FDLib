package com.finderfeed.fdlib.systems.screen.test;

import com.finderfeed.fdlib.systems.screen.Anchor;
import com.finderfeed.fdlib.systems.screen.FDEditorComponent;
import com.finderfeed.fdlib.systems.screen.FDScreen;
import com.finderfeed.fdlib.systems.screen.default_components.buttons.MethodButtonComponent;
import net.minecraft.client.gui.GuiGraphics;

public class ComponentTestScreen extends FDScreen {


    @Override
    protected void init() {
        super.init();

        SimpleColoredSquare simpleColoredSquare = new SimpleColoredSquare(this,"test",200,100,100,100);
        this.addComponent(simpleColoredSquare);

        FDEditorComponent editorComponent = new FDEditorComponent(simpleColoredSquare,this,"testeditor",0,0);
        editorComponent.setAnchor(Anchor.CENTER);
        this.addComponent(editorComponent);

    }

    @Override
    public void render(GuiGraphics graphics, int mx, int my, float partialTicks) {
        super.render(graphics, mx, my, partialTicks);
    }

    public static void testMethod(MethodButtonComponent buttonComponent){
        System.out.println("test succeeded: " + buttonComponent.getUniqueId());
    }
}
