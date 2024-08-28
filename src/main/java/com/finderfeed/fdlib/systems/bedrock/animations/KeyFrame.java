package com.finderfeed.fdlib.systems.bedrock.animations;

import com.finderfeed.fdlib.shunting_yard.RPNVector3f;
import com.finderfeed.fdlib.shunting_yard.sy_base.ExpressionContext;
import org.joml.Vector3f;

public class KeyFrame {

    public RPNVector3f data;
    public int time;
    public InterpolationMode interpolationMode;

    public KeyFrame(RPNVector3f data,int time,InterpolationMode mode) {
        this.data = data;
        this.time = time;
        this.interpolationMode = mode;
    }

    public Vector3f getValue(ExpressionContext context){
        if (data == null) return null;
        return data.get(context);
    }

}
