package com.finderfeed.fdlib.init;


import com.finderfeed.fdlib.FDLib;
import com.finderfeed.fdlib.FDLibCalls;
import com.finderfeed.fdlib.network.FDPacketHandler;
import com.finderfeed.fdlib.systems.FDRegistries;
import com.finderfeed.fdlib.systems.config.JsonConfig;
import com.finderfeed.fdlib.systems.config.packets.JsonConfigSyncPacket;
import com.finderfeed.fdlib.systems.config.packets.TriggerClientsideConfigReloadPacket;
import com.finderfeed.fdlib.systems.shake.FDShakeData;
import com.finderfeed.fdlib.systems.shake.PositionedScreenShakePacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.util.thread.EffectiveSide;
import net.minecraftforge.network.NetworkDirection;

@Mod.EventBusSubscriber(modid = FDLib.MOD_ID,bus = Mod.EventBusSubscriber.Bus.FORGE)
public class FDGameEvents {

    @SubscribeEvent
    public static void serverStartedEvent(ServerStartedEvent event){
        FDLib.LOGGER.info("Loading FD configs...");

        for (JsonConfig config : FDRegistries.CONFIGS.get()){
            config.loadFromDisk();
        }

        FDLib.LOGGER.info("Loaded FD configs.");
    }

    @SubscribeEvent
    public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event){
        if (event.getEntity() instanceof ServerPlayer player){
            FDPacketHandler.INSTANCE.sendTo(new JsonConfigSyncPacket(), player.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
            FDPacketHandler.INSTANCE.sendTo(new TriggerClientsideConfigReloadPacket(false), player.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
        }
    }

    @SubscribeEvent
    public static void testCutscenes(TickEvent.PlayerTickEvent event){

        if (event.phase != TickEvent.Phase.START) return;

        Player player = event.player;

        if (player instanceof ServerPlayer serverPlayer && serverPlayer.isCrouching()){


            PositionedScreenShakePacket.send((ServerLevel)player.level(), FDShakeData.builder()
                            .outTime(10)
                            .frequency(20)
                            .amplitude(1)
                            .stayTime(10)
                    .build(),player.position(),20);

//            Vec3 pos = serverPlayer.position();
//
//            Vec3 previuousEndPos;
//
//            CutsceneData data1 = CutsceneData.create()
//                    .addScreenEffect(0, FDScreenEffects.SCREEN_COLOR, new ScreenColorData(0,0,0,1), 0,0,20)
//                    .addScreenEffect(20, FDScreenEffects.SCREEN_COLOR, new ScreenColorData(0,0,0,1), 20,0,0)
//                    .time(40)
//                    .addCameraPos(new CameraPos(pos.add(0,0,0),new Vec3(1,0,0)))
//                    .addCameraPos(new CameraPos(pos.add(10,0,5),new Vec3(1,0,0)))
//                    .addCameraPos(new CameraPos(previuousEndPos = pos.add(20,0,0),new Vec3(1,0,0)));
//
//            CutsceneData data2 = CutsceneData.create()
//                    .addScreenEffect(0, FDScreenEffects.SCREEN_COLOR, new ScreenColorData(0,0,0,1), 0,0,20)
//                    .addScreenEffect(20, FDScreenEffects.SCREEN_COLOR, new ScreenColorData(0,0,0,1), 20,0,0)
//                    .time(40)
//                    .addCameraPos(new CameraPos(previuousEndPos.add(0,0,0),new Vec3(0,0,1)))
//                    .addCameraPos(new CameraPos(previuousEndPos.add(-5,0,10),new Vec3(0,0,1)))
//                    .addCameraPos(new CameraPos(previuousEndPos = previuousEndPos.add(0,0,20),new Vec3(0,0,1)));
//
//            CutsceneData data3 = CutsceneData.create()
//                    .addScreenEffect(0, FDScreenEffects.SCREEN_COLOR, new ScreenColorData(0,0,0,1), 0,0,20)
//                    .addScreenEffect(20, FDScreenEffects.SCREEN_COLOR, new ScreenColorData(0,0,0,1), 20,0,0)
//                    .time(40)
//                    .addCameraPos(new CameraPos(previuousEndPos.add(0,0,0),new Vec3(-1,0,0)))
//                    .addCameraPos(new CameraPos(previuousEndPos.add(-10,0,-5),new Vec3(-1,0,0)))
//                    .addCameraPos(new CameraPos(previuousEndPos = previuousEndPos.add(-20,0,0),new Vec3(-1,0,0)));
//
//            CutsceneData data4 = CutsceneData.create()
//                    .addScreenEffect(0, FDScreenEffects.SCREEN_COLOR, new ScreenColorData(0,0,0,1), 0,0,20)
//                    .addScreenEffect(20, FDScreenEffects.SCREEN_COLOR, new ScreenColorData(0,0,0,1), 20,0,0)
//                    .time(40)
//                    .addCameraPos(new CameraPos(previuousEndPos.add(0,0,0),new Vec3(0,0,-1)))
//                    .addCameraPos(new CameraPos(previuousEndPos.add(5,0,-10),new Vec3(0,0,-1)))
//                    .addCameraPos(new CameraPos(previuousEndPos.add(0,0,-20),new Vec3(0,0,-1)));
//
//            data1.nextCutscene(data2);
//            data2.nextCutscene(data3);
//            data3.nextCutscene(data4);
//
//
//            FDLibCalls.startCutsceneForPlayer(serverPlayer, data1);


        }

    }

//    @SubscribeEvent
//    public static void test(InputEvent.Key event){
//        if (event.getKey() == GLFW.GLFW_KEY_J){
//
//            ImpactFramesHandler.addImpactFrame(new ImpactFrame(0.5f,0.05f,2,false));
//
//        }
//    }

}
