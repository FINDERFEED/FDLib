package com.finderfeed.fdlib.systems.hud.bossbars;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.UUID;

public class FDBossbars {

    protected static final HashMap<UUID,FDBossBar> BOSS_BARS = new LinkedHashMap<>();


    @SubscribeEvent
    public static void tick(PlayerTickEvent event){

        for (FDBossBar bossBar : BOSS_BARS.values()){
            bossBar.tick();
        }

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
