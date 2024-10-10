package com.finderfeed.fdlib.systems.shake;

import com.finderfeed.fdlib.util.math.ComplexEasingFunction;
import com.finderfeed.fdlib.util.math.FDMathUtil;
import com.finderfeed.fdlib.util.rendering.FDEasings;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.joml.AxisAngle4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class PositionedScreenShake extends FDScreenShake{

    private Vec3 pos;
    private ComplexEasingFunction easingFunction;

    public PositionedScreenShake(FDShakeData data, Vec3 pos) {
        super(data);
        this.pos = pos;
        this.easingFunction = ComplexEasingFunction.builder()
                .addArea(data.getInTime(), FDEasings::easeIn)
                .addArea(data.getStayTime(), FDEasings::one)
                .addArea(data.getOutTime(), FDEasings::reversedEaseOut)
                .build();
    }

    @Override
    public void process(PoseStack projection,int time, float partialTicks) {

        Camera camera = Minecraft.getInstance().gameRenderer.getMainCamera();
        Vec3 cameraPos = camera.getPosition();
        Vec3 look = Minecraft.getInstance().player.getLookAngle();
        Vec3 left = new Vec3(0,1,0).cross(look);
        Vec3 b = pos.subtract(cameraPos);
        Vec3 proj = FDMathUtil.projectVectorOntoPlane(b,look);
        Vec3 up = look.cross(left);
        double angle = FDMathUtil.angleBetweenVectors(up,proj);
        if (Double.isNaN(angle)){
            angle = 0;
        }

        int duration = this.getData().duration();
        float t = Mth.clamp(time + partialTicks,0,duration);

        float p = t / duration;
        float s = p * FDMathUtil.FPI * 2 * this.getData().getFrequency();

        if (proj.dot(left) < 0){
            angle = -angle;
        }

        Vector3f axis = new Vector3f(0,1,0).rotateZ((float)(angle + FDMathUtil.FPI / 2));

        float amplitude = this.getData().getAmplitude();
        float strength = easingFunction.apply(t);
        projection.mulPose(new Quaternionf(new AxisAngle4f((float) Math.toRadians(Math.sin(s + FDMathUtil.FPI) * amplitude * strength),axis.x,axis.y,axis.z)));


    }

    public Vec3 getPos() {
        return pos;
    }

    public void setPos(Vec3 pos) {
        this.pos = pos;
    }
}
