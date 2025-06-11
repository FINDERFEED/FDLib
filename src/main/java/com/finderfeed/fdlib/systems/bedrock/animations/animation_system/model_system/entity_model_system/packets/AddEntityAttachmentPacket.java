package com.finderfeed.fdlib.systems.bedrock.animations.animation_system.model_system.entity_model_system.packets;

import com.finderfeed.fdlib.FDClientPacketExecutables;
import com.finderfeed.fdlib.network.FDPacket;
import com.finderfeed.fdlib.network.RegisterFDPacket;
import com.finderfeed.fdlib.systems.FDRegistries;
import com.finderfeed.fdlib.systems.bedrock.models.FDModelInfo;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.UUID;

@RegisterFDPacket("fdlib:add_entity_attachment")
public class AddEntityAttachmentPacket extends FDPacket {

    private int entityId;
    private int layer;
    private String bone;
    private UUID uuid;
    private FDModelInfo fdModelInfo;

    public AddEntityAttachmentPacket(Entity entity, int layer, String bone, UUID uuid, FDModelInfo modelInfo){
        this.entityId = entity.getId();
        this.layer = layer;
        this.bone = bone;
        this.uuid = uuid;
        this.fdModelInfo = modelInfo;
    }

    public AddEntityAttachmentPacket(FriendlyByteBuf buf){
        this.entityId = buf.readInt();
        this.layer = buf.readInt();
        this.bone = buf.readUtf();
        this.uuid = buf.readUUID();
        ResourceLocation loc = buf.readResourceLocation();
        this.fdModelInfo = FDRegistries.MODELS.get(loc);
    }

    @Override
    public void write(RegistryFriendlyByteBuf buf) {
        buf.writeInt(entityId);
        buf.writeInt(layer);
        buf.writeUtf(bone);
        buf.writeUUID(uuid);
        ResourceLocation key = FDRegistries.MODELS.getKey(fdModelInfo);
        buf.writeResourceLocation(key);
    }

    @Override
    public void clientAction(IPayloadContext context) {
        FDClientPacketExecutables.addEntityAttachmentPacket(entityId,layer,bone,uuid,fdModelInfo);
    }

    @Override
    public void serverAction(IPayloadContext context) {

    }
}
