package com.finderfeed.fdlib.systems.screen.screen_effect.instances;

import com.finderfeed.fdlib.systems.screen.screen_effect.ScreenEffect;
import com.finderfeed.fdlib.systems.screen.screen_effect.instances.datas.ScreenColorData;
import com.finderfeed.fdlib.util.rendering.FDRenderUtil;
import net.minecraft.client.gui.GuiGraphics;

public class ScreenColorEffect extends ScreenEffect<ScreenColorData> {

    public ScreenColorEffect(ScreenColorData data, int inTime, int stayTime, int outTime) {
        super(data, inTime, stayTime, outTime);
    }

    @Override
    public void render(GuiGraphics graphics, float pticks, int currentTick, float screenWidth, float screenHeight) {
        ScreenColorData colorData = this.getData();
        float p = 0;
        if (this.isInTime(currentTick)){
            p = this.getInTimePercent(currentTick,pticks);
        }else if (this.isStayTime(currentTick)){
            p = 1;
        }else if (this.isOutTime(currentTick)){
            p = 1 - this.getOutTimePercent(currentTick,pticks);
        }
        FDRenderUtil.fill(graphics.pose(),0,0,screenWidth,screenHeight,colorData.r,colorData.g,colorData.b,colorData.a * p);
    }
}
