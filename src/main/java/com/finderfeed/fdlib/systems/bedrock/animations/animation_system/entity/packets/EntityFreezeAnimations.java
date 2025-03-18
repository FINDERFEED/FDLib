package com.finderfeed.fdlib.systems.bedrock.animations.animation_system.entity.packets;

import com.finderfeed.fdlib.FDClientPacketExecutables;
import com.finderfeed.fdlib.network.FDPacket;
import com.finderfeed.fdlib.network.RegisterFDPacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.neoforged.neoforge.network.handling.IPayloadContext;

@RegisterFDPacket("fdlib:freeze_entity_animations_packet")
public class EntityFreezeAnimations extends FDPacket {

    private int entityId;
    private boolean state;

    public EntityFreezeAnimations(int entityId,boolean state){
        this.state = state;
        this.entityId = entityId;
    }

    public EntityFreezeAnimations(FriendlyByteBuf buf){
        this.entityId = buf.readInt();
        this.state = buf.readBoolean();
    }

    @Override
    public void write(RegistryFriendlyByteBuf buf) {
        buf.writeInt(this.entityId);
        buf.writeBoolean(state);
    }

    @Override
    public void clientAction(IPayloadContext context) {
        FDClientPacketExecutables.entityFreezeAnimations(this.entityId,this.state);
    }

    @Override
    public void serverAction(IPayloadContext context) {

    }
}
