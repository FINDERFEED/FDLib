package com.finderfeed.fdlib.systems.bedrock.animations.animation_system.entity.head;

import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.AnimatedObject;
import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.AnimationSystem;
import com.finderfeed.fdlib.systems.bedrock.models.FDModel;
import com.finderfeed.fdlib.systems.bedrock.models.FDModelPart;
import com.finderfeed.fdlib.util.math.FDMathUtil;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import org.joml.*;

import java.lang.Math;

public class HeadController<T extends Mob & IHasHead<T> & AnimatedObject> {

    private HeadControllerContainer<T> container;
    private FDModel fdModel;
    private T entity;
    private String headBone;

    private float headTransitionSpeed;

    private float currentHeadRotationX = 0;
    private float currentHeadRotationY = 0;
    private float oldHeadRotationX = 0;
    private float oldHeadRotationY = 0;



    public HeadController(HeadControllerContainer<T> container, FDModel model, String headBone, T entity){
        this.entity = entity;
        this.headBone = headBone;
        this.fdModel =  model;
        this.headTransitionSpeed = entity.getHeadRotSpeed();
        this.container = container;
    }

    public void clientTick(){


        this.oldHeadRotationX = this.currentHeadRotationX;
        this.oldHeadRotationY = this.currentHeadRotationY;

        Vector2f angles = this.getLookAtTargetAngles();

        float maxXRot = entity.getMaxHeadXRot();
        float maxYRot = entity.getMaxHeadYRot();

        float newY = this.getRotation(currentHeadRotationY, Mth.clamp(FDMathUtil.convertMCYRotationToNormal(angles.y), -maxYRot,maxYRot));
        float newX = this.getRotation(currentHeadRotationX, Mth.clamp(FDMathUtil.convertMCYRotationToNormal(angles.x), -maxXRot,maxXRot));

        this.currentHeadRotationY = newY;
        this.currentHeadRotationX = newX;

    }

    public void onControllerModeChanged(HeadControllerContainer.Mode oldMode, HeadControllerContainer.Mode newMode){
        if (newMode == HeadControllerContainer.Mode.LOOK){

            this.entity.getAnimationSystem().applyAnimations(this.fdModel, 0);
            FDModelPart part = this.fdModel.getModelPart(headBone);

            this.currentHeadRotationX = FDMathUtil.convertMCYRotationToNormal(this.currentHeadRotationX + part.getXRot());
            this.currentHeadRotationY = FDMathUtil.convertMCYRotationToNormal(this.currentHeadRotationY + part.getYRot());
            this.oldHeadRotationX = this.currentHeadRotationX;
            this.oldHeadRotationY = this.currentHeadRotationY;

        }
    }


    private Vector2f getLookAtTargetAngles(){

        if (container.getControllersMode() == HeadControllerContainer.Mode.ANIMATION){
            return new Vector2f();
        }

        AnimationSystem system = entity.getAnimationSystem();
        fdModel.resetTransformations();
        system.applyAnimations(fdModel, 0);
        float yRot = entity.getYRot();
        fdModel.main.addYRot(-yRot);
        FDModelPart head = fdModel.getModelPart(headBone);
        head.setXRot(head.initRotation.x);
        head.setYRot(head.initRotation.y);
        head.setZRot(head.initRotation.z);

        Matrix4f transform = fdModel.getModelPartTransformation(head);

        Vector3f position = transform.transformPosition(new Vector3f());
        Vector3f wposition = position.add(
                (float) entity.getX(),
                (float) entity.getY(),
                (float) entity.getZ(),
                new Vector3f()
        );

        Vector3d playerPosition = new Vector3d(this.container.getWantedX(),this.container.getWantedY(),this.container.getWantedZ());

        Vector3d between = playerPosition.sub(wposition,new Vector3d());

        Vector3d axisX = this.floatToDouble(transform.transformDirection(new Vector3f(1,0,0))).normalize();
        Vector3d axisY = this.floatToDouble(transform.transformDirection(new Vector3f(0,1,0))).normalize();
        Vector3d axisZ = this.floatToDouble(transform.transformDirection(new Vector3f(0,0,1))).normalize();


        Vector3d vec = this.fromOtherBasisToEuclidian(axisX,axisY,axisZ,between);

        float verticalAngle = 180 + (float)Math.toDegrees(Math.atan2(vec.x,vec.z));
        double a = Math.sqrt(vec.z * vec.z + vec.x * vec.x);
        float horizontalAngle =  (float) Math.toDegrees(Math.atan2(vec.y,a));

        return new Vector2f(horizontalAngle,verticalAngle);
    }



    public float getHeadTransitionSpeed() {
        return headTransitionSpeed;
    }

    public T getEntity() {
        return entity;
    }

    private float getRotation(float currentRotation, float targetY){
        if (currentRotation >= 0 && targetY >= 0 || currentRotation < 0 && targetY < 0){
            int direction = targetY - currentRotation > 0 ? 1 : -1;
            if (currentRotation >= 0) {
                if (direction == 1){
                    return Mth.clamp(currentRotation + this.getHeadTransitionSpeed() * direction, -Integer.MAX_VALUE,targetY);
                }else{
                    return Mth.clamp(currentRotation + this.getHeadTransitionSpeed() * direction, targetY, Integer.MAX_VALUE);
                }
            }else{
                if (direction == 1){
                    return Mth.clamp(currentRotation + this.getHeadTransitionSpeed() * direction, -Integer.MAX_VALUE,targetY);
                }else{
                    return Mth.clamp(currentRotation + this.getHeadTransitionSpeed() * direction, targetY, Integer.MAX_VALUE);
                }
            }
        }else{
            if (currentRotation < 0){
                float dist = -currentRotation + targetY;
                if (dist > 180){
                    currentRotation -= this.getHeadTransitionSpeed();
                    if (currentRotation < -180){
                        currentRotation += 360;
                        return Mth.clamp(currentRotation, targetY, Integer.MAX_VALUE);
                    }
                    return Mth.clamp(currentRotation, -Integer.MAX_VALUE, targetY);
                }else{
                    return Mth.clamp(currentRotation + this.getHeadTransitionSpeed(), -Integer.MAX_VALUE, targetY);
                }
            }else{
                float dist = -targetY + currentRotation;
                if (dist > 180){
                    currentRotation += this.getHeadTransitionSpeed();
                    if (currentRotation > 180){
                        currentRotation -= 360;
                        return Mth.clamp(currentRotation, -Integer.MAX_VALUE, targetY);
                    }
                    return Mth.clamp(currentRotation, targetY, Integer.MAX_VALUE);
                }else{
                    return Mth.clamp(currentRotation - this.getHeadTransitionSpeed(), targetY, Integer.MAX_VALUE);
                }
            }
        }
    }

    public float getCurrentHeadRotationX() {
        return currentHeadRotationX;
    }

    public float getCurrentHeadRotationY() {
        return currentHeadRotationY;
    }

    public Vector2f getCurrentHeadAngles(float pticks){
        return new Vector2f(
                FDMathUtil.lerpAround(oldHeadRotationX,currentHeadRotationX,-180,180,pticks),
                FDMathUtil.lerpAround(oldHeadRotationY,currentHeadRotationY,-180,180,pticks)
        );
    }

    //TO FDMATHHELPER
    private Vector3d floatToDouble(Vector3f v){
        return new Vector3d(
                v.x,v.y,v.z
        );
    }

    private Vector3d fromEuclidianToOtherBasis(
            Vector3d nbx,
            Vector3d nby,
            Vector3d nbz,
            Vector3d point
    ){
        Matrix3d matrix3d = new Matrix3d(
                nbx.x,nbx.y,nbx.z,
                nby.x,nby.y,nby.z,
                nbz.x,nbz.y,nbz.z
        );
        return matrix3d.transform(point, new Vector3d());
    }

    private Vector3d fromOtherBasisToEuclidian(
            Vector3d nbx,
            Vector3d nby,
            Vector3d nbz,
            Vector3d point
    ){
        Matrix3d matrix3d = new Matrix3d(
                nbx.x,nbx.y,nbx.z,
                nby.x,nby.y,nby.z,
                nbz.x,nbz.y,nbz.z
        ).invert();
        return matrix3d.transform(point, new Vector3d());
    }

}
