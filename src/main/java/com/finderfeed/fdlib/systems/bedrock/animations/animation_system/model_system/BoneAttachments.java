package com.finderfeed.fdlib.systems.bedrock.animations.animation_system.model_system;

import com.finderfeed.fdlib.data_structures.Pair;
import com.finderfeed.fdlib.systems.FDRegistries;
import com.finderfeed.fdlib.systems.bedrock.models.FDModel;
import com.finderfeed.fdlib.systems.bedrock.models.FDModelInfo;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.util.INBTSerializable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BoneAttachments implements INBTSerializable<CompoundTag> {

    private List<Pair<UUID, FDModel>> boneAttachments = new ArrayList<>();

    public boolean hasAttachment(UUID attachmentUUID){
        return this.listContainsModelWithUUID(boneAttachments,attachmentUUID);
    }

    protected boolean removeAttachment(UUID uuid){
        var iterator = boneAttachments.listIterator();
        while (iterator.hasNext()){
            var pair = iterator.next();
            var uuid2 = pair.first;
            if (uuid2.equals(uuid)){
                iterator.remove();
                return true;
            }
        }
        return false;
    }

    protected void attach(UUID uuid, FDModel fdModel){
        if (!this.hasAttachment(uuid)) {
            this.boneAttachments.add(new Pair<>(uuid, fdModel));
        }
    }

    private boolean listContainsModelWithUUID(List<Pair<UUID, FDModel>> list, UUID uuid){
        for (var entry : list){
            if (entry.first.equals(uuid)){
                return true;
            }
        }
        return false;
    }

    public List<Pair<UUID, FDModel>> getAllAttachedModels(){
        return boneAttachments;
    }


    @Override
    public CompoundTag serializeNBT(HolderLookup.Provider provider) {

        CompoundTag tag = new CompoundTag();
        int id = 0;
        for (var pair : this.boneAttachments){

            CompoundTag attachment = new CompoundTag();
            attachment.putUUID("uuid", pair.first);

            FDModel fdModel = pair.second;
            var modelInfoId = fdModel.getModelInfoId();
            attachment.putString("modelInfoId", modelInfoId.toString());

            tag.put("attachment_" + id++, attachment);
        }

        return tag;
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag tag) {

        this.boneAttachments = new ArrayList<>();

        int id = 0;
        while (tag.contains("attachment_"+id)){

            var attachment = tag.getCompound("attachment"+id);

            var uuid = attachment.getUUID("uuid");
            var modelInfoId = attachment.getString("modelInfoId");

            FDModelInfo modelInfo = FDRegistries.MODELS.get(ResourceLocation.parse(modelInfoId));

            FDModel fdModel = new FDModel(modelInfo);

            this.boneAttachments.add(new Pair<>(uuid,fdModel));

            id++;
        }

    }
}
