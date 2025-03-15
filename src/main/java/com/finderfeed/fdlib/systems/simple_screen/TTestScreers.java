package com.finderfeed.fdlib.systems.simple_screen;

import com.finderfeed.fdlib.FDLib;
import com.finderfeed.fdlib.systems.simple_screen.fdwidgets.FDButton;
import com.finderfeed.fdlib.systems.simple_screen.fdwidgets.TestButtonWidget;
import com.finderfeed.fdlib.systems.simple_screen.fdwidgets.util.FDButtonTextures;
import com.finderfeed.fdlib.util.math.FDMathUtil;
import com.finderfeed.fdlib.util.rendering.FDEasings;
import com.finderfeed.fdlib.util.rendering.FDRenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.InputEvent;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

//@EventBusSubscriber(bus = EventBusSubscriber.Bus.GAME, modid = FDLib.MOD_ID)
public class TTestScreers extends SimpleFDScreen {

    private static int test = 0;
    private static Vector2f[] testw = new Vector2f[]{
            new Vector2f(20,20),
            new Vector2f(220,20),
            new Vector2f(220,220),
            new Vector2f(20,220)
    };

    @Override
    protected void init() {
        super.init();

//        FDButton test2 = new FDButton(this,10,10,10,10)
//                .setTexture(new FDButtonTextures(FDLib.location("textures/gui/test2.png")))
//                .setOnHoverAction(((widget,graphics, mx, my, pticks) -> {
//                    graphics.renderTooltip(font,Component.literal("Ты думал что то здесь будет? Неее..."),(int)mx,(int)my);
//                }));

//        FDButton button = new FDButton(this,20,20,100,50)
//                .setOnClickAction(((button1, mx, my, key) -> {
//                    Vector2f next = testw[++test % testw.length];
//                    button1.moveWidgetTo(10,next.x,next.y, FDEasings::easeOutBounce);
//                    return true;
//                }))
//                .setText(Component.literal("Click me!"),50,1,true)
//                .setTexture(new FDButtonTextures(FDLib.location("textures/gui/test.png")));
//
//        button.addChild("test",test2);
//        this.addRenderableWidget(button);
    }


    @Override
    public void render(GuiGraphics graphics, int mx, int my, float pticks) {
        super.render(graphics, mx, my, pticks);

        List<Vector2f> l = new ArrayList<>((List.of(
                new Vector2f(-50,-50),
                new Vector2f(-50,50),
                new Vector2f(50,50),
                new Vector2f(50,-50)
        )));

        float rotangle = (System.currentTimeMillis() % Integer.MAX_VALUE) / 2000f;

        Vector3f v = new Vector3f(0,1,0).rotateZ(rotangle);


        var list = FDMathUtil.scalePointsInDirection(l,new Vector2f(v.x,v.y),2f);

        Vector2f a = this.getAnchor(0.5f,0.5f);

        int d = 0;

        float pw = 10;
        for (var point : list){
            float col = d / (float) l.size();

            FDRenderUtil.fill(graphics.pose(),a.x + point.x - pw/2,a.y + point.y - pw/2,pw,pw,col,col,col,1f);
            d++;

        }

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
