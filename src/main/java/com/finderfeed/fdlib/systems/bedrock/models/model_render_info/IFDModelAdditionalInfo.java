package com.finderfeed.fdlib.systems.bedrock.models.model_render_info;

import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.model_system.LayerAttachments;
import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.model_system.attachments.ModelAttachment;
import com.finderfeed.fdlib.systems.bedrock.models.FDModel;

import java.util.Collection;
import java.util.List;

public interface IFDModelAdditionalInfo {

    Collection<? extends ModelAttachment<?,?>> getBoneModelAttachments(String bone);

    public static IFDModelAdditionalInfo empty(){
        return new EmtpyModelAdditionalInfo();
    }

    public static IFDModelAdditionalInfo attachmentData(LayerAttachments layerAttachments){
        return new AttachmentAdditionalRenderInfo(layerAttachments);
    }

}
