package com.finderfeed.fdlib.systems.bedrock.animations.animation_system.tile.packets;

import com.finderfeed.fdlib.FDClientPacketExecutables;
import com.finderfeed.fdlib.network.FDPacket;
import com.finderfeed.fdlib.network.RegisterFDPacket;
import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.AnimationTicker;
import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.TickerSyncInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.Map;

@RegisterFDPacket("fdlib:sync_tile_animations")
public class SyncTileAnimationsPacket extends FDPacket {

    private BlockPos pos;
    private TickerSyncInstance[] tickers;

    public SyncTileAnimationsPacket(BlockPos pos, Map<String, AnimationTicker> tickers){
        this.pos = pos;
        this.tickers = new TickerSyncInstance[tickers.size()];
        int i = 0;
        for (var entry : tickers.entrySet()){
            this.tickers[i] = new TickerSyncInstance(entry.getKey(),entry.getValue());
            i++;
        }
    }

    public SyncTileAnimationsPacket(FriendlyByteBuf buf){
        this.pos = buf.readBlockPos();
        this.tickers = new TickerSyncInstance[buf.readInt()];
        for (int i = 0; i < tickers.length;i++){
            tickers[i] = new TickerSyncInstance(
                    buf.readUtf(),
                    AnimationTicker.NETWORK_CODEC.decode(buf)
            );
        }
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeBlockPos(pos);
        buf.writeInt(tickers.length);
        for (TickerSyncInstance instance : tickers){
            buf.writeUtf(instance.tickerName());
            AnimationTicker.NETWORK_CODEC.encode(buf,instance.ticker());
        }
    }

    @Override
    public void clientAction(IPayloadContext context) {
        FDClientPacketExecutables.tileEntitySyncAnimations(this.pos,this.tickers);
    }

    @Override
    public void serverAction(IPayloadContext context) {

    }
}
