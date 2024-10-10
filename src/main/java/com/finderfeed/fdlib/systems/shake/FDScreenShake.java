package com.finderfeed.fdlib.systems.shake;

import com.finderfeed.fdlib.util.math.ComplexEasingFunction;
import com.finderfeed.fdlib.util.rendering.FDEasings;
import com.mojang.blaze3d.vertex.PoseStack;

public abstract class FDScreenShake implements ScreenShake {

    private FDShakeData data;

    public FDScreenShake(FDShakeData data) {
        this.data = data;
    }

    @Override
    public boolean hasEnded(int elapsedTime) {
        return elapsedTime > data.duration();
    }

    public FDShakeData getData() {
        return data;
    }
}
