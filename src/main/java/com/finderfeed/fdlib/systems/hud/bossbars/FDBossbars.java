package com.finderfeed.fdlib.systems.hud.bossbars;

import com.finderfeed.fdlib.FDLib;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.UUID;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE,modid = FDLib.MOD_ID,value = Dist.CLIENT)
public class FDBossbars {

    protected static final HashMap<UUID,FDBossBar> BOSS_BARS = new LinkedHashMap<>();


    @SubscribeEvent
    public static void tick(TickEvent.ClientTickEvent event){

        if (event.phase != TickEvent.Phase.START) return;

        if (Minecraft.getInstance().level == null){
            BOSS_BARS.clear();
            return;
        }

        float offs = FDBossBarsOverlay.calculateBossBarsOffset();

        for (FDBossBar bossBar : BOSS_BARS.values()){
            bossBar.tick(offs);
            offs += bossBar.height();
        }

    }

    @SubscribeEvent
    public static void onLogoff(ClientPlayerNetworkEvent.LoggingOut event){
        BOSS_BARS.clear();
    }

    public static void removeBossBar(UUID uuid){
        BOSS_BARS.remove(uuid);
    }

    public static void addBossBar(FDBossBar bossBar){
        BOSS_BARS.put(bossBar.getUUID(),bossBar);
    }

    public static FDBossBar getBossBar(UUID uuid){
        return BOSS_BARS.get(uuid);
    }



}
