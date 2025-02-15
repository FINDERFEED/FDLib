package com.finderfeed.fdlib.systems.cutscenes.test;

import com.finderfeed.fdlib.FDClientHelpers;
import com.finderfeed.fdlib.FDClientPacketExecutables;
import com.finderfeed.fdlib.FDHelpers;
import com.finderfeed.fdlib.FDLib;
import com.finderfeed.fdlib.systems.cutscenes.ClientCameraEntity;
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
public class CameraHandler {


    private static ClientCameraEntity clientCameraEntity;
    private static Entity previousCameraEntity;


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
                initiateCamera();
            }else{
                stopCamera();
            }
        }

    }

    @SubscribeEvent
    public static void playerTickEvent(PlayerTickEvent.Pre playerTickEvent){
        Player player = playerTickEvent.getEntity();
        if (!player.level().isClientSide) return;
        if (clientCameraEntity == null) return;


        setCameraOlds();
        setCameraRotation(player.getLookAngle());
        if (movingForward){
            Vec3 pos = clientCameraEntity.position();
            setCameraPosition(pos.add(clientCameraEntity.getLookAngle()));
        }

    }

    @SubscribeEvent
    public static void cameraAngles(ViewportEvent.ComputeCameraAngles event){

        if (clientCameraEntity == null) return;

        float yRotO = clientCameraEntity.yRotO;
        float yRot = clientCameraEntity.getYRot();

        event.setYaw((float) FDMathUtil.lerp(yRotO,yRot,event.getPartialTick()));

    }

    private static void setCameraOlds(){
        clientCameraEntity.yRotO = clientCameraEntity.getYRot();
        clientCameraEntity.xRotO = clientCameraEntity.getXRot();
        clientCameraEntity.xo = clientCameraEntity.getX();
        clientCameraEntity.yo = clientCameraEntity.getY();
        clientCameraEntity.zo = clientCameraEntity.getZ();
    }

    private static void setCameraRotation(Vec3 v){
        float xRot = FDHelpers.xRotFromVector(v);
        float yRot = FDHelpers.yRotFromVector(v);
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


    public static void initiateCamera(){

        Level level = FDClientHelpers.getClientLevel();

        Entity currentCamera = Minecraft.getInstance().cameraEntity;
        if (currentCamera instanceof ClientCameraEntity) return;


        Entity player = currentCamera;

        ClientCameraEntity camera = new ClientCameraEntity(level);
        Vec3 pos = player.position().add(0,player.getEyeHeight(),0);
        camera.setPos(pos);
        camera.xo = pos.x;
        camera.yo = pos.y;
        camera.zo = pos.z;

        Minecraft.getInstance().setCameraEntity(camera);

        clientCameraEntity = camera;
        previousCameraEntity = currentCamera;
    }

    public static void stopCamera(){
        clientCameraEntity = null;
        Minecraft.getInstance().setCameraEntity(previousCameraEntity);
    }



}
