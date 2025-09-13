package com.finderfeed.fdlib.systems.bedrock.animations.animation_system.model_system.entity_model_system;

import com.finderfeed.fdlib.network.FDPacketHandler;
import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.AnimatedObject;
import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.entity.packets.SyncEntityAnimationsPacket;
import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.model_system.attachments.ModelAttachmentData;
import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.model_system.entity_model_system.packets.AddEntityAttachmentPacket;
import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.model_system.entity_model_system.packets.RemoveEntityAttachmentPacket;
import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.model_system.entity_model_system.packets.SyncEntityAttachmentsPacket;
import com.finderfeed.fdlib.systems.bedrock.models.FDModelInfo;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.PacketDistributor;

import java.util.UUID;

public class ServerEntityModelSystem<T extends Entity & AnimatedObject> extends EntityModelSystem<T>{

    protected ServerEntityModelSystem(T entity) {
        super(entity);
    }

    public void syncToPlayer(ServerPlayer serverPlayer){

        SyncEntityAnimationsPacket packet = new SyncEntityAnimationsPacket(this.getEntity().getId(),this.getAnimationSystem().getTickers());
        FDPacketHandler.INSTANCE.sendTo(packet,serverPlayer.connection.connection, NetworkDirection.PLAY_TO_CLIENT);

        SyncEntityAttachmentsPacket syncEntityAttachmentsPacket = new SyncEntityAttachmentsPacket(this.getEntity(),this);
        FDPacketHandler.INSTANCE.sendTo(syncEntityAttachmentsPacket,serverPlayer.connection.connection, NetworkDirection.PLAY_TO_CLIENT);


    }

    @Override
    public void onAttachment(int layer, String bone, UUID modelUUID, ModelAttachmentData<?> attachedModel) {
        AddEntityAttachmentPacket addEntityAttachmentPacket = new AddEntityAttachmentPacket(this.getEntity(),layer,bone,modelUUID,attachedModel);
        FDPacketHandler.INSTANCE.send(PacketDistributor.TRACKING_ENTITY.with(this::getEntity), addEntityAttachmentPacket);
    }

    @Override
    public void onAttachmentRemoved(UUID modelUUID) {

        RemoveEntityAttachmentPacket removeEntityAttachmentPacket = new RemoveEntityAttachmentPacket(this.getEntity(),modelUUID);
        FDPacketHandler.INSTANCE.send(PacketDistributor.TRACKING_ENTITY.with(this::getEntity), removeEntityAttachmentPacket);

    }



}
