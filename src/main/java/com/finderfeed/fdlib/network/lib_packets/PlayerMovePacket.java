package com.finderfeed.fdlib.network.lib_packets;

import com.finderfeed.fdlib.FDClientPacketExecutables;
import com.finderfeed.fdlib.network.FDPacket;
import com.finderfeed.fdlib.network.RegisterFDPacket;
import com.finderfeed.fdlib.util.NetworkCodec;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.neoforge.network.handling.Supplier<NetworkEvent.Context>;

@RegisterFDPacket("fdlib:player_move_packet")
public class PlayerMovePacket extends FDPacket {

    private Vec3 movement;

    public PlayerMovePacket(Vec3 movement){
        this.movement = movement;
    }

    public PlayerMovePacket(FriendlyByteBuf buf){
        this.movement = NetworkCodec.VEC3.decode(buf);
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        NetworkCodec.VEC3.encode(buf,movement);
    }

    @Override
    public void clientAction(Supplier<NetworkEvent.Context> context) {
        FDClientPacketExecutables.movePlayer(movement);
    }

    @Override
    public void serverAction(Supplier<NetworkEvent.Context> context) {

    }
}
