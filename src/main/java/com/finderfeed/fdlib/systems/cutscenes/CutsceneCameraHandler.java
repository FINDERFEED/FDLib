package com.finderfeed.fdlib.systems.cutscenes;

import com.finderfeed.fdlib.FDClientHelpers;
import com.finderfeed.fdlib.FDLib;
import com.finderfeed.fdlib.data_structures.ObjectHolder;
import com.finderfeed.fdlib.init.FDClientModEvents;
import com.finderfeed.fdlib.systems.screen.screen_particles.ScreenParticlesRenderEvent;
import com.finderfeed.fdlib.util.math.FDMathUtil;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import net.minecraft.client.player.Input;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.*;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import org.lwjgl.glfw.GLFW;

@EventBusSubscriber(modid = FDLib.MOD_ID,value = Dist.CLIENT,bus = EventBusSubscriber.Bus.GAME)
public class CutsceneCameraHandler {


    private static ClientCameraEntity clientCameraEntity;
    private static Entity previousCameraEntity;
    private static CutsceneExecutor cutsceneExecutor;

    @SubscribeEvent
    private static void onLogoff(ClientPlayerNetworkEvent.LoggingOut out){
        clientCameraEntity = null;
        previousCameraEntity = null;
        cutsceneExecutor = null;
    }

    @SubscribeEvent
    public static void testCamera(InputEvent.Key event){
        if (Minecraft.getInstance().level == null || Minecraft.getInstance().screen != null) return;

        int key = event.getKey();
        int action = event.getAction();

        if (action != GLFW.GLFW_PRESS) return;

        if (key == GLFW.GLFW_KEY_H) {
            Entity c = Minecraft.getInstance().cameraEntity;

            if (!(c instanceof ClientCameraEntity camera)) {

                Player player = Minecraft.getInstance().player;

                Vec3 eyePos = player.getEyePosition();

                CutsceneData data = CutsceneData.create()
                        .stopMode(CutsceneData.StopMode.UNSTOPPABLE)
                        .time(200)
                        .lookEasing(EasingType.LINEAR)
                        .timeEasing(EasingType.EASE_IN_OUT)
                        .moveCurveType(CurveType.LINEAR);

                int segments = 64;
                float angle = FDMathUtil.FPI * 2 / segments;
                Vec3 base = player.position().add(0,5,0);
                for (int i = 0; i <= segments; i++){

                    Vec3 v = new Vec3(15,0,0).yRot(i * angle);
                    Vec3 pos = base.add(v);

                    Vec3 look = player.position().add(0,1.5,0).subtract(pos);

                    data.addCameraPos(new CameraPos(pos,look));

                }

                startCutscene(data);
            }else{
                stopCutscene();
            }
        }

    }

    @SubscribeEvent
    public static void renderScreenParticlesEvent(ScreenParticlesRenderEvent.Gui event){
        if (isCutsceneActive()) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void renderScreenParticlesEvent2(ScreenParticlesRenderEvent.Screen event){
        if (isCutsceneActive()) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void playerTickEvent(PlayerTickEvent.Pre event){
        Player player = event.getEntity();
        if (!player.level().isClientSide) return;

        boolean wasStoppedByPlayer = false;
        while (FDClientModEvents.END_CUTSCENE.consumeClick()){
            wasStoppedByPlayer = true;
        }

        if (!isCutsceneActive()) return;


        LocalPlayer localPlayer = (LocalPlayer) player;
        nullifyInput(localPlayer);

        if (cutsceneExecutor.getData().getStopMode() == CutsceneData.StopMode.PLAYER && wasStoppedByPlayer){
            stopCutscene();
            return;
        }

        if (Minecraft.getInstance().options.getCameraType() != CameraType.FIRST_PERSON){
            Minecraft.getInstance().options.setCameraType(CameraType.FIRST_PERSON);
        }

        ensurePlayerIsACamera();

        cutsceneExecutor.tick(clientCameraEntity);

        if (cutsceneExecutor.hasEnded() && cutsceneExecutor.getData().getStopMode() == CutsceneData.StopMode.AUTOMATIC){
            stopCutscene();
        }

    }

    public static void nullifyInput(LocalPlayer player){
        Input input = player.input;
        input.leftImpulse = 0;
        input.forwardImpulse = 0;
        input.up = false;
        input.down = false;
        input.left = false;
        input.right = false;
        input.jumping = false;
        input.shiftKeyDown = false;
    }

    @SubscribeEvent
    public static void renderHighlightEvent(RenderHighlightEvent.Block event){
        if (isCutsceneActive()) {
            event.setCanceled(true);
        }

    }

    @SubscribeEvent
    public static void renderGuiLayers(RenderGuiLayerEvent.Pre event){
        if (isCutsceneActive()){
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void renderHand(RenderHandEvent event){
        if (isCutsceneActive()){
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void cancelPlayerMouseUsage(InputEvent.InteractionKeyMappingTriggered event){
        if (isCutsceneActive()){
            event.setCanceled(true);
            event.setSwingHand(false);
        }
    }

    @SubscribeEvent
    public static void cameraAngles(ViewportEvent.ComputeCameraAngles event){

        if (!isCutsceneActive()) return;

        ObjectHolder<Float> yaw = new ObjectHolder<>(event.getYaw());
        ObjectHolder<Float> pitch = new ObjectHolder<>(event.getPitch());
        ObjectHolder<Float> roll = new ObjectHolder<>(event.getRoll());


        cutsceneExecutor.setCameraRotation((float) event.getPartialTick(), yaw, pitch, roll);

        event.setPitch(pitch.getValue());
        event.setYaw(yaw.getValue());
        event.setRoll(roll.getValue());


    }


    private static void ensurePlayerIsACamera(){

        if (cutsceneExecutor != null && clientCameraEntity != null){
            Entity camera = Minecraft.getInstance().cameraEntity;
            if (!(camera instanceof ClientCameraEntity)){
                previousCameraEntity = camera;
                Minecraft.getInstance().setCameraEntity(clientCameraEntity);
            }
        }

    }

    public static void startCutscene(CutsceneData data){

        Level level = FDClientHelpers.getClientLevel();

        Entity currentCamera = Minecraft.getInstance().cameraEntity;
        if (currentCamera instanceof ClientCameraEntity) return;

        ClientCameraEntity camera = new ClientCameraEntity(level);

        CameraPos pos = data.getCameraPositions().get(0);

        Vec3 p = pos.getPos();

        camera.setPos(p);
        camera.xo = p.x;
        camera.yo = p.y;
        camera.zo = p.z;

        Minecraft.getInstance().setCameraEntity(camera);

        clientCameraEntity = camera;
        previousCameraEntity = currentCamera;

        cutsceneExecutor = new CutsceneExecutor(data);
    }


    public static void stopCutscene() {
        if (isCutsceneActive()) {
            clientCameraEntity = null;
            cutsceneExecutor = null;
            MouseHandler mouseHandler = Minecraft.getInstance().mouseHandler;
            mouseHandler.accumulatedDX = 0;
            mouseHandler.accumulatedDY = 0;
            Minecraft.getInstance().setCameraEntity(previousCameraEntity);
        }
    }

    public static CutsceneExecutor getCutsceneExecutor(){
        return cutsceneExecutor;
    }

    public static boolean isCutsceneActive(){
        return clientCameraEntity != null && cutsceneExecutor != null;
    }

}