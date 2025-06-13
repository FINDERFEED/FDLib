package com.finderfeed.fdlib.systems.bedrock.models.model_render_info;

import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.model_system.LayerAttachments;
import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.model_system.attachments.ModelAttachment;
import com.finderfeed.fdlib.systems.bedrock.models.FDModel;

import java.util.Collection;

public class AttachmentAdditionalRenderInfo implements IFDModelAdditionalInfo {

    private LayerAttachments layerAttachments;

    public AttachmentAdditionalRenderInfo(LayerAttachments layerAttachments){
        this.layerAttachments = layerAttachments;
    }

    @Override
    public Collection<? extends ModelAttachment<?,?>> getBoneModelAttachments(String bone) {
        var models = layerAttachments.getAllBoneAttachments(bone);
        return models.stream().map(v->v.second).toList();
    }

}
