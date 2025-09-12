package com.finderfeed.fdlib.systems.bedrock.animations.animation_system.model_system.entity_model_system.packets;

import com.finderfeed.fdlib.FDClientPacketExecutables;
import com.finderfeed.fdlib.network.FDPacket;
import com.finderfeed.fdlib.network.RegisterFDPacket;
import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.model_system.ModelSystem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.neoforge.network.handling.Supplier<NetworkEvent.Context>;

@RegisterFDPacket("fdlib:sync_model_system")
public class SyncEntityAttachmentsPacket extends FDPacket {

    private CompoundTag entityAttachmentsData;
    private int entityId;

    public SyncEntityAttachmentsPacket(Entity entity, ModelSystem modelSystem){
        this.entityAttachmentsData = new CompoundTag();
        modelSystem.saveAttachments(entity.level().registryAccess(), entityAttachmentsData);
        this.entityId = entity.getId();
    }

    public SyncEntityAttachmentsPacket(FriendlyByteBuf buf){
        this.entityAttachmentsData = buf.readNbt();
        this.entityId = buf.readInt();
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeNbt(entityAttachmentsData);
        buf.writeInt(this.entityId);
    }

    @Override
    public void clientAction(Supplier<NetworkEvent.Context> context) {

        FDClientPacketExecutables.syncEntityAttachmentsPacket(entityId, entityAttachmentsData);

    }

    @Override
    public void serverAction(Supplier<NetworkEvent.Context> context) {

    }
}
