package com.finderfeed.fdlib.systems.cutscenes.camera_motion;

import com.finderfeed.fdlib.data_structures.ObjectHolder;
import com.finderfeed.fdlib.systems.cutscenes.CutsceneData;


public abstract class CameraLookProcessor {

    public abstract void rotate(CutsceneData cutsceneData, int currentTime, float partialTick, ObjectHolder<Float> yaw, ObjectHolder<Float> pitch, ObjectHolder<Float> roll);

}
