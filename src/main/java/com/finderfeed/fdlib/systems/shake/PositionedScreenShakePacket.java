package com.finderfeed.fdlib.systems.shake;

import com.finderfeed.fdlib.ClientMixinHandler;
import com.finderfeed.fdlib.network.FDPacket;
import com.finderfeed.fdlib.network.RegisterFDPacket;
import com.finderfeed.fdlib.util.FDByteBufCodecs;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;


@RegisterFDPacket("fdlib:position_screen_shake")
public class PositionedScreenShakePacket extends FDPacket {

    public FDShakeData data;
    public Vec3 pos;
    public double maxDistance;


    public PositionedScreenShakePacket(FDShakeData data, Vec3 pos,double maxDistance){
        this.data = data;
        this.pos = pos;
        this.maxDistance = maxDistance;
    }

    public PositionedScreenShakePacket(FriendlyByteBuf buf){
        this.data = FDShakeData.STREAM_CODEC.decode(buf);
        this.pos = FDByteBufCodecs.VEC3.decode(buf);
        this.maxDistance = buf.readDouble();
    }

    @Override
    public void write(RegistryFriendlyByteBuf buf) {
        FDShakeData.STREAM_CODEC.encode(buf,data);
        FDByteBufCodecs.VEC3.encode(buf,pos);
        buf.writeDouble(maxDistance);
    }

    @Override
    public void clientAction(IPayloadContext context) {
        ClientMixinHandler.addShake(new PositionedScreenShake(data,pos,maxDistance));
    }

    @Override
    public void serverAction(IPayloadContext context) {

    }

    public static void send(ServerLevel serverLevel,FDShakeData data,Vec3 pos,double radius){
        PacketDistributor.sendToPlayersNear(serverLevel,null,pos.x,pos.y,pos.z,radius,new PositionedScreenShakePacket(data,pos,radius));
    }

}
