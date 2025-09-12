package com.finderfeed.fdlib.systems.bedrock.animations.animation_system.model_system.entity_model_system.packets;

import com.finderfeed.fdlib.FDClientPacketExecutables;
import com.finderfeed.fdlib.network.FDPacket;
import com.finderfeed.fdlib.network.RegisterFDPacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.neoforge.network.handling.Supplier<NetworkEvent.Context>;

import java.util.UUID;

@RegisterFDPacket("fdlib:remove_entity_attachment")
public class RemoveEntityAttachmentPacket extends FDPacket {

    private int entityId;
    private UUID uuid;

    public RemoveEntityAttachmentPacket(Entity entity, UUID uuid){
        this.uuid = uuid;
        this.entityId = entity.getId();
    }

    public RemoveEntityAttachmentPacket(FriendlyByteBuf buf){
        this.entityId = buf.readInt();
        this.uuid = buf.readUUID();
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeInt(entityId);
        buf.writeUUID(uuid);
    }

    @Override
    public void clientAction(Supplier<NetworkEvent.Context> context) {
        FDClientPacketExecutables.removeEntityAttachment(entityId, uuid);
    }

    @Override
    public void serverAction(Supplier<NetworkEvent.Context> context) {

    }
}
