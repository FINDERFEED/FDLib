package com.finderfeed.fdlib.systems.bedrock.animations.animation_system.entity.packets;


import com.finderfeed.fdlib.FDClientPacketExecutables;
import com.finderfeed.fdlib.network.FDPacket;
import com.finderfeed.fdlib.network.RegisterFDPacket;
import com.finderfeed.fdlib.systems.bedrock.animations.Animation;
import com.finderfeed.fdlib.systems.bedrock.animations.TransitionAnimation;
import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.AnimationTicker;
import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.TickerSyncInstance;
import net.minecraft.network.FriendlyByteBuf;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RegisterFDPacket("fdlib:sync_entity_animations")
public class SyncEntityAnimationsPacket extends FDPacket {

    private int entityId;
    private List<TickerSyncInstance> tickers;

    public SyncEntityAnimationsPacket(int entityId, Map<String, AnimationTicker> tickers){
        this.entityId = entityId;
        this.tickers = new ArrayList<>();
        for (var entry : tickers.entrySet()){
            Animation animation = entry.getValue().getAnimation();
            if (animation.isToNullTransition()) continue;
            if (animation.isTransition()){
                AnimationTicker ticker = new AnimationTicker(entry.getValue());
                ticker.setAnimation(((TransitionAnimation)animation).getTransitionTo());
                this.tickers.add(new TickerSyncInstance(entry.getKey(), ticker));
            }else {
                this.tickers.add(new TickerSyncInstance(entry.getKey(), entry.getValue()));
            }
        }
    }

    public SyncEntityAnimationsPacket(FriendlyByteBuf buf){
        this.entityId = buf.readInt();
        this.tickers = new ArrayList<>();
        int length = buf.readInt();
        for (int i = 0; i < length;i++){
            tickers.add(new TickerSyncInstance(
                buf.readUtf(),
                AnimationTicker.NETWORK_CODEC.decode(buf)
            ));
        }
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeInt(this.entityId);
        buf.writeInt(tickers.size());
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
