package com.finderfeed.fdlib.systems.bedrock.animations.animation_system.item.packets;

import com.finderfeed.fdlib.FDClientPacketExecutables;
import com.finderfeed.fdlib.network.FDPacket;
import com.finderfeed.fdlib.network.RegisterFDPacket;
import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.AnimationTicker;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

@RegisterFDPacket("fdbosses:start_item_animation_in_hand_packet")
public class StartItemAnimationInHandPacket extends FDPacket {

    private String layer;
    private int entityId;
    private AnimationTicker animation;
    private InteractionHand hand;

    public StartItemAnimationInHandPacket(String layer, LivingEntity serverPlayer, AnimationTicker animationTicker, InteractionHand hand){
        this.layer = layer;
        this.animation = animationTicker;
        this.hand = hand;
        this.entityId = serverPlayer.getId();
    }

    public StartItemAnimationInHandPacket(FriendlyByteBuf buf){
        this.hand = InteractionHand.values()[buf.readInt()];
        this.animation = AnimationTicker.NETWORK_CODEC.fromNetwork(buf);
        this.entityId = buf.readInt();
        this.layer = buf.readUtf();
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeInt(hand.ordinal());
        AnimationTicker.NETWORK_CODEC.toNetwork(buf, animation);
        buf.writeInt(entityId);
        buf.writeUtf(layer);
    }

    @Override
    public void clientAction(Supplier<NetworkEvent.Context> ctx) {
        FDClientPacketExecutables.startItemAnimationInHand(entityId, animation, hand, layer);
    }

    @Override
    public void serverAction(Supplier<NetworkEvent.Context> ctx) {

    }

}
