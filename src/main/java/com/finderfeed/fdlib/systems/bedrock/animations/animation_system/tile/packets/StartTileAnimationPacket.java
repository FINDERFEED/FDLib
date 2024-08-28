package com.finderfeed.fdlib.systems.bedrock.animations.animation_system.tile.packets;

import com.finderfeed.fdlib.FDClientPacketExecutables;
import com.finderfeed.fdlib.network.FDPacket;
import com.finderfeed.fdlib.network.RegisterFDPacket;
import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.AnimationTicker;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.neoforged.neoforge.network.handling.IPayloadContext;

@RegisterFDPacket("fdlib:start_tile_animation")
public class StartTileAnimationPacket extends FDPacket {

    private AnimationTicker ticker;
    private String layer;
    private BlockPos pos;

    public StartTileAnimationPacket(BlockPos pos,String layer,AnimationTicker ticker){
        this.ticker = ticker;
        this.layer = layer;
        this.pos = pos;
    }

    public StartTileAnimationPacket(FriendlyByteBuf buf){
        this.ticker = AnimationTicker.NETWORK_CODEC.decode(buf);
        this.layer = buf.readUtf();
        this.pos = buf.readBlockPos();
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        AnimationTicker.NETWORK_CODEC.encode(buf,ticker);
        buf.writeUtf(this.layer);
        buf.writeBlockPos(this.pos);
    }

    @Override
    public void clientAction(IPayloadContext context) {
        FDClientPacketExecutables.tileEntityStartAnimation(pos,ticker,layer);
    }

    @Override
    public void serverAction(IPayloadContext context) {

    }
}
