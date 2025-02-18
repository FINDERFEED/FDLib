package com.finderfeed.fdlib.systems.cutscenes;

import com.finderfeed.fdlib.FDClientHelpers;
import com.finderfeed.fdlib.FDHelpers;
import com.finderfeed.fdlib.FDLib;
import com.finderfeed.fdlib.util.math.FDMathUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.client.event.ViewportEvent;
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
    public static void keyEvent(InputEvent.Key event){
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

        if (key == GLFW.GLFW_KEY_K) {
            Entity c = Minecraft.getInstance().cameraEntity;

            if (!(c instanceof ClientCameraEntity camera)) {

                Player player = Minecraft.getInstance().player;

                Vec3 eyePos = player.getEyePosition();

                CutsceneData data = CutsceneData.create()
                        .time(500)
                        .moveCurveType(CurveType.CATMULLROM)
                        .timeEasing(EasingType.LINEAR)
                        .addCameraPos(new CameraPos(eyePos.add(10,0,0),Vec3.ZERO))
                        .addCameraPos(new CameraPos(eyePos.add(0,0,10),Vec3.ZERO))
                        .addCameraPos(new CameraPos(eyePos.add(-10,0,0),Vec3.ZERO))
                        .addCameraPos(new CameraPos(eyePos.add(0,0,-10),Vec3.ZERO))
                        .addCameraPos(new CameraPos(eyePos.add(10,0,0),Vec3.ZERO))

                        ;

                initiateCamera(data);
            }else{
                stopCamera();
            }
        }

    }

    @SubscribeEvent
    public static void playerTickEvent(PlayerTickEvent.Pre playerTickEvent){
        Player player = playerTickEvent.getEntity();
        if (!player.level().isClientSide) return;
        if (clientCameraEntity == null || cutsceneExecutor == null) return;


        setCameraOlds();

        cutsceneExecutor.tick(clientCameraEntity);

        if (cutsceneExecutor.hasEnded()){
            stopCamera();
        }

    }

    @SubscribeEvent
    public static void cameraAngles(ViewportEvent.ComputeCameraAngles event){

        if (clientCameraEntity == null || cutsceneExecutor == null) return;

        float yRotO = FDMathUtil.convertMCYRotationToNormal(clientCameraEntity.yRotO);
        float yRot = FDMathUtil.convertMCYRotationToNormal(clientCameraEntity.getYRot());

        event.setYaw((float) FDMathUtil.lerpAround(yRotO,yRot,-180,180,(float) event.getPartialTick()));


    }

    private static void setCameraOlds(){
        clientCameraEntity.yRotO = clientCameraEntity.getYRot();
        clientCameraEntity.xRotO = clientCameraEntity.getXRot();
        clientCameraEntity.xo = clientCameraEntity.getX();
        clientCameraEntity.yo = clientCameraEntity.getY();
        clientCameraEntity.zo = clientCameraEntity.getZ();
    }

    private static void setCameraRotation(Vec3 v){
        float xRot = FDMathUtil.xRotFromVector(v);
        float yRot = FDMathUtil.yRotFromVector(v);
        setCameraRotation(xRot,yRot);
    }

    private static void setCameraRotation(float xRot,float yRot) {
        clientCameraEntity.setXRot(xRot);
        clientCameraEntity.setYRot(yRot);
    }

    private static void setCameraPosition(Vec3 pos){
        setCameraPosition(pos.x,pos.y,pos.z);
    }

    private static void setCameraPosition(double x,double y,double z){
        clientCameraEntity.setPos(x,y,z);
    }


    public static void initiateCamera(CutsceneData data){

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

    public static void stopCamera(){
        clientCameraEntity = null;
        cutsceneExecutor = null;
        Minecraft.getInstance().setCameraEntity(previousCameraEntity);
    }



}
