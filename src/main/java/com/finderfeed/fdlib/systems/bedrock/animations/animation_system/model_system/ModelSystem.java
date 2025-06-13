package com.finderfeed.fdlib.systems.bedrock.animations.animation_system.model_system;

import com.finderfeed.fdlib.data_structures.Pair;
import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.AnimationSystem;
import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.model_system.attachments.ModelAttachment;
import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.model_system.attachments.ModelAttachmentData;
import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.model_system.attachments.ModelAttachmentType;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;

import java.util.*;

public abstract class ModelSystem {

    private AnimationSystem animationSystem;

    //Here lies HashMap<Integer, HashMap<String, List<Pair<UUID, FDModel>>>>, RIP
    private HashMap<Integer, LayerAttachments> layerBoneModelMap = new HashMap<>();

    public ModelSystem(AnimationSystem animationSystem){
        this.animationSystem = animationSystem;
    }

    public AnimationSystem getAnimationSystem() {
        return animationSystem;
    }

    public void tick(){
        this.animationSystem.tick();
    }


    public void attachToLayer(int layer, String boneName, UUID attachmentUUID, ModelAttachmentData<?> attachmentData){
        if (!this.hasAttachment(attachmentUUID)) {
            var layerAttachments = this.getLayerAttachments(layer);

            ModelAttachmentType attachmentType = attachmentData.type();
            var modelAttachment = attachmentType.attachmentFromData(attachmentData);

            layerAttachments.attach(boneName, attachmentUUID, modelAttachment);
            this.onAttachment(layer, boneName, attachmentUUID, attachmentData);
        }
    }

    public void removeAttachment(UUID attachmentUUID){
        for (var entry : this.layerBoneModelMap.entrySet()){

            var layerAttachments = entry.getValue();
            if (layerAttachments.removeAttachment(attachmentUUID)){
                this.onAttachmentRemoved(attachmentUUID);
                return;
            }
        }
    }

    public List<Pair<UUID, ModelAttachment<?,?>>> getAllLayerAttachments(int layerId){
        LayerAttachments layerAttachments = this.getLayerAttachments(layerId);
        return layerAttachments.getAllAttachments();
    }

    public Collection<Integer> allLayers(){
        return this.layerBoneModelMap.keySet();
    }

    public boolean hasAttachment(UUID uuid){
        for (var entry : this.layerBoneModelMap.entrySet()){
            var layerAttachments = entry.getValue();
            if (layerAttachments.hasAttachment(uuid)){
                return true;
            }
        }
        return false;
    }


    public LayerAttachments getLayerAttachments(int layerId){
        return this.layerBoneModelMap.computeIfAbsent(layerId,v->new LayerAttachments());
    }

    public void saveAttachments(HolderLookup.Provider provider, CompoundTag compoundTag){

        CompoundTag modelSystemData = new CompoundTag();

        int id = 0;
        for (var entry : layerBoneModelMap.entrySet()){

            CompoundTag layerAttachmentsData = new CompoundTag();
            int layerId = entry.getKey();
            LayerAttachments layerAttachments = entry.getValue();
            CompoundTag clayer = layerAttachments.serializeNBT(provider);

            layerAttachmentsData.putInt("layerId",layerId);
            layerAttachmentsData.put("layerAttachments",clayer);

            modelSystemData.put("attachments_"+id, layerAttachmentsData);
            id++;
        }

        compoundTag.put("modelAttachmentsData", modelSystemData);
    }

    public void loadAttachments(HolderLookup.Provider provider, CompoundTag tag){
        this.layerBoneModelMap = new HashMap<>();

        int id = 0;

        CompoundTag attachmentsData = tag.getCompound("modelAttachmentsData");
        while (attachmentsData.contains("attachments_" + id)){
            CompoundTag attachments = attachmentsData.getCompound("attachments_" + id);

            int layerId = attachments.getInt("layerId");
            LayerAttachments layerAttachments = new LayerAttachments();
            layerAttachments.deserializeNBT(provider, attachments.getCompound("layerAttachments"));

            this.layerBoneModelMap.put(layerId, layerAttachments);

            id++;
        }
    }

    public abstract void onAttachment(int layer, String bone, UUID modelUUID, ModelAttachmentData<?> attachedModel);

    public abstract void onAttachmentRemoved(UUID modelUUID);

}
