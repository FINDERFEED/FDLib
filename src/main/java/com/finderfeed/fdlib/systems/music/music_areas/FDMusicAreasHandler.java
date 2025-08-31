package com.finderfeed.fdlib.systems.music.music_areas;

import com.finderfeed.fdlib.FDLib;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.util.thread.EffectiveSide;
import net.neoforged.neoforge.event.server.ServerStoppedEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

import java.util.HashMap;
import java.util.Iterator;
import java.util.UUID;

@EventBusSubscriber(modid = FDLib.MOD_ID)
public class FDMusicAreasHandler {

    private static final HashMap<UUID, FDMusicArea> MUSIC_AREAS = new HashMap<>();

    @SubscribeEvent
    public static void removeAreas(ServerStoppedEvent event){
        MUSIC_AREAS.clear();
    }

    @SubscribeEvent
    public static void tickAreas(ServerTickEvent.Pre event){


        var iterator = MUSIC_AREAS.entrySet().iterator();

        while (iterator.hasNext()){

            var pair = iterator.next();

            FDMusicArea area = pair.getValue();


            if (area.shouldBeDeleted()){
                area.onRemoval(event.getServer());
                iterator.remove();
            }else {
                ServerLevel level = event.getServer().getLevel(area.getDimension());
                if (level != null) {
                    area.tick(level);
                }
                area.tickDeletionTicker(level == null);
            }

        }
    }

    public static void removeArea(MinecraftServer server, UUID uuid){
        FDMusicArea area = getMusicArea(uuid);
        if (area != null) {
            area.onRemoval(server);
            MUSIC_AREAS.remove(uuid);
        }
    }

    public static void addArea(UUID uuid, FDMusicArea musicArea){
        if (!MUSIC_AREAS.containsKey(uuid)) {
            MUSIC_AREAS.put(uuid, musicArea);
        }
    }

    public static FDMusicArea getMusicArea(UUID uuid){
        return MUSIC_AREAS.get(uuid);
    }

    public static boolean hasMusicArea(UUID uuid){
        return MUSIC_AREAS.containsKey(uuid);
    }

}
