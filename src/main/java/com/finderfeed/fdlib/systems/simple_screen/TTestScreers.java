package com.finderfeed.fdlib.systems.simple_screen;

import com.finderfeed.fdlib.FDLib;
import com.finderfeed.fdlib.systems.simple_screen.fdwidgets.TestButtonWidget;
import net.minecraft.client.Minecraft;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.InputEvent;
import org.lwjgl.glfw.GLFW;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.GAME, modid = FDLib.MOD_ID)
public class TTestScreers extends SimpleFDScreen{


    @Override
    protected void init() {
        super.init();

        TestButtonWidget testButtonWidget2 = new TestButtonWidget(this,5,5,10,10);
        TestButtonWidget testButtonWidget3 = new TestButtonWidget(this,55,5,50,50);
        TestButtonWidget testButtonWidget4 = new TestButtonWidget(this,25,5,10,10);

        TestButtonWidget testButtonWidget = new TestButtonWidget(this,50,50,200,200);
        testButtonWidget.addChild("testButton",testButtonWidget2);
        testButtonWidget.addChild("testButton2",testButtonWidget3);
        testButtonWidget3.addChild("testButton",testButtonWidget4);

        this.addRenderableWidget(testButtonWidget);


        TestButtonWidget other = new TestButtonWidget(this,300,20,10,10);
        this.addRenderableWidget(other);
    }

    @Override
    public float getScreenWidth() {
        return 0;
    }

    @Override
    public float getScreenHeight() {
        return 0;
    }

//    @SubscribeEvent
//    public static void onKeyPress(InputEvent.Key event){
//        if (Minecraft.getInstance().level == null) return;
//
//        if (event.getKey() == GLFW.GLFW_KEY_M){
//            Minecraft.getInstance().setScreen(new TTestScreers());
//        }
//    }
}
