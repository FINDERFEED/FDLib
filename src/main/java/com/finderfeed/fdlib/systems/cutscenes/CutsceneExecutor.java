package com.finderfeed.fdlib.systems.cutscenes;

import com.finderfeed.fdlib.data_structures.ObjectHolder;
import com.finderfeed.fdlib.systems.cutscenes.camera_motion.*;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

public class CutsceneExecutor {

    private CutsceneData data;

    private CameraLookProcessor lookProcessor;

    private CameraMotion cameraMotion;

    private int currentTime = 0;

    public CutsceneExecutor(CutsceneData data){
        this.data = data;
        if (data.getMoveType() == CurveType.LINEAR){
            cameraMotion = new LinearCameraMotion();
        }else{
            cameraMotion = new CatmullRomCameraMotion();
        }
        lookProcessor = new NormalLookProcessor();
    }

    public boolean tick(ClientCameraEntity camera){

        camera.xo = camera.getX();
        camera.yo = camera.getY();
        camera.zo = camera.getZ();

        if (currentTime >= data.getCutsceneTime()){
            return true;
        }

        Vec3 newPos = cameraMotion.calculateCameraPosition(data,currentTime,0);

        camera.setPos(newPos);

        currentTime = Mth.clamp(currentTime + 1,0,data.getCutsceneTime());
        return false;
    }

    public void setYawAndPitch(float partialTick, ObjectHolder<Float> yaw,ObjectHolder<Float> pitch){
        lookProcessor.rotate(this.data,currentTime,partialTick,yaw,pitch);
    }


    public boolean hasEnded(){
        return currentTime >= data.getCutsceneTime();
    }


    public CutsceneData getData() {
        return data;
    }

    public int getCurrentTime() {
        return currentTime;
    }
}
