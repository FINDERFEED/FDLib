package com.finderfeed.fdlib.systems.bedrock.animations.animation_system.model_system.attachments;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;

public class ModelAttachmentRenderContext {

    private Entity entity;
    private BlockEntity blockEntity;

    public static ModelAttachmentRenderContext create(Entity entity){
        ModelAttachmentRenderContext renderContext = new ModelAttachmentRenderContext();
        renderContext.entity = entity;
        return renderContext;
    }
    public static ModelAttachmentRenderContext create(BlockEntity entity){
        ModelAttachmentRenderContext renderContext = new ModelAttachmentRenderContext();
        renderContext.blockEntity = entity;
        return renderContext;
    }

    public BlockEntity getBlockEntity() {
        return blockEntity;
    }

    public Entity getEntity() {
        return entity;
    }

}
