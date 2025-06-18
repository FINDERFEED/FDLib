package com.finderfeed.fdlib.systems.bedrock.animations;

import com.finderfeed.fdlib.data_structures.IntRangedList;
import com.finderfeed.fdlib.util.math.FDMathUtil;
import net.minecraft.util.Mth;
import org.joml.Vector3f;

import java.util.List;

public class AnimationUtil {

    public static Vector3f catmullRomThroughKeyFrames(AnimationContext context, IntRangedList<KeyFrame> keyFrames, float time, float partialTick){

        Animation.LoopMode loopMode = context.getCurrentLoopMode();

        if (loopMode != Animation.LoopMode.LOOP || keyFrames.size() <= 2) {

            List<KeyFrame> frames = keyFrames.getValues((int) Math.floor(time), 1, 2);
            KeyFrame current = frames.get(1);
            KeyFrame next = frames.get(2);
            if (next == null) {
                return current.getPostValue(context);
            }
            float localTime = time - current.time;
            float timeBetween = next.time - current.time;
            float p = Mth.clamp((localTime) / timeBetween, 0, 1);
            KeyFrame f1 = frames.get(0);
            KeyFrame f4 = frames.get(3);
            return FDMathUtil.catmullrom(
                    f1 != null ? f1.getPostValue(context) : null,
                    current.getPostValue(context),
                    next.getPostValue(context),
                    f4 != null ? f4.getPostValue(context) : null,
                    p
            );

        }else{

            int animTime = context.getAnimation().getAnimTime();

            List<KeyFrame> frames = keyFrames.getValues((int) Math.floor(time), 1, 2);
            KeyFrame previous = frames.get(0);
            KeyFrame current = frames.get(1);
            KeyFrame next = frames.get(2);
            KeyFrame next2 = frames.get(3);

            //0 - regular, -1 - previous is set to last, 1 - next2 is set to second keyframe, next is non null, 2 - next is set to second keyframe, next2 is set to third
            int state = 0;

            if (next2 == null){
                if (next != null) {
                    state = 1;
                    next2 = keyFrames.get(1);
                }else{
                    state = 2;
                    next2 = keyFrames.get(2);
                }
            }
            if (next == null){
                next = keyFrames.get(1);
            }
            if (previous == null){
                state = -1;
                KeyFrame last = keyFrames.getLast();
                if (last.time == animTime){
                    previous = keyFrames.get(keyFrames.size() - 2);
                }else{
                    previous = last;
                }
            }

            float p;

            switch (state){
                case 2 -> { //is only possible if time equals animation time
                    float localTime = 0;
                    float timeBetween = next.time;
                    p = Mth.clamp((localTime) / timeBetween, 0, 1);
                }
                default -> {
                    float localTime = time - current.time;
                    float timeBetween = next.time - current.time;
                    p = Mth.clamp((localTime) / timeBetween, 0, 1);
                }
            }

            return FDMathUtil.catmullrom(
                    previous.getPostValue(context),
                    current.getPostValue(context),
                    next.getPostValue(context),
                    next2.getPostValue(context),
                    p
            );
        }
    }

}
