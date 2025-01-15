package com.finderfeed.fdlib.systems.bedrock.animations.keyframe_processors;

import com.finderfeed.fdlib.data_structures.IntRangedList;
import com.finderfeed.fdlib.systems.bedrock.animations.AnimationContext;
import com.finderfeed.fdlib.systems.bedrock.animations.KeyFrame;
import com.finderfeed.fdlib.systems.bedrock.models.FDModelPart;
import org.joml.Vector3f;

import java.util.List;

public abstract class KeyFrameProcessor {

    private IntRangedList<KeyFrame> keyFrames;

    public KeyFrameProcessor(List<KeyFrame> keyFrames,boolean sorted){
        if (keyFrames != null && !keyFrames.isEmpty()) {
            this.keyFrames = new IntRangedList<>(keyFrames, sorted, keyFrame -> keyFrame.time);
        }
    }

    public abstract void applyTransformations(AnimationContext context, FDModelPart modelPart, float time);

    public abstract Vector3f getCurrentTransformation(AnimationContext context, float time);

    public IntRangedList<KeyFrame> getKeyFrames() {
        return keyFrames;
    }

    public boolean isActive(){
        return keyFrames != null;
    }
}
