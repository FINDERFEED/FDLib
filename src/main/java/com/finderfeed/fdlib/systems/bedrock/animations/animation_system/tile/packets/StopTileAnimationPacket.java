package com.finderfeed.fdlib.systems.bedrock.animations.animation_system.tile.packets;

import com.finderfeed.fdlib.FDClientPacketExecutables;
import com.finderfeed.fdlib.network.FDPacket;
import com.finderfeed.fdlib.network.RegisterFDPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.FriendlyByteBuf;


@RegisterFDPacket("fdlib:stop_animation_packet")
public class StopTileAnimationPacket extends FDPacket {

    private String layer;
    private BlockPos pos;

    public StopTileAnimationPacket(BlockPos pos,String layer){
        this.layer = layer;
        this.pos = pos;
    }

    public StopTileAnimationPacket(FriendlyByteBuf buf){
        this.layer = buf.readUtf();
        this.pos = buf.readBlockPos();
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeUtf(this.layer);
        buf.writeBlockPos(this.pos);
    }

    @Override
    public void clientAction(Supplier<NetworkEvent.Context> context) {
        FDClientPacketExecutables.tileEntityStopAnimation(pos,layer);
    }

    @Override
    public void serverAction(Supplier<NetworkEvent.Context> context) {

    }
}
