package com.finderfeed.fdlib.systems.cutscenes.camera_motion;

import com.finderfeed.fdlib.FDLibCalls;
import com.finderfeed.fdlib.systems.cutscenes.CameraPos;
import com.finderfeed.fdlib.systems.cutscenes.CutsceneData;
import com.finderfeed.fdlib.systems.cutscenes.CutsceneUtil;
import com.finderfeed.fdlib.systems.cutscenes.EasingType;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class LinearCameraMotion extends CameraMotion {

    @Override
    public Vec3 calculateCameraPosition(CutsceneData data, int currentTime, float partialTick) {

        List<CameraPos> positions = data.getCameraPositions();

        if (positions.isEmpty()){
            throw new RuntimeException("List of camera positions cannot be empty!");
        }

        EasingType easingType = data.getTimeEasing();

        float p = easingType.apply(CutsceneUtil.getPercent(data,currentTime,partialTick));

        float globalPercent = p * (positions.size() - 1);

        int index = (int) globalPercent;

        float localPercent = globalPercent - index;

        CameraPos current = FDLibCalls.getListValueOrBoundaries(index,positions);
        CameraPos next = FDLibCalls.getListValueOrBoundaries(index + 1,positions);

        return current.interpolate(next,localPercent);
    }

}
