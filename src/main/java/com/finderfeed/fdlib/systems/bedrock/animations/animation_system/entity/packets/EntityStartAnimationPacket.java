package com.finderfeed.fdlib.systems.bedrock.animations.animation_system.entity.packets;

import com.finderfeed.fdlib.FDClientPacketExecutables;
import com.finderfeed.fdlib.network.FDPacket;
import com.finderfeed.fdlib.network.RegisterFDPacket;
import com.finderfeed.fdlib.systems.bedrock.animations.Animation;
import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.AnimationTicker;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.FriendlyByteBuf;


@RegisterFDPacket("fdlib:entity_start_animation_packet")
public class EntityStartAnimationPacket extends FDPacket {

    private int entityId;
    private String tickerName;
    private AnimationTicker ticker;
    public EntityStartAnimationPacket(int entityId, String layer, AnimationTicker ticker){
        Animation animation = ticker.getAnimation();
        if (animation.isTransition() || animation.isToNullTransition()){
            throw new RuntimeException("Cannot sync transitions! Only registered animations are allowed!");
        }
        this.entityId = entityId;
        this.tickerName = layer;
        this.ticker = ticker;
    }

    public EntityStartAnimationPacket(FriendlyByteBuf buf){
        this.entityId = buf.readInt();
        this.tickerName = buf.readUtf();
        this.ticker = AnimationTicker.NETWORK_CODEC.decode(buf);
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeInt(entityId);
        buf.writeUtf(tickerName);
        AnimationTicker.NETWORK_CODEC.encode(buf,ticker);
    }

    @Override
    public void clientAction(Supplier<NetworkEvent.Context> context) {
        FDClientPacketExecutables.entityStartAnimationPacket(entityId,tickerName,ticker);
    }

    @Override
    public void serverAction(Supplier<NetworkEvent.Context> context) {
        System.out.println("Wtf?");
    }
}
