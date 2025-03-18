package com.finderfeed.fdlib.systems.bedrock.animations.animation_system.entity.packets;

import com.finderfeed.fdlib.FDClientPacketExecutables;
import com.finderfeed.fdlib.network.FDPacket;
import com.finderfeed.fdlib.network.RegisterFDPacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.neoforged.neoforge.network.handling.IPayloadContext;

@RegisterFDPacket("fdlib:entity_stop_animation_packet")
public class EntityStopAnimationPacket extends FDPacket {

    private int entityId;
    private String tickerName;
    public EntityStopAnimationPacket(int entityId, String tickerName){
        this.entityId = entityId;
        this.tickerName = tickerName;
    }

    public EntityStopAnimationPacket(FriendlyByteBuf buf){
        this.entityId = buf.readInt();
        this.tickerName = buf.readUtf();
    }

    @Override
    public void write(RegistryFriendlyByteBuf buf) {
        buf.writeInt(this.entityId);
        buf.writeUtf(this.tickerName);
    }

    @Override
    public void clientAction(IPayloadContext context) {
        FDClientPacketExecutables.entityStopAnimationPacket(entityId,tickerName);
    }

    @Override
    public void serverAction(IPayloadContext context) {

    }
}
