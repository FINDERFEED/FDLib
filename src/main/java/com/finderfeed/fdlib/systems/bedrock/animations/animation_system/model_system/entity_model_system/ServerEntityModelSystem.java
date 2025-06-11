package com.finderfeed.fdlib.systems.bedrock.animations.animation_system.model_system.entity_model_system;

import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.AnimatedObject;
import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.entity.packets.SyncEntityAnimationsPacket;
import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.model_system.entity_model_system.packets.AddEntityAttachmentPacket;
import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.model_system.entity_model_system.packets.RemoveEntityAttachmentPacket;
import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.model_system.entity_model_system.packets.SyncEntityAttachmentsPacket;
import com.finderfeed.fdlib.systems.bedrock.models.FDModelInfo;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.UUID;

public class ServerEntityModelSystem<T extends Entity & AnimatedObject> extends EntityModelSystem<T>{

    protected ServerEntityModelSystem(T entity) {
        super(entity);
    }

    public void syncToPlayer(ServerPlayer serverPlayer){

        SyncEntityAnimationsPacket packet = new SyncEntityAnimationsPacket(this.getEntity().getId(),this.getAnimationSystem().getTickers());
        PacketDistributor.sendToPlayer(serverPlayer,packet);

        SyncEntityAttachmentsPacket syncEntityAttachmentsPacket = new SyncEntityAttachmentsPacket(this.getEntity(),this);
        PacketDistributor.sendToPlayer(serverPlayer,syncEntityAttachmentsPacket);

    }

    @Override
    public void onAttachment(int layer, String bone, UUID modelUUID, FDModelInfo attachedModel) {

        AddEntityAttachmentPacket addEntityAttachmentPacket = new AddEntityAttachmentPacket(this.getEntity(),layer,bone,modelUUID,attachedModel);
        PacketDistributor.sendToPlayersTrackingEntity(this.getEntity(), addEntityAttachmentPacket);

    }

    @Override
    public void onAttachmentRemoved(UUID modelUUID) {

        RemoveEntityAttachmentPacket removeEntityAttachmentPacket = new RemoveEntityAttachmentPacket(this.getEntity(),modelUUID);
        PacketDistributor.sendToPlayersTrackingEntity(this.getEntity(), removeEntityAttachmentPacket);

    }



}
