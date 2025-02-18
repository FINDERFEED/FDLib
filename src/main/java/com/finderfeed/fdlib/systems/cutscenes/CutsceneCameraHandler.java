package com.finderfeed.fdlib.systems.cutscenes;

import com.finderfeed.fdlib.FDClientHelpers;
import com.finderfeed.fdlib.FDLib;
import com.finderfeed.fdlib.data_structures.ObjectHolder;
import com.finderfeed.fdlib.init.FDClientModEvents;
import com.finderfeed.fdlib.util.math.FDMathUtil;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
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

    //TODO: TEST
    private static boolean movingForward = false;

    @SubscribeEvent
    public static void testCamera(InputEvent.Key event){
        if (Minecraft.getInstance().level == null || Minecraft.getInstance().screen != null) return;

        int key = event.getKey();
        int action = event.getAction();

        if (clientCameraEntity != null) {
            if (key == GLFW.GLFW_KEY_W) {
                if (action == GLFW.GLFW_PRESS) {
                    movingForward = true;
                } else if (action == GLFW.GLFW_RELEASE) {
                    movingForward = false;
                }
            }
        }

        if (action != GLFW.GLFW_PRESS) return;

        if (key == GLFW.GLFW_KEY_H) {
            Entity c = Minecraft.getInstance().cameraEntity;

            if (!(c instanceof ClientCameraEntity camera)) {

                Player player = Minecraft.getInstance().player;

                Vec3 eyePos = player.getEyePosition();

                CutsceneData data = CutsceneData.create()
                        .stopMode(CutsceneData.StopMode.UNSTOPPABLE)
                        .time(1000)
                        .lookEasing(EasingType.LINEAR)
                        .timeEasing(EasingType.EASE_IN_OUT)
                        .moveCurveType(CurveType.LINEAR);

                int segments = 32;
                float angle = FDMathUtil.FPI * 2 / segments;
                Vec3 base = player.position().add(0,10,0);
                for (float i = 0; i <= FDMathUtil.FPI * 2;i += angle){

                    Vec3 v = new Vec3(30,0,0).yRot(i);
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
    public static void playerTickEvent(PlayerTickEvent.Pre playerTickEvent){
        Player player = playerTickEvent.getEntity();
        if (!player.level().isClientSide) return;

        boolean wasStoppedByPlayer = false;
        while (FDClientModEvents.END_CUTSCENE.consumeClick()){
            wasStoppedByPlayer = true;
        }

        if (!isCutsceneActive()) return;

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
    public static void cameraAngles(ViewportEvent.ComputeCameraAngles event){

        if (!isCutsceneActive()) return;

        ObjectHolder<Float> yaw = new ObjectHolder<>(event.getYaw());
        ObjectHolder<Float> pitch = new ObjectHolder<>(event.getPitch());

        cutsceneExecutor.setYawAndPitch((float) event.getPartialTick(),yaw,pitch);

        event.setPitch(pitch.getValue());
        event.setYaw(yaw.getValue());

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
