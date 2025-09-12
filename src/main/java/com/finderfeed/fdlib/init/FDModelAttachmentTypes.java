package com.finderfeed.fdlib.init;

import com.finderfeed.fdlib.FDLib;
import com.finderfeed.fdlib.systems.FDRegistries;
import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.model_system.attachments.ModelAttachmentType;
import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.model_system.attachments.instances.fdmodel.FDModelAttachment;
import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.model_system.attachments.instances.fdmodel.FDModelAttachmentData;
import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.model_system.attachments.instances.fdmodel.FDModelAttachmentType;
import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.model_system.attachments.instances.item_stack.ItemStackAttachment;
import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.model_system.attachments.instances.item_stack.ItemStackAttachmentData;
import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.model_system.attachments.instances.item_stack.ItemStackAttachmentType;
import net.minecraftforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class FDModelAttachmentTypes {

    public static final DeferredRegister<ModelAttachmentType<?,?>> MODEL_ATTACHMENT_TYPES = DeferredRegister.create(FDRegistries.MODEL_ATTACHMENT_TYPE_KEY, FDLib.MOD_ID);

    public static final Supplier<ModelAttachmentType<ItemStackAttachment, ItemStackAttachmentData>> ITEM_STACK_MODEL_ATTACHMENT = MODEL_ATTACHMENT_TYPES.register("item_stack", ItemStackAttachmentType::new);
    public static final Supplier<ModelAttachmentType<FDModelAttachment, FDModelAttachmentData>> FDMODEL_ATTACHMENT = MODEL_ATTACHMENT_TYPES.register("fdmodel", FDModelAttachmentType::new);

}
