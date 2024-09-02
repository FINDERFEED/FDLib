package com.finderfeed.fdlib.systems.bedrock.animations;

import com.finderfeed.fdlib.data_structures.IntRangedList;
import com.finderfeed.fdlib.util.math.FDMathUtil;
import net.minecraft.util.Mth;
import org.joml.Vector3f;

import java.util.List;

public class AnimationUtil {

    public static Vector3f catmullRomThroughKeyFrames(AnimationContext context, IntRangedList<KeyFrame> keyFrames, float time, float partialTick){

        List<KeyFrame> frames = keyFrames.getValues((int)Math.floor(time),1,2);
        KeyFrame current = frames.get(1);
        KeyFrame next = frames.get(2);
        if (next == null){
            return current.getPostValue(context);
        }
        float localTime = time - current.time;
        float timeBetween = next.time - current.time;
        float p = Mth.clamp((localTime + partialTick) / timeBetween,0,1);
        KeyFrame f1 = frames.get(0);
        KeyFrame f4 = frames.get(3);
        return FDMathUtil.catmullrom(
                f1 != null ? f1.getPostValue(context) : null,
                current.getPostValue(context),
                next.getPostValue(context),
                f4 != null ? f4.getPostValue(context) : null,
                p
        );
    }

}
