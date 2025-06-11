package com.finderfeed.fdlib.systems.bedrock.animations.animation_system.model_system;

import com.finderfeed.fdlib.data_structures.Pair;
import com.finderfeed.fdlib.systems.bedrock.models.FDModel;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.neoforged.neoforge.common.util.INBTSerializable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class LayerAttachments implements INBTSerializable<CompoundTag> {

    private HashMap<String, BoneAttachments> layerAttachments = new HashMap<>();


    protected void attach(String boneName, UUID uuid, FDModel model){
        var boneAttachments = this.getBoneAttachments(boneName);
        boneAttachments.attach(uuid,model);
    }

    protected boolean removeAttachment(UUID uuid){
        for (var v : this.layerAttachments.values()){
            if (v.removeAttachment(uuid)){
                return true;
            }
        }
        return false;
    }

    public boolean hasAttachment(UUID attachmentUUID){
        for (var boneAttachment : layerAttachments.values()){
            if (boneAttachment.hasAttachment(attachmentUUID)){
                return true;
            }
        }
        return false;
    }

    public BoneAttachments getBoneAttachments(String boneName){
        return this.layerAttachments.computeIfAbsent(boneName,v->new BoneAttachments());
    }

    public List<Pair<UUID, FDModel>> getAllAttachedModels(){
        List<Pair<UUID, FDModel>> list = new ArrayList<>();
        for (BoneAttachments boneAttachments : layerAttachments.values()){
            list.addAll(boneAttachments.getAllAttachedModels());
        }
        return list;
    }

    public List<Pair<UUID,FDModel>> getModelsAttachedToBone(String bone){
        return this.getBoneAttachments(bone).getAllAttachedModels();
    }

    @Override
    public CompoundTag serializeNBT(HolderLookup.Provider provider) {
        CompoundTag tag = new CompoundTag();
        int id = 0;
        for (var entry : layerAttachments.entrySet()){

            CompoundTag boneAttachment = new CompoundTag();
            boneAttachment.putString("bone", entry.getKey());

            var boneAttachments = entry.getValue().serializeNBT(provider);
            boneAttachment.put("attachment",boneAttachments);

            tag.put("attachment_"+id++, boneAttachment);
        }
        return tag;
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag tag) {

        this.layerAttachments = new HashMap<>();

        int id = 0;
        while (tag.contains("attachment_"+id)){
            CompoundTag t = tag.getCompound("attachment_"+id);

            BoneAttachments boneAttachments = new BoneAttachments();
            boneAttachments.deserializeNBT(provider, t.getCompound("attachment"));
            String bone = t.getString("bone");

            this.layerAttachments.put(bone, boneAttachments);

            id++;
        }

    }
}
