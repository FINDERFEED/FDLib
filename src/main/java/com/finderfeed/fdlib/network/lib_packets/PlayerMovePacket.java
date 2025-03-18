package com.finderfeed.fdlib.network.lib_packets;

import com.finderfeed.fdlib.FDClientPacketExecutables;
import com.finderfeed.fdlib.network.FDPacket;
import com.finderfeed.fdlib.network.RegisterFDPacket;
import com.finderfeed.fdlib.util.FDByteBufCodecs;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.handling.IPayloadContext;

@RegisterFDPacket("fdlib:player_move_packet")
public class PlayerMovePacket extends FDPacket {

    private Vec3 movement;

    public PlayerMovePacket(Vec3 movement){
        this.movement = movement;
    }

    public PlayerMovePacket(FriendlyByteBuf buf){
        this.movement = FDByteBufCodecs.VEC3.decode(buf);
    }

    @Override
    public void write(RegistryFriendlyByteBuf buf) {
        FDByteBufCodecs.VEC3.encode(buf,movement);
    }

    @Override
    public void clientAction(IPayloadContext context) {
        FDClientPacketExecutables.movePlayer(movement);
    }

    @Override
    public void serverAction(IPayloadContext context) {

    }
}
