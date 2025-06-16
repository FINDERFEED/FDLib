package com.finderfeed.fdlib.systems.bedrock.animations.animation_system;

import com.finderfeed.fdlib.systems.bedrock.models.FDModelPart;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;

@FunctionalInterface
public interface BoneTransformationController<T extends AnimatedObject> {

    /**
     * Model part has animation transformation
     */
    void transformBone(T object, FDModelPart modelPart, PoseStack matrices, MultiBufferSource src, int light, int overlay, float pticks);

}
