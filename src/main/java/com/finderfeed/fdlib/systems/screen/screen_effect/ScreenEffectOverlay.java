package com.finderfeed.fdlib.systems.screen.screen_effect;

import com.finderfeed.fdlib.FDLib;
import com.mojang.blaze3d.platform.Window;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.LayeredDraw;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@EventBusSubscriber(bus =  EventBusSubscriber.Bus.GAME, value = Dist.CLIENT, modid = FDLib.MOD_ID)
public class ScreenEffectOverlay implements LayeredDraw.Layer {

    private static final List<ScreenEffectInstance> screenEffects = new ArrayList<>();

    @Override
    public void render(GuiGraphics graphics, DeltaTracker deltaTracker) {
        Window window = Minecraft.getInstance().getWindow();
        float width = window.getGuiScaledWidth();
        float height = window.getGuiScaledHeight();
        for (ScreenEffectInstance instance : screenEffects){
            instance.effect.render(graphics,deltaTracker,instance.currentTime,width,height);
        }
    }

    @SubscribeEvent
    public static void tickEvent(ClientTickEvent.Post event){
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
