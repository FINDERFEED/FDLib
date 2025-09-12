package com.finderfeed.fdlib.systems.bedrock.animations.animation_system.tile.packets;

import com.finderfeed.fdlib.FDClientPacketExecutables;
import com.finderfeed.fdlib.network.FDPacket;
import com.finderfeed.fdlib.network.RegisterFDPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.FriendlyByteBuf;


@RegisterFDPacket("fdlib:freeze_tile_animations_packet")
public class FreezeTileAnimationsPacket extends FDPacket {

    private BlockPos pos;
    private boolean state;

    public FreezeTileAnimationsPacket(BlockPos entityId,boolean state){
        this.state = state;
        this.pos = entityId;
    }

    public FreezeTileAnimationsPacket(FriendlyByteBuf buf){
        this.pos = buf.readBlockPos();
        this.state = buf.readBoolean();
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeBlockPos(this.pos);
        buf.writeBoolean(state);
    }

    @Override
    public void clientAction(Supplier<NetworkEvent.Context> context) {
        FDClientPacketExecutables.tileEntityFreezeAnimations(this.pos,this.state);
    }

    @Override
    public void serverAction(Supplier<NetworkEvent.Context> context) {

    }
}