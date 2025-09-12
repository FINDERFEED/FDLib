package com.finderfeed.fdlib.systems.screen.screen_effect;

import com.finderfeed.fdlib.FDLib;
import com.mojang.blaze3d.platform.Window;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Mod.EventBusSubscriber(bus =  Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT, modid = FDLib.MOD_ID)
public class ScreenEffectOverlay implements IGuiOverlay {

    private static final List<ScreenEffectInstance> screenEffects = new ArrayList<>();

    @Override
    public void render(ForgeGui gui, GuiGraphics guiGraphics, float partialTick, int screenWidth, int screenHeight) {
        Window window = Minecraft.getInstance().getWindow();
        float width = window.getGuiScaledWidth();
        float height = window.getGuiScaledHeight();
        for (ScreenEffectInstance instance : screenEffects){
            instance.effect.render(graphics,deltaTracker,instance.currentTime,width,height);
        }
    }

    @SubscribeEvent
    public static void tickEvent(TickEvent.ClientTickEvent event){

        if (event.phase != TickEvent.Phase.END) return;

        if (Minecraft.getInstance().isPaused()) return;

        Iterator<ScreenEffectInstance> screenEffectInstanceIterator = screenEffects.iterator();
        while (screenEffectInstanceIterator.hasNext()){
            ScreenEffectInstance instance = screenEffectInstanceIterator.next();
            if (instance.isFinished()){
                screenEffectInstanceIterator.remove();
            }else{
                instance.tick();
            }
        }
    }


    public static List<ScreenEffectInstance> getScreenEffects(){
        return screenEffects;
    }

    public static void addScreenEffect(ScreenEffect effect){
        screenEffects.add(new ScreenEffectInstance(effect));
    }

}
