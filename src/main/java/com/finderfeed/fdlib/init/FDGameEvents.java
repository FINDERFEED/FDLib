package com.finderfeed.fdlib.init;


import com.finderfeed.fdlib.FDLib;
import com.finderfeed.fdlib.FDLibCalls;
import com.finderfeed.fdlib.systems.FDRegistries;
import com.finderfeed.fdlib.systems.config.JsonConfig;
import com.finderfeed.fdlib.systems.config.packets.JsonConfigSyncPacket;
import com.finderfeed.fdlib.systems.config.packets.TriggerClientsideConfigReloadPacket;
import com.finderfeed.fdlib.systems.cutscenes.CameraPos;
import com.finderfeed.fdlib.systems.cutscenes.CutsceneCameraHandler;
import com.finderfeed.fdlib.systems.cutscenes.CutsceneData;
import com.finderfeed.fdlib.systems.impact_frames.ImpactFrame;
import com.finderfeed.fdlib.systems.impact_frames.ImpactFramesHandler;
import com.finderfeed.fdlib.systems.screen.screen_effect.instances.datas.ScreenColorData;
import com.finderfeed.fdlib.util.client.particles.lightning_particle.LightningParticleOptions;
import net.minecraft.client.Minecraft;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.server.ServerStartedEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import org.lwjgl.glfw.GLFW;

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
            PacketDistributor.sendToPlayer(player,new TriggerClientsideConfigReloadPacket(false));
        }
    }

//    @SubscribeEvent
//    public static void testCutscenes(PlayerTickEvent.Pre event){
//
//        Player player = event.getEntity();
//
//        if (player instanceof ServerPlayer serverPlayer && serverPlayer.isCrouching()){
//
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
//
//
//        }
//
//    }

//    @SubscribeEvent
//    public static void test(InputEvent.Key event){
//        if (event.getKey() == GLFW.GLFW_KEY_J){
//
//            ImpactFramesHandler.addImpactFrame(new ImpactFrame(0.5f,0.05f,2,false));
//
//        }
//    }

}
