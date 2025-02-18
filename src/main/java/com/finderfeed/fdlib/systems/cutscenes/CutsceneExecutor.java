package com.finderfeed.fdlib.systems.cutscenes;

import com.finderfeed.fdlib.systems.cutscenes.camera_motion.CameraMotion;
import com.finderfeed.fdlib.systems.cutscenes.camera_motion.CatmullRomCameraMotion;
import com.finderfeed.fdlib.systems.cutscenes.camera_motion.LinearCameraMotion;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

public class CutsceneExecutor {

    private CutsceneData data;

    private CameraMotion cameraMotion;

    private int currentTime = 0;

    public CutsceneExecutor(CutsceneData data){
        this.data = data;
        if (data.getMoveType() == CurveType.LINEAR){
            cameraMotion = new LinearCameraMotion();
        }else{
            cameraMotion = new CatmullRomCameraMotion();
        }
    }

    public boolean tick(ClientCameraEntity camera){
        if (currentTime >= data.getCutsceneTime()){
            return true;
        }

        camera.xo = camera.getX();
        camera.yo = camera.getY();
        camera.zo = camera.getZ();

        Vec3 newPos = cameraMotion.calculateCameraPosition(data,currentTime,0);

        camera.setPos(newPos);

        currentTime = Mth.clamp(currentTime + 1,0,data.getCutsceneTime());
        return false;
    }


    public boolean hasEnded(){
        return currentTime >= data.getCutsceneTime();
    }


}
