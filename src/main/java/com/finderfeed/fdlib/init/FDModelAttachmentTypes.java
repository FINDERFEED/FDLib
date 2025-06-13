package com.finderfeed.fdlib.init;

import com.finderfeed.fdlib.FDLib;
import com.finderfeed.fdlib.systems.FDRegistries;
import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.model_system.attachments.ModelAttachmentType;
import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.model_system.attachments.instances.item_stack.ItemStackAttachment;
import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.model_system.attachments.instances.item_stack.ItemStackAttachmentData;
import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.model_system.attachments.instances.item_stack.ItemStackAttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class FDModelAttachmentTypes {

    public static final DeferredRegister<ModelAttachmentType<?,?>> MODEL_ATTACHMENT_TYPES = DeferredRegister.create(FDRegistries.MODEL_ATTACHMENT_TYPES, FDLib.MOD_ID);

    public static final Supplier<ModelAttachmentType<ItemStackAttachment, ItemStackAttachmentData>> ITEM_STACK_MODEL_ATTACHMENT = MODEL_ATTACHMENT_TYPES.register("item_stack", ItemStackAttachmentType::new);

}
