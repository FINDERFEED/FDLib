package com.finderfeed.fdlib.systems.shake;

import com.mojang.blaze3d.vertex.PoseStack;

public interface ScreenShake {

    void process(PoseStack projection,int time, float partialTicks);

    boolean hasEnded(int elapsedTime);

}
