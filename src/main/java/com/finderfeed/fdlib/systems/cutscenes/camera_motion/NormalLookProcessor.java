package com.finderfeed.fdlib.systems.cutscenes.camera_motion;

import com.finderfeed.fdlib.FDLibCalls;
import com.finderfeed.fdlib.data_structures.ObjectHolder;
import com.finderfeed.fdlib.systems.cutscenes.*;
import com.finderfeed.fdlib.util.math.FDMathUtil;

import java.lang.ref.Reference;
import java.util.List;

public class NormalLookProcessor extends CameraLookProcessor {

    @Override
    public void rotate(CutsceneData cutsceneData, int currentTime, float partialTick, ObjectHolder<Float> yaw, ObjectHolder<Float> pitch) {

        List<CameraPos> positions = cutsceneData.getCameraPositions();

        EasingType easingType = cutsceneData.getLookEasing();

        float p = CutsceneUtil.getPercent(cutsceneData,currentTime,partialTick);

        float globalPercent = p * (positions.size() - 1);

        int index = (int) globalPercent;

        float localPercent = easingType.apply(globalPercent - index);

        CameraPos current = FDLibCalls.getListValueOrBoundaries(index,positions);
        CameraPos next = FDLibCalls.getListValueOrBoundaries(index + 1,positions);

        float currentYaw = current.getYaw();
        float nextYaw = next.getYaw();

        float currentPitch = current.getPitch();
        float nextPitch = next.getPitch();

        float pitchValue = FDMathUtil.lerp(currentPitch,nextPitch,localPercent);
        float yawValue = FDMathUtil.lerpAround(currentYaw,nextYaw,-180,180,localPercent);

        pitch.setValue(pitchValue);
        yaw.setValue(yawValue);
    }

}
