package com.finderfeed.fdlib.systems.bedrock.animations.animation_system.model_system.entity_model_system;

import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.AnimatedObject;
import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.model_system.attachments.ModelAttachmentData;
import com.finderfeed.fdlib.systems.bedrock.models.FDModelInfo;
import net.minecraft.world.entity.Entity;

import java.util.UUID;

public class ClientEntityModelSystem<T extends Entity & AnimatedObject> extends EntityModelSystem<T>{

    protected ClientEntityModelSystem(T entity) {
        super(entity);
    }

    @Override
    public void onAttachment(int layer, String bone, UUID modelUUID, ModelAttachmentData<?> attachedModel) {

    }

    @Override
    public void onAttachmentRemoved(UUID modelUUID) {

    }
}
