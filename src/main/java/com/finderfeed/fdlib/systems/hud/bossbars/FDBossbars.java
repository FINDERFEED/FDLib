package com.finderfeed.fdlib.systems.hud.bossbars;

import com.finderfeed.fdlib.FDLib;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.UUID;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.GAME,modid = FDLib.MOD_ID,value = Dist.CLIENT)
public class FDBossbars {

    protected static final HashMap<UUID,FDBossBar> BOSS_BARS = new LinkedHashMap<>();


    @SubscribeEvent
    public static void tick(PlayerTickEvent.Pre event){
        if (!event.getEntity().level().isClientSide) return;

        for (FDBossBar bossBar : BOSS_BARS.values()){
            bossBar.tick();
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
