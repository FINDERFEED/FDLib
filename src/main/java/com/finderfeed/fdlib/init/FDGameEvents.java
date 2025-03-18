package com.finderfeed.fdlib.init;


import com.finderfeed.fdlib.FDLib;
import com.finderfeed.fdlib.FDLibCalls;
import com.finderfeed.fdlib.systems.FDRegistries;
import com.finderfeed.fdlib.systems.config.JsonConfig;
import com.finderfeed.fdlib.systems.config.packets.JsonConfigSyncPacket;
import com.finderfeed.fdlib.systems.screen.screen_effect.instances.datas.ScreenColorData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.AddReloadListenerEvent;
import net.neoforged.neoforge.event.OnDatapackSyncEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.server.ServerStartedEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;

@EventBusSubscriber(modid = FDLib.MOD_ID,bus = EventBusSubscriber.Bus.GAME)
public class FDGameEvents {

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

    @SubscribeEvent
    public static void test(PlayerTickEvent.Pre event){
        Player player = event.getEntity();
        if (!player.level().isClientSide && player.isCrouching() && player.level().getGameTime() % 400 == 0){
            FDLibCalls.sendScreenEffect((ServerPlayer) player, FDScreenEffects.SCREEN_COLOR.get(),new ScreenColorData(1f,0f,1f,1f),
                    0,10,30
            );
        }
    }

}
