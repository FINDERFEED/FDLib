package com.finderfeed.fdlib.systems.bedrock.animations.animation_system.tile.packets;

import com.finderfeed.fdlib.FDClientPacketExecutables;
import com.finderfeed.fdlib.network.FDPacket;
import com.finderfeed.fdlib.network.RegisterFDPacket;
import com.finderfeed.fdlib.systems.bedrock.animations.Animation;
import com.finderfeed.fdlib.systems.bedrock.animations.TransitionAnimation;
import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.AnimationTicker;
import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.TickerSyncInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraftforge.neoforge.network.handling.IPayloadContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RegisterFDPacket("fdlib:sync_tile_animations")
public class SyncTileAnimationsPacket extends FDPacket {

    private BlockPos pos;
    private List<TickerSyncInstance> tickers;

    public SyncTileAnimationsPacket(BlockPos pos, Map<String, AnimationTicker> tickers){
        this.pos = pos;
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

    public SyncTileAnimationsPacket(FriendlyByteBuf buf){
        this.pos = buf.readBlockPos();
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
    public void write(RegistryFriendlyByteBuf buf) {
        buf.writeBlockPos(pos);
        buf.writeInt(tickers.size());
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
