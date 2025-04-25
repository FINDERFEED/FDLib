package com.finderfeed.fdlib.systems.shake;

import com.finderfeed.fdlib.util.math.ComplexEasingFunction;
import com.finderfeed.fdlib.util.math.FDMathUtil;
import com.finderfeed.fdlib.util.rendering.FDEasings;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

import java.util.Random;

public class DefaultShake extends FDScreenShake{

    private int oldTime;
    private double xo;
    private double yo;
    private ComplexEasingFunction easingFunction;

    public DefaultShake(FDShakeData data) {
        super(data);
        easingFunction = ComplexEasingFunction.builder()
                .addArea(data.getInTime(), FDEasings::linear)
                .addArea(data.getStayTime(), FDEasings::one)
                .addArea(data.getOutTime(), FDEasings::reversedLinear)
                .build();
    }



    @Override
    public void process(PoseStack projection, int time, float partialTicks) {
        float power = easingFunction.apply(time + partialTicks);
        long t = time + 1;
        Random random = new Random(t * 34324);
        double x = this.randomN(random,power);
        double y = this.randomN(random,power);

        double xd = FDMathUtil.lerp(xo,x,partialTicks);
        double yd = FDMathUtil.lerp(yo,y,partialTicks);

        projection.translate(
                Double.isNaN(xd) ? 0 : xd,
                Double.isNaN(yd) ? 0 : yd,
                0
        );

        if (oldTime != time) {
            oldTime = time;
            t -= 1;
            Random r = new Random(t * 34324);
            xo = randomN(r,power);
            yo = randomN(r,power);
        }

    }

    private double randomN(Random random,float power){
        return (random.nextFloat() * this.getData().getAmplitude() * 2 - this.getData().getAmplitude()) * power;
    }


}
