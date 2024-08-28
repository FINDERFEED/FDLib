package com.finderfeed.fdlib.init;


import com.finderfeed.fdlib.FDLib;
import com.finderfeed.fdlib.systems.FDRegistries;
import com.finderfeed.fdlib.systems.config.JsonConfig;
import com.finderfeed.fdlib.systems.config.packets.JsonConfigSyncPacket;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.AddReloadListenerEvent;
import net.neoforged.neoforge.event.OnDatapackSyncEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.server.ServerStartedEvent;
import net.neoforged.neoforge.network.PacketDistributor;

@EventBusSubscriber(modid = FDLib.MOD_ID,bus = EventBusSubscriber.Bus.GAME)
public class FDGameEvents {

    //        if (level instanceof ServerLevel s && s.getGameTime() % 200 == 0){
//            ModList modList = ModList.get();
//            var modFiles = modList.getModFiles();
//
//            for (var info : modFiles){
//                for (var eee : info.getMods()){
//                    String namespace = eee.getNamespace();
//                    Path path = info.getFile().findResource("assets",namespace,"shaders","program");
//                    if (Files.exists(path)){
//                        try {
//                            List<Path> paths = recursivelyCollectFilePaths(path);
//                            for (Path p : paths){
//                                System.out.println(readFile(p));
//                            }
//                        }catch (Exception e){
//                            System.out.println("error");
//                            e.printStackTrace();
//                        }
//                    }
//                }
//            }
//        }else{
//
//        }

    @SubscribeEvent
    public static void registerReloadListeners(AddReloadListenerEvent event){
//        event.addListener(new ModelReloadableResourceListener());
//        event.addListener(new AnimationReloadableResourceListener());
    }

    @SubscribeEvent
    public static void onDatapackSync(OnDatapackSyncEvent event){
//        for (ServerPlayer player : event.getRelevantPlayers().toList()){
//            PacketDistributor.sendToPlayer(player, new SyncModelsPacket());
//            PacketDistributor.sendToPlayer(player, new AnimationsSyncPacket());
//        }
    }

    @SubscribeEvent
    public static void serverStartedEvent(ServerStartedEvent event){
        FDLib.LOGGER.info("Loading FD configs...");

        for (JsonConfig config : FDRegistries.CONFIGS){
            config.loadFromDisk();
        }

        FDLib.LOGGER.info("Loaded FD configs.");
    }

    @SubscribeEvent
    public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event){
        if (event.getEntity() instanceof ServerPlayer player){
            PacketDistributor.sendToPlayer(player,new JsonConfigSyncPacket());
        }
    }

}
