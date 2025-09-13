package com.finderfeed.fdlib.network.lib_packets;

import com.finderfeed.fdlib.FDClientPacketExecutables;
import com.finderfeed.fdlib.network.FDPacket;
import com.finderfeed.fdlib.network.RegisterFDPacket;
import com.finderfeed.fdlib.systems.stream_codecs.NetworkCodec;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;


@RegisterFDPacket("fdlib:player_move_packet")
public class PlayerMovePacket extends FDPacket {

    private Vec3 movement;

    public PlayerMovePacket(Vec3 movement){
        this.movement = movement;
    }

    public PlayerMovePacket(FriendlyByteBuf buf){
        this.movement = NetworkCodec.VEC3.fromNetwork(buf);
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        NetworkCodec.VEC3.toNetwork(buf,movement);
    }

    @Override
    public void clientAction(Supplier<NetworkEvent.Context> context) {
        FDClientPacketExecutables.movePlayer(movement);
    }

    @Override
    public void serverAction(Supplier<NetworkEvent.Context> context) {

    }
}
