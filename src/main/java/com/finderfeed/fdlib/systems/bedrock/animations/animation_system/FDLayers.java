package com.finderfeed.fdlib.systems.bedrock.animations.animation_system;

import com.finderfeed.fdlib.systems.bedrock.models.FDModel;
import com.finderfeed.fdlib.systems.bedrock.models.FDModelInfo;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class FDLayers {

    private HashMap<String,FDModelInfo> layerInfo = new HashMap<>();


    public FDLayers addLayer(String layerName,FDModelInfo layer){
        this.layerInfo.put(layerName,layer);
        return this;
    }

    
    public Map<String,FDModel> convert(){
        return layerInfo.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, entry->new FDModel(entry.getValue())));
    }

}
