package com.finderfeed.fdlib.systems.bedrock.animations.animation_system.entity.packets;


import com.finderfeed.fdlib.FDClientPacketExecutables;
import com.finderfeed.fdlib.network.FDPacket;
import com.finderfeed.fdlib.network.RegisterFDPacket;
import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.AnimationTicker;
import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.TickerSyncInstance;
import net.minecraft.network.FriendlyByteBuf;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.Map;

@RegisterFDPacket("fdlib:sync_entity_animations")
public class SyncEntityAnimationsPacket extends FDPacket {

    private int entityId;
    private TickerSyncInstance[] tickers;

    public SyncEntityAnimationsPacket(int entityId, Map<String, AnimationTicker> tickers){
        this.entityId = entityId;
        this.tickers = new TickerSyncInstance[tickers.size()];
        int i = 0;
        for (var entry : tickers.entrySet()){
            this.tickers[i] = new TickerSyncInstance(entry.getKey(),entry.getValue());
            i++;
        }
    }

    public SyncEntityAnimationsPacket(FriendlyByteBuf buf){
        this.entityId = buf.readInt();
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
        buf.writeInt(this.entityId);
        buf.writeInt(tickers.length);
        for (TickerSyncInstance instance : tickers){
            buf.writeUtf(instance.tickerName());
            AnimationTicker.NETWORK_CODEC.encode(buf,instance.ticker());
        }
    }

    @Override
    public void clientAction(IPayloadContext context) {
        FDClientPacketExecutables.entitySyncAnimationsPacket(this.entityId,this.tickers);
    }

    @Override
    public void serverAction(IPayloadContext context) {

    }


}
