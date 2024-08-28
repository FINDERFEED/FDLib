package com.finderfeed.fdlib.systems.bedrock.animations.keyframe_processors;

import com.finderfeed.fdlib.systems.bedrock.animations.AnimationContext;
import com.finderfeed.fdlib.systems.bedrock.animations.AnimationUtil;
import com.finderfeed.fdlib.systems.bedrock.animations.InterpolationMode;
import com.finderfeed.fdlib.systems.bedrock.animations.KeyFrame;
import com.finderfeed.fdlib.systems.bedrock.models.FDModelPart;
import com.finderfeed.fdlib.util.math.FDMathUtil;
import net.minecraft.util.Mth;
import org.joml.Vector3f;

import java.util.List;

public class KFScaleProcessor extends KeyFrameProcessor{

    public KFScaleProcessor(List<KeyFrame> keyFrames,boolean sorted) {
        super(keyFrames,sorted);
    }


    @Override
    public void applyTransformations(AnimationContext context, FDModelPart model, float time, float partialTick) {
        Vector3f v = this.getCurrentTransformation(context,time,partialTick);
        model.xScale *= v.x;
        model.yScale *= v.y;
        model.zScale *= v.z;
    }

    @Override
    public Vector3f getCurrentTransformation(AnimationContext context, float time, float partialTick) {
        List<KeyFrame> currentAndNext = this.getKeyFrames().getValues((int)Math.floor(time),0,1);
        KeyFrame current = currentAndNext.get(0);
        KeyFrame next = currentAndNext.get(1);
        if (current.interpolationMode == InterpolationMode.LINEAR && (next == null || next.interpolationMode == InterpolationMode.LINEAR)){
            Vector3f v1 = current.getValue(context);
            if (next != null) {
                Vector3f v2 = current.getValue(context);
                float p = ((time + partialTick) - current.time) / (float) (next.time - current.time); p = Mth.clamp(p,0,1);
                Vector3f i = FDMathUtil.interpolateVectors(v1, v2, p);

                return i;
            }else{
                return v1;
            }
        }else{
            Vector3f v = AnimationUtil.catmullRomThroughKeyFrames(context,this.getKeyFrames(),time,partialTick);
            return v;
        }
    }
}
