package com.finderfeed.fdlib.init;


import com.finderfeed.fdlib.FDLib;
import com.finderfeed.fdlib.systems.FDRegistries;
import com.finderfeed.fdlib.systems.config.JsonConfig;
import com.finderfeed.fdlib.systems.config.packets.JsonConfigSyncPacket;
import com.finderfeed.fdlib.systems.cutscenes.CameraPos;
import com.finderfeed.fdlib.systems.cutscenes.CutsceneCameraHandler;
import com.finderfeed.fdlib.systems.cutscenes.CutsceneData;
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
        }
    }

    @SubscribeEvent
    public static void test(InputEvent.Key event){
        if (event.getKey() == GLFW.GLFW_KEY_J){

            Player player = Minecraft.getInstance().player;

            CutsceneData cutsceneData = new CutsceneData();

            cutsceneData.time(100);
            cutsceneData.addCameraPos(new CameraPos(player.position(), new Vec3(1,0,0)));
            cutsceneData.addCameraPos(new CameraPos(player.position().add(10,0,0), new Vec3(0,0,1)));
            cutsceneData.stopMode(CutsceneData.StopMode.UNSTOPPABLE);

            CutsceneCameraHandler.startCutscene(cutsceneData);


        }else if (event.getKey() == GLFW.GLFW_KEY_T){
            Player player = Minecraft.getInstance().player;

            CutsceneData cutsceneData = new CutsceneData();

            cutsceneData.time(100);
            cutsceneData.addCameraPos(new CameraPos(player.position(), new Vec3(1,0,0)));
            cutsceneData.addCameraPos(new CameraPos(player.position().add(10,0,0), new Vec3(0,0,1)));

            CutsceneCameraHandler.moveCamera(cutsceneData);
        }
    }

}
