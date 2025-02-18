package com.finderfeed.fdlib.systems.cutscenes;

public class CutsceneUtil {


    public static float getPercent(CutsceneData data,int currentTime,float partialTick){
        return (currentTime + partialTick) / data.getCutsceneTime();
    }

}
