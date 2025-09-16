package com.finderfeed.fdlib.systems.bedrock.animations.animation_system.model_system.attachments.instances.fdmodel;

import com.finderfeed.fdlib.systems.FDRegistries;
import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.model_system.attachments.BaseModelAttachmentData;
import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.model_system.attachments.ModelAttachment;
import com.finderfeed.fdlib.systems.bedrock.models.FDModel;
import com.finderfeed.fdlib.systems.bedrock.models.FDModelInfo;
import com.finderfeed.fdlib.systems.render_types.FDRenderType;
import com.finderfeed.fdlib.util.FDColor;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.UnknownNullability;

public class FDModelAttachment implements ModelAttachment<FDModelAttachment, FDModelAttachmentData> {

    private FDModelAttachmentData data;

    private FDModel fdModel;

    protected FDModelAttachment(){}

    public FDModelAttachment(FDModelAttachmentData fdModelAttachmentData){
        this.data =  fdModelAttachmentData;
        this.fdModel = new FDModel(fdModelAttachmentData.getFdModelInfo());
    }

    public FDModel getFdModel() {
        return fdModel;
    }

    public FDColor getColor(){
        return data.getColor();
    }

    public FDRenderType getRenderType(){
        return this.data.getRenderType();
    }

    public ResourceLocation getTexture(){
        return this.data.getTexture();
    }

    public BaseModelAttachmentData getData() {
        return data.getBaseModelAttachmentData();
    }

    @Override
    public FDModelAttachmentData attachmentData() {
        return data;
    }

    @Override
    public @UnknownNullability CompoundTag serializeNBT() {

        CompoundTag tag = new CompoundTag();

        ResourceLocation id = FDRegistries.MODELS.get().getKey(data.getFdModelInfo());
        tag.putString("modelInfo",id.toString());
        tag.put("data",data.getBaseModelAttachmentData().serializeNBT());

        ResourceLocation rtid = FDRegistries.RENDER_TYPE.get().getKey(this.data.getRenderType());
        tag.putString("renderType",rtid.toString());
        tag.putString("texture", this.data.getTexture().toString());

        FDColor fdColor = this.data.getColor();
        tag.putInt("color",fdColor.encode());

        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        ResourceLocation location = ResourceLocation.tryParse(nbt.getString("modelInfo"));
        FDModelInfo info = FDRegistries.MODELS.get().getValue(location);
        BaseModelAttachmentData baseModelAttachmentData = new BaseModelAttachmentData(); baseModelAttachmentData.deserializeNBT(nbt.getCompound("data"));
        ResourceLocation texture = ResourceLocation.tryParse(nbt.getString("texture"));

        ResourceLocation renderTypeLocation = ResourceLocation.tryParse(nbt.getString("renderType"));
        FDRenderType fdRenderType = FDRegistries.RENDER_TYPE.get().getValue(renderTypeLocation);

        FDColor color = FDColor.decode(nbt.getInt("color"));

        this.data = new FDModelAttachmentData(baseModelAttachmentData, fdRenderType, info, texture, color);
        this.fdModel = new FDModel(info);
    }
}
