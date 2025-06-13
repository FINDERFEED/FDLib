package com.finderfeed.fdlib.systems.bedrock.animations.animation_system.model_system.entity_model_system.packets;

import com.finderfeed.fdlib.FDClientPacketExecutables;
import com.finderfeed.fdlib.network.FDPacket;
import com.finderfeed.fdlib.network.RegisterFDPacket;
import com.finderfeed.fdlib.systems.FDRegistries;
import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.model_system.attachments.ModelAttachment;
import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.model_system.attachments.ModelAttachmentData;
import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.model_system.attachments.ModelAttachmentType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.Entity;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.UUID;

@RegisterFDPacket("fdlib:add_entity_attachment")
public class AddEntityAttachmentPacket extends FDPacket {

    private int entityId;
    private int layer;
    private String bone;
    private UUID uuid;
    private ModelAttachmentData<?> modelAttachmentData;

    public AddEntityAttachmentPacket(Entity entity, int layer, String bone, UUID uuid, ModelAttachmentData<?> modelAttachmentData){
        this.entityId = entity.getId();
        this.layer = layer;
        this.bone = bone;
        this.uuid = uuid;
        this.modelAttachmentData = modelAttachmentData;
    }

    public AddEntityAttachmentPacket(RegistryFriendlyByteBuf buf){
        this.entityId = buf.readInt();
        this.layer = buf.readInt();
        this.bone = buf.readUtf();
        this.uuid = buf.readUUID();

        var registry = buf.registryAccess().registryOrThrow(FDRegistries.MODEL_ATTACHMENT_TYPE_KEY);
        var type = registry.get(buf.readResourceLocation());
        modelAttachmentData = type.dataStreamCodec().decode(buf);

    }

    @Override
    public void write(RegistryFriendlyByteBuf buf) {
        buf.writeInt(entityId);
        buf.writeInt(layer);
        buf.writeUtf(bone);
        buf.writeUUID(uuid);

        ModelAttachmentType<?,? extends ModelAttachmentData<?>> type = modelAttachmentData.type();
        var registry = buf.registryAccess().registryOrThrow(FDRegistries.MODEL_ATTACHMENT_TYPE_KEY);
        buf.writeResourceLocation(registry.getKey(type));

        this.hackyEncode(buf, type);
    }

    @Override
    public void clientAction(IPayloadContext context) {
        FDClientPacketExecutables.addEntityAttachmentPacket(entityId,layer,bone,uuid, modelAttachmentData);
    }

    @Override
    public void serverAction(IPayloadContext context) {

    }

    private <T extends ModelAttachmentData<?>> void hackyEncode(RegistryFriendlyByteBuf buf, ModelAttachmentType<?,T> type){
        type.dataStreamCodec().encode(buf,(T) modelAttachmentData);
    }

}
