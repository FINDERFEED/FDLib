package com.finderfeed.fdlib.systems.bedrock.animations.animation_system.entity.head;

import com.finderfeed.fdlib.FDClientHelpers;
import com.finderfeed.fdlib.network.FDPacket;
import com.finderfeed.fdlib.network.RegisterFDPacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.control.LookControl;
import net.minecraftforge.neoforge.network.handling.Supplier<NetworkEvent.Context>;

@RegisterFDPacket("fdlib:wanted_look_coords_packet")
public class WantedLookCoordinatesPacket extends FDPacket {

    private int entityId;
    private double x;
    private double y;
    private double z;

    public WantedLookCoordinatesPacket(FriendlyByteBuf buf){
        this.entityId = buf.readInt();
        this.x = buf.readDouble();
        this.y = buf.readDouble();
        this.z = buf.readDouble();
    }

    public WantedLookCoordinatesPacket(Mob entity, double x, double y, double z){
        this.entityId = entity.getId();
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public void write(FriendlyByteBuf FriendlyByteBuf) {
        FriendlyByteBuf.writeInt(entityId);
        FriendlyByteBuf.writeDouble(x);
        FriendlyByteBuf.writeDouble(y);
        FriendlyByteBuf.writeDouble(z);
    }

    @Override
    public void clientAction(Supplier<NetworkEvent.Context> Supplier<NetworkEvent.Context>) {
        if (FDClientHelpers.getClientLevel().getEntity(entityId) instanceof Mob mob){
            LookControl lookControl = mob.getLookControl();
            lookControl.setLookAt(x,y,z);
        }
    }

    @Override
    public void serverAction(Supplier<NetworkEvent.Context> Supplier<NetworkEvent.Context>) {

    }
}
