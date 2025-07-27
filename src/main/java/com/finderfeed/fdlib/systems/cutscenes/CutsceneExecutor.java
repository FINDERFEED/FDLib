package com.finderfeed.fdlib.systems.cutscenes;

import com.finderfeed.fdlib.data_structures.ObjectHolder;
import com.finderfeed.fdlib.systems.cutscenes.camera_motion.*;
import com.finderfeed.fdlib.systems.screen.screen_effect.ScreenEffect;
import com.finderfeed.fdlib.systems.screen.screen_effect.ScreenEffectData;
import com.finderfeed.fdlib.systems.screen.screen_effect.ScreenEffectOverlay;
import com.finderfeed.fdlib.systems.screen.screen_effect.ScreenEffectType;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

import java.util.List;

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

        if (currentTime > data.getCutsceneTime()){
            return true;
        }

        List<CutsceneScreenEffectData.ScreenEffectInstance<?, ?>> effects = data.getScreenEffectData().getAllEffectsOnTick(currentTime);

        for (var effect : effects){
            var data = effect.data();
            var type = effect.type();
            var screenEffect = this.useScreenEffectFactory(type,data,effect.inTime(),effect.stayTime(),effect.outTime());
            ScreenEffectOverlay.addScreenEffect(screenEffect);
        }


        Vec3 newPos = this.getCameraPos();

        camera.setPos(newPos);

        currentTime = Mth.clamp(currentTime + 1,0,data.getCutsceneTime());

        return false;
    }

    private <A extends ScreenEffectData, B extends ScreenEffect<A>> ScreenEffect<A> useScreenEffectFactory(ScreenEffectType<A,B> screenEffectType, ScreenEffectData data, int inTime, int stayTime, int outTime){
        var factory = screenEffectType.factory;
        var effect = factory.create((A) data, inTime, outTime, stayTime);
        return effect;
    }

    public Vec3 getCameraPos(){
        Vec3 newPos = cameraMotion.calculateCameraPosition(data,currentTime,0);
        return newPos;
    }

    public void setCameraRotation(float partialTick, ObjectHolder<Float> yaw, ObjectHolder<Float> pitch, ObjectHolder<Float> roll){
        lookProcessor.rotate(this.data,currentTime,partialTick,yaw, pitch, roll);
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
