package com.finderfeed.fdlib.systems.bedrock.animations;

import com.finderfeed.fdlib.shunting_yard.RPNVector3f;
import com.finderfeed.fdlib.systems.bedrock.animations.keyframe_processors.KFPositionProcessor;
import com.finderfeed.fdlib.systems.bedrock.animations.keyframe_processors.KFRotationProcessor;
import com.finderfeed.fdlib.systems.bedrock.animations.keyframe_processors.KFScaleProcessor;
import com.finderfeed.fdlib.systems.bedrock.models.FDModel;
import com.finderfeed.fdlib.systems.bedrock.models.FDModelPart;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class BoneAnimationData {

    private String boneName;
    private KFPositionProcessor position;
    private KFRotationProcessor rotation;
    private KFScaleProcessor scale;

    public BoneAnimationData(String boneName,List<KeyFrame> positions,List<KeyFrame> rotations,List<KeyFrame> scales,boolean sorted){
        this.boneName = boneName;
        this.position = new KFPositionProcessor(positions,sorted);
        this.rotation = new KFRotationProcessor(rotations,sorted);
        this.scale = new KFScaleProcessor(scales,sorted);
    }

    public void apply(FDModel model,AnimationContext context,float time){
        FDModelPart part = model.getModelPart(boneName);
        if (part == null) return;
        if (position.isActive()){
            position.applyTransformations(context,part,time);
        }
        if (rotation.isActive()){
            rotation.applyTransformations(context,part,time);
        }
        if (scale.isActive()){
            scale.applyTransformations(context,part,time);
        }
    }

    public BoneAnimationData createTransitionData(Animation next, BoneAnimationData transitionTo, AnimationContext currentContext,int toNullTime,float elapsedTime, boolean transitionToReversed){
        List<KeyFrame> positionFrames = this.createPositionTransitionKeyFrames(next, transitionTo,currentContext,toNullTime,elapsedTime,transitionToReversed);
        List<KeyFrame> rotationFrames = this.createRotationTransitionKeyFrames(next, transitionTo,currentContext,toNullTime,elapsedTime,transitionToReversed);
        List<KeyFrame> scaleFrames = this.createScaleTransitionKeyFrames(next, transitionTo,currentContext,toNullTime,elapsedTime,transitionToReversed);
        return new BoneAnimationData(this.boneName,positionFrames,rotationFrames,scaleFrames,true);
    }

    private List<KeyFrame> createPositionTransitionKeyFrames(Animation next, BoneAnimationData transitionTo,AnimationContext currentContext,int toNullTime,float elapsedTime, boolean transitionToReversed){
        if (this.position.isActive()){
            Vector3f current = this.position.getCurrentTransformation(currentContext,elapsedTime);
            RPNVector3f v = new RPNVector3f(current.x,current.y,current.z);
            if (transitionTo != null && transitionTo.position.isActive()){
                List<KeyFrame> frames = transitionTo.position.getKeyFrames().getAllValuesAfter(0);
                if (!transitionToReversed) {
                    KeyFrame first = frames.getFirst();
                    if (first.time != 0) {
                        frames.addFirst(new KeyFrame(v, 0, first.interpolationMode));
                    } else {
                        frames.set(0, new KeyFrame(v, 0, first.interpolationMode));
                    }
                    return frames;
                }else{
                    KeyFrame last = frames.getLast();
                    if (last.time != next.getAnimTime()) {
                        frames.addLast(new KeyFrame(v, next.getAnimTime(), last.interpolationMode));
                    } else {
                        frames.set(frames.size() - 1, new KeyFrame(v, next.getAnimTime(), last.interpolationMode));
                    }
                    return frames;
                }
            }else{
                return new ArrayList<>(List.of(
                        new KeyFrame(v,0,InterpolationMode.LINEAR),
                        new KeyFrame(new RPNVector3f(0,0,0),toNullTime,InterpolationMode.LINEAR)
                ));
            }
        }else{
            return null;
        }
    }

    private List<KeyFrame> createRotationTransitionKeyFrames(Animation next, BoneAnimationData transitionTo,AnimationContext currentContext,int toNullTime,float elapsedTime, boolean transitionToReversed){
        if (this.rotation.isActive()){
            Vector3f current = this.rotation.getCurrentTransformation(currentContext,elapsedTime);
            RPNVector3f v = new RPNVector3f(current.x,current.y,current.z);
            if (transitionTo != null && transitionTo.rotation.isActive()){
                List<KeyFrame> frames = transitionTo.rotation.getKeyFrames().getAllValuesAfter(0);
                if (!transitionToReversed) {
                    KeyFrame first = frames.getFirst();
                    if (first.time != 0) {
                        frames.addFirst(new KeyFrame(v, 0, first.interpolationMode));
                    } else {
                        frames.set(0, new KeyFrame(v, 0, first.interpolationMode));
                    }
                    return frames;
                }else{
                    KeyFrame last = frames.getLast();
                    if (last.time != next.getAnimTime()) {
                        frames.addLast(new KeyFrame(v, next.getAnimTime(), last.interpolationMode));
                    } else {
                        frames.set(frames.size() - 1, new KeyFrame(v, next.getAnimTime(), last.interpolationMode));
                    }
                    return frames;
                }
            }else{
                return new ArrayList<>(List.of(
                        new KeyFrame(v,0,InterpolationMode.LINEAR),
                        new KeyFrame(new RPNVector3f(0,0,0),toNullTime,InterpolationMode.LINEAR)
                ));
            }
        }else{
            return null;
        }
    }

    private List<KeyFrame> createScaleTransitionKeyFrames(Animation next, BoneAnimationData transitionTo,AnimationContext currentContext,int toNullTime,float elapsedTime, boolean transitionToReversed){
        if (this.scale.isActive()){
            Vector3f current = this.scale.getCurrentTransformation(currentContext,elapsedTime);
            RPNVector3f v = new RPNVector3f(current.x,current.y,current.z);
            if (transitionTo != null && transitionTo.scale.isActive()){
                List<KeyFrame> frames = transitionTo.scale.getKeyFrames().getAllValuesAfter(0);
                if (!transitionToReversed) {
                    KeyFrame first = frames.getFirst();
                    if (first.time != 0) {
                        frames.addFirst(new KeyFrame(v, 0, first.interpolationMode));
                    } else {
                        frames.set(0, new KeyFrame(v, 0, first.interpolationMode));
                    }
                    return frames;
                }else{
                    KeyFrame last = frames.getLast();
                    if (last.time != next.getAnimTime()) {
                        frames.addLast(new KeyFrame(v, next.getAnimTime(), last.interpolationMode));
                    } else {
                        frames.set(frames.size() - 1, new KeyFrame(v, next.getAnimTime(), last.interpolationMode));
                    }
                    return frames;
                }
            }else{
                return new ArrayList<>(List.of(
                        new KeyFrame(v,0,InterpolationMode.LINEAR),
                        new KeyFrame(new RPNVector3f(1,1,1),toNullTime,InterpolationMode.LINEAR)
                ));
            }
        }else{
            return null;
        }
    }

    public String getBoneName() {
        return boneName;
    }

}
