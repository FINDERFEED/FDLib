package com.finderfeed.fdlib.systems.bedrock.animations.animation_system;

import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.model_system.ModelSystem;
import com.finderfeed.fdlib.systems.bedrock.models.FDModel;
import net.minecraft.world.entity.Entity;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

public interface AnimatedObject {

    ModelSystem getModelSystem();

    default AnimationSystem getAnimationSystem(){
        return this.getModelSystem().getAnimationSystem();
    }

    default void tickModelSystem(){
        this.getModelSystem().tick();
    }

    default Matrix4f getModelPartTransformation(String name, FDModel model){
        return this.getModelPartTransformation(name,model,0);
    }

    default Matrix4f getModelPartTransformation(String name, FDModel model, float pticks){
        var system = this.getAnimationSystem();
        model.resetTransformations();
        system.applyAnimations(model,pticks);
        return model.getModelPartTransformation(name);
    }

    default Vector3f transformPoint(Vector3f initPoint,String name,FDModel model){
        Matrix4f transform = this.getModelPartTransformation(name,model);
        Vector4f v = new Vector4f(initPoint.x,initPoint.y,initPoint.z,1);
        transform.transform(v);
        return new Vector3f(v.x,v.y,v.z);
    }

    default Vector3f getModelPartPosition(String name,FDModel model){
        return this.transformPoint(new Vector3f(0,0,0),name,model);
    }

    default Matrix4f getModelPartTransformation(Entity entity,String name, FDModel model, float pticks){
        var system = this.getAnimationSystem();
        model.resetTransformations();
        system.applyAnimations(model,pticks);
        float yRot = entity.getYRot();
        model.main.addYRot(-yRot);
        return model.getModelPartTransformation(name);
    }

    default Matrix4f getModelPartTransformation(Entity entity,String name, FDModel model){
        return this.getModelPartTransformation(entity,name,model,0);
    }

    default Vector3f transformPoint(Entity entity,Vector3f initPoint,String name,FDModel model){
        Matrix4f transform = this.getModelPartTransformation(entity,name,model);
        Vector4f v = new Vector4f(initPoint.x,initPoint.y,initPoint.z,1);
        transform.transform(v);
        return new Vector3f(v.x,v.y,v.z);
    }

    default Vector3f getModelPartPosition(Entity entity,String name,FDModel model){
        return this.transformPoint(entity,new Vector3f(0,0,0),name,model);
    }

}
