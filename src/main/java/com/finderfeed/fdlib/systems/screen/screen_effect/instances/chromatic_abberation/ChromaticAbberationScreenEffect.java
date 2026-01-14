package com.finderfeed.fdlib.systems.screen.screen_effect.instances.chromatic_abberation;

import com.finderfeed.fdlib.systems.chromatic_abberation_effect.ChromaticAbberationEffect;
import com.finderfeed.fdlib.systems.chromatic_abberation_effect.ChromaticAbberationHandler;
import com.finderfeed.fdlib.systems.screen.screen_effect.ScreenEffect;
import net.minecraft.client.gui.GuiGraphics;

public class ChromaticAbberationScreenEffect extends ScreenEffect<ChromaticAbberationData> {

    public ChromaticAbberationScreenEffect(ChromaticAbberationData data, int inTime, int stayTime, int outTime) {
        super(data, inTime, stayTime, outTime);
    }

    @Override
    public void render(GuiGraphics graphics, float pticks, int currentTick, float screenWidth, float screenHeight) {
        if (currentTick == 1){
            ChromaticAbberationHandler.setEffect(new ChromaticAbberationEffect(this.getInTime(),this.getStayTime(),this.getOutTime(), this.getData().getStrength()));
        }
    }

}
