package com.finderfeed.fdlib.systems.cutscenes.camera_motion;

import com.finderfeed.fdlib.data_structures.ObjectHolder;
import com.finderfeed.fdlib.systems.cutscenes.ClientCameraEntity;
import com.finderfeed.fdlib.systems.cutscenes.CutsceneData;
import net.minecraft.world.phys.Vec3;


public abstract class CameraLookProcessor {

    public abstract void rotate(CutsceneData cutsceneData, int currentTime, float partialTick, ObjectHolder<Float> yaw, ObjectHolder<Float> pitch);

}
