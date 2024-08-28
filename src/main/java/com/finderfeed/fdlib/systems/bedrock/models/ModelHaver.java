package com.finderfeed.fdlib.systems.bedrock.models;

import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.FDLayers;

import java.util.Map;

public interface ModelHaver {

    FDModel getModel();

    FDModelInfo getModelBase();

    Map<String,FDModel> getLayers();

    FDLayers getLayerInfos();
}
