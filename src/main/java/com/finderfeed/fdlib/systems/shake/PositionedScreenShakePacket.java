package com.finderfeed.fdlib.systems.shake;

import com.finderfeed.fdlib.ClientMixinHandler;
import com.finderfeed.fdlib.network.FDPacket;
import com.finderfeed.fdlib.network.FDPacketHandler;
import com.finderfeed.fdlib.network.RegisterFDPacket;
import com.finderfeed.fdlib.systems.stream_codecs.NetworkCodec;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;

import java.util.function.Supplier;


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
        this.data = FDShakeData.STREAM_CODEC.fromNetwork(buf);
        this.pos = NetworkCodec.VEC3.fromNetwork(buf);
        this.maxDistance = buf.readDouble();
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        FDShakeData.STREAM_CODEC.toNetwork(buf,data);
        NetworkCodec.VEC3.toNetwork(buf,pos);
        buf.writeDouble(maxDistance);
    }

    @Override
    public void clientAction(Supplier<NetworkEvent.Context> context) {
        ClientMixinHandler.addShake(new PositionedScreenShake(data,pos,maxDistance));
    }

    @Override
    public void serverAction(Supplier<NetworkEvent.Context> context) {

    }

    public static void send(ServerLevel serverLevel,FDShakeData data,Vec3 pos,double radius){
        FDPacketHandler.INSTANCE.send(PacketDistributor.NEAR.with(PacketDistributor.TargetPoint.p(pos.x,pos.y,pos.z,radius,serverLevel.dimension())),new PositionedScreenShakePacket(data,pos,radius));
    }

}
