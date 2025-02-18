package com.finderfeed.fdlib.systems.cutscenes.camera_motion;

import com.finderfeed.fdlib.systems.cutscenes.CutsceneData;
import net.minecraft.world.phys.Vec3;

public abstract class CameraMotion {

    public abstract Vec3 calculateCameraPosition(CutsceneData data,int currentTime,float partialTick);

}
