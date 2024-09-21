package com.finderfeed.fdlib.to_other_mod;

import com.finderfeed.fdlib.FDLib;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.InputEvent;
import org.lwjgl.glfw.GLFW;

@EventBusSubscriber(modid = FDLib.MOD_ID,bus = EventBusSubscriber.Bus.GAME,value = Dist.CLIENT)
public class BossClientEvents {


    @SubscribeEvent
    public static void inputEvent(InputEvent.Key event){
        if (event.getKey() == GLFW.GLFW_KEY_I && Minecraft.getInstance().screen == null){
//            Minecraft.getInstance().setScreen(new ComponentTestScreen());
//            System.out.println(Minecraft.getInstance().font.getSplitter().splitLines("Test string to\nsplit",300, Style.EMPTY)
//                    .stream().map(FormattedText::getString).toList());
        }
    }


}
