package com.finderfeed.fdlib.systems.simple_screen;

import com.finderfeed.fdlib.util.rendering.FDRenderUtil;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.Items;
import org.joml.Vector2f;

//@Mod.EventBusSubscriber(bus = EventBusSubscriber.Bus.FORGE, modid = FDLib.MOD_ID)
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

        FDRenderUtil.renderScaledItemStack(graphics,mx,my,2f,Items.ACACIA_DOOR.getDefaultInstance());
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
