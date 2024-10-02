package com.finderfeed.fdlib.to_other_mod.packets;


import com.finderfeed.fdlib.network.FDPacket;
import com.finderfeed.fdlib.network.RegisterFDPacket;
import com.finderfeed.fdlib.to_other_mod.BossClientPackets;
import com.finderfeed.fdlib.util.FDByteBufCodecs;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.handling.IPayloadContext;

@RegisterFDPacket("fdlib:pos_level_event")
public class PosLevelEventPacket extends FDPacket {

    private Vec3 pos;
    private int event;

    public PosLevelEventPacket(Vec3 pos,int event){
        this.pos = pos;
        this.event = event;
    }

    public PosLevelEventPacket(FriendlyByteBuf buf){
        this.pos = FDByteBufCodecs.VEC3.decode(buf);
        this.event = buf.readInt();
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        FDByteBufCodecs.VEC3.encode(buf,pos);
        buf.writeInt(event);
    }

    @Override
    public void clientAction(IPayloadContext context) {
        BossClientPackets.posEvent(pos,event);
    }

    @Override
    public void serverAction(IPayloadContext context) {

    }
}
