package com.finderfeed.fdlib.systems.bedrock.models.model_render_info;

import com.finderfeed.fdlib.systems.bedrock.models.FDModel;

import java.util.Collection;
import java.util.List;

public class EmtpyModelAdditionalInfo implements IFDModelAdditionalInfo{

    public static final List<FDModel> EMPTY = List.of();

    @Override
    public Collection<FDModel> getBoneModelAttachments(String bone) {
        return EMPTY;
    }

}
