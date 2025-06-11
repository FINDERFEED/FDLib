package com.finderfeed.fdlib.systems.bedrock.animations.animation_system;

import com.finderfeed.fdlib.systems.bedrock.models.FDModel;
import com.finderfeed.fdlib.systems.bedrock.models.FDModelInfo;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.neoforged.neoforge.common.util.INBTSerializable;
import org.jetbrains.annotations.UnknownNullability;

import java.util.HashMap;
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
