package com.finderfeed.fdlib.systems.hud;


import com.finderfeed.fdlib.FDLib;
import com.finderfeed.fdlib.systems.hud.bossbars.FDBossBarsOverlay;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

@EventBusSubscriber(modid = FDLib.MOD_ID,value = Dist.CLIENT,bus = EventBusSubscriber.Bus.MOD)
public class FDHuds {



    @SubscribeEvent
    public static void registerOverlays(RegisterGuiLayersEvent event){

        event.registerAboveAll(ResourceLocation.tryBuild(FDLib.MOD_ID,"boss_bars"),new FDBossBarsOverlay());

    }



}
