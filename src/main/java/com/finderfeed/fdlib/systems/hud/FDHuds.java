package com.finderfeed.fdlib.systems.hud;


import com.finderfeed.fdlib.FDLib;
import com.finderfeed.fdlib.systems.broken_screen_effect.ShatteredScreenEffectHandler;
import com.finderfeed.fdlib.systems.hud.bossbars.FDBossBarsOverlay;
import com.finderfeed.fdlib.systems.screen.screen_effect.ScreenEffectOverlay;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;

@EventBusSubscriber(modid = FDLib.MOD_ID,value = Dist.CLIENT,bus = EventBusSubscriber.Bus.MOD)
public class FDHuds {

    public static final ScreenEffectOverlay SCREEN_EFFECT_OVERLAY = new ScreenEffectOverlay();


    @SubscribeEvent
    public static void registerOverlays(RegisterGuiLayersEvent event){

        event.registerAboveAll(FDLib.location("boss_bars"),new FDBossBarsOverlay());
        event.registerAboveAll(FDLib.location("screen_effect"),SCREEN_EFFECT_OVERLAY);
        event.registerBelow(FDLib.location("boss_bars"), FDLib.location("shattered_screen"), new ShatteredScreenEffectHandler());

    }



}
