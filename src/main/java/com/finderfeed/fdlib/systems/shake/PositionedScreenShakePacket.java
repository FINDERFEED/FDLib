package com.finderfeed.fdlib.systems.shake;

import com.finderfeed.fdlib.ClientMixinHandler;
import com.finderfeed.fdlib.network.FDPacket;
import com.finderfeed.fdlib.network.RegisterFDPacket;
import com.finderfeed.fdlib.util.FDByteBufCodecs;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.handling.IPayloadContext;


@RegisterFDPacket("fdlib:position_screen_shake")
public class PositionedScreenShakePacket extends FDPacket {

    public FDShakeData data;
    public Vec3 pos;


    public PositionedScreenShakePacket(FDShakeData data, Vec3 pos){
        this.data = data;
        this.pos = pos;
    }

    public PositionedScreenShakePacket(FriendlyByteBuf buf){
        this.data = FDShakeData.STREAM_CODEC.decode(buf);
        this.pos = FDByteBufCodecs.VEC3.decode(buf);
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        FDShakeData.STREAM_CODEC.encode(buf,data);
        FDByteBufCodecs.VEC3.encode(buf,pos);
    }

    @Override
    public void clientAction(IPayloadContext context) {
        ClientMixinHandler.addShake(new PositionedScreenShake(data,pos));
    }

    @Override
    public void serverAction(IPayloadContext context) {

    }
}
