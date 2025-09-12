package com.finderfeed.fdlib.systems.hud;


import com.finderfeed.fdlib.FDLib;
import com.finderfeed.fdlib.systems.hud.bossbars.FDBossBarsOverlay;
import com.finderfeed.fdlib.systems.screen.screen_effect.ScreenEffectOverlay;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.neoforge.client.event.RegisterGuiLayersEvent;

@Mod.EventBusSubscriber(modid = FDLib.MOD_ID,value = Dist.CLIENT,bus = Mod.EventBusSubscriber.Bus.MOD)
public class FDHuds {

    public static final ScreenEffectOverlay SCREEN_EFFECT_OVERLAY = new ScreenEffectOverlay();



    @SubscribeEvent
    public static void registerOverlays(RegisterGuiOverlaysEvent event){

        event.registerAboveAll(FDLib.location("boss_bars").toString(),new FDBossBarsOverlay());
        event.registerAboveAll(FDLib.location("screen_effect").toString(),SCREEN_EFFECT_OVERLAY);

    }



}
