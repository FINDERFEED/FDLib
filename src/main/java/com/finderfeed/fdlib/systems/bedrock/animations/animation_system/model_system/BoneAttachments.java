package com.finderfeed.fdlib.systems.bedrock.animations.animation_system.model_system;

import com.finderfeed.fdlib.data_structures.Pair;
import com.finderfeed.fdlib.systems.FDRegistries;
import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.model_system.attachments.ModelAttachment;
import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.model_system.attachments.ModelAttachmentType;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BoneAttachments implements INBTSerializable<CompoundTag> {

    private List<Pair<UUID, ModelAttachment<?,?>>> boneAttachments = new ArrayList<>();

    public boolean hasAttachment(UUID attachmentUUID){
        return this.listContainsAttachmentWithUUID(boneAttachments,attachmentUUID);
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

    protected void attach(UUID uuid, ModelAttachment<?,?> fdModel){
        if (!this.hasAttachment(uuid)) {
            this.boneAttachments.add(new Pair<>(uuid, fdModel));
        }
    }

    private boolean listContainsAttachmentWithUUID(List<Pair<UUID, ModelAttachment<?,?>>> list, UUID uuid){
        for (var entry : list){
            if (entry.first.equals(uuid)){
                return true;
            }
        }
        return false;
    }

    public List<Pair<UUID, ModelAttachment<?,?>>> getAttachments(){
        return boneAttachments;
    }


    @Override
    public CompoundTag serializeNBT() {

        CompoundTag tag = new CompoundTag();
        int id = 0;
        for (var pair : this.boneAttachments){

            CompoundTag attachment = new CompoundTag();
            attachment.putUUID("uuid", pair.first);

            ModelAttachment<?,?> modelAttachment = pair.second;

            ModelAttachmentType<?,?> type = modelAttachment.attachmentData().type();

            var key = FDRegistries.MODEL_ATTACHMENT_TYPES.get().getKey(type);


            CompoundTag t = modelAttachment.serializeNBT();
            attachment.put("attachmentData",t);
            attachment.putString("attachmentType",key.toString());

            tag.put("attachment_" + id++, attachment);
        }

        return tag;
    }

    @Override
    public void deserializeNBT( CompoundTag tag) {

        this.boneAttachments = new ArrayList<>();

        int id = 0;
        while (tag.contains("attachment_"+id)){

            var attachment = tag.getCompound("attachment_"+id);

            var uuid = attachment.getUUID("uuid");

            CompoundTag modelAttachmentTag = attachment.getCompound("attachmentData");
            var location = ResourceLocation.parse(attachment.getString("attachmentType"));

            ModelAttachmentType<?,?> type = FDRegistries.MODEL_ATTACHMENT_TYPES.get().getValue(location);

            var modelAttachment = type.createInstance();
            modelAttachment.deserializeNBT(modelAttachmentTag);

            this.boneAttachments.add(new Pair<>(uuid,modelAttachment));

            id++;
        }

    }
}
