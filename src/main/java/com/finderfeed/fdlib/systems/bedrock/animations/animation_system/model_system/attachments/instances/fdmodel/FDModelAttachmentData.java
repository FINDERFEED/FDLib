package com.finderfeed.fdlib.systems.bedrock.animations.animation_system.model_system.attachments.instances.fdmodel;

import com.finderfeed.fdlib.init.FDModelAttachmentTypes;
import com.finderfeed.fdlib.init.FDRenderTypes;
import com.finderfeed.fdlib.systems.FDRegistries;
import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.model_system.attachments.BaseModelAttachmentData;
import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.model_system.attachments.ModelAttachmentData;
import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.model_system.attachments.ModelAttachmentType;
import com.finderfeed.fdlib.systems.bedrock.models.FDModelInfo;
import com.finderfeed.fdlib.systems.render_types.FDRenderType;
import com.finderfeed.fdlib.util.NetworkCodec;
import com.finderfeed.fdlib.util.FDColor;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.NetworkCodec;
import net.minecraft.network.codec.NetworkCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class FDModelAttachmentData implements ModelAttachmentData<FDModelAttachment> {

    public static final NetworkCodec<FriendlyByteBuf, FDModelAttachmentData> STREAM_CODEC = NetworkCodec.composite(
            NetworkCodec.registry(FDRegistries.BEDROCK_MODEL_INFOS_KEY),v->v.fdModelInfo,
            BaseModelAttachmentData.CODEC,v->v.baseModelAttachmentData,
            NetworkCodec.registry(FDRegistries.RENDER_TYPE_KEY),v->v.renderType,
            ResourceLocation.STREAM_CODEC,v->v.texture,
            NetworkCodec.COLOR,v->v.color,
            (info,baseData, renderType, texture,color)->{
                FDModelAttachmentData fdModelAttachmentData = new FDModelAttachmentData();
                fdModelAttachmentData.fdModelInfo = info;
                fdModelAttachmentData.baseModelAttachmentData = baseData;
                fdModelAttachmentData.texture = texture;
                fdModelAttachmentData.renderType = renderType;
                fdModelAttachmentData.color = color;
                return fdModelAttachmentData;
            }
    );

    private FDRenderType renderType;
    private FDModelInfo fdModelInfo;
    private ResourceLocation texture;
    private BaseModelAttachmentData baseModelAttachmentData;
    private FDColor color = new FDColor(1f,1f,1f,1f);

    public static FDModelAttachmentData create(BaseModelAttachmentData baseModelAttachmentData, FDModelInfo fdModelInfo){
        return new FDModelAttachmentData(baseModelAttachmentData, FDRenderTypes.ENTITY_CUTOUT_NO_CULL.get(), fdModelInfo, ResourceLocation.withDefaultNamespace("missingno"), new FDColor(1f,1f,1f,1f));
    }

    public FDModelAttachmentData(BaseModelAttachmentData baseModelAttachmentData, FDRenderType renderType, FDModelInfo fdModelInfo, ResourceLocation texture, FDColor fdColor){
        this.fdModelInfo = fdModelInfo;
        this.baseModelAttachmentData = baseModelAttachmentData;
        this.renderType = renderType;
        this.texture = texture;
        this.color = fdColor;
    }

    public FDModelAttachmentData(){}

    public FDModelAttachmentData renderType(FDRenderType renderType){
        this.renderType = renderType;
        return this;
    }

    public FDModelAttachmentData color(float r, float g, float b, float a){
        this.color = new FDColor(r,g,b,a);
        return this;
    }

    public FDModelAttachmentData texture(ResourceLocation location){
        this.texture = location;
        return this;
    }

    public FDModelInfo getFdModelInfo() {
        return fdModelInfo;
    }

    public BaseModelAttachmentData getBaseModelAttachmentData() {
        return baseModelAttachmentData;
    }

    public FDRenderType getRenderType() {
        return renderType;
    }

    public ResourceLocation getTexture() {
        return texture;
    }

    public FDColor getColor() {
        return color;
    }

    @Override
    public ModelAttachmentType<FDModelAttachment, ?> type() {
        return FDModelAttachmentTypes.FDMODEL_ATTACHMENT.get();
    }


}
