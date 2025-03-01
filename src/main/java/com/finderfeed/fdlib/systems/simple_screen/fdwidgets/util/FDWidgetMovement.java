package com.finderfeed.fdlib.systems.simple_screen.fdwidgets.util;

import com.finderfeed.fdlib.systems.simple_screen.FDWidget;
import com.finderfeed.fdlib.util.InterpolatedValue;
import com.finderfeed.fdlib.util.math.FDMathUtil;
import com.finderfeed.fdlib.util.rendering.FDRenderUtil;
import net.minecraft.client.Minecraft;

import java.util.function.Function;

public class FDWidgetMovement {

    private InterpolatedValue moveTaskX;
    private InterpolatedValue moveTaskY;

    private FDWidget widget;

    public FDWidgetMovement(FDWidget widget){
        this.widget = widget;
    }

    public void moveTo(int time, float x, float y, Function<Float,Float> easing){
        this.moveTaskX = new InterpolatedValue(widget.getX(),x,time,easing);
        this.moveTaskY = new InterpolatedValue(widget.getY(),y,time,easing);
    }

    public void setWidgetPositionInRender(){
        if (moveTaskX != null && moveTaskY != null) {
            float pticks = FDRenderUtil.tryGetPartialTickIgnorePause();
            float x = moveTaskX.getValue(pticks);
            float y = moveTaskY.getValue(pticks);
            widget.setX(x);
            widget.setY(y);
        }
    }

    public void tick(){
        if (this.moveTaskX != null && this.moveTaskY != null){
            if (this.moveTaskX.hasFinished() && this.moveTaskY.hasFinished()){
                float x = moveTaskX.getEnd();
                float y = moveTaskY.getEnd();
                widget.setX(x);
                widget.setY(y);
                this.moveTaskX = null;
                this.moveTaskY = null;
                return;
            }
            this.moveTaskX.tick();
            this.moveTaskY.tick();
        }
    }

    public boolean isMoving(){
        return this.moveTaskX != null || this.moveTaskY != null;
    }
}
