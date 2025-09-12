package com.finderfeed.fdlib.systems.shake;

import com.finderfeed.fdlib.ClientMixinHandler;
import com.finderfeed.fdlib.network.FDPacket;
import com.finderfeed.fdlib.network.RegisterFDPacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.neoforge.network.PacketDistributor;
import net.minecraftforge.neoforge.network.handling.Supplier<NetworkEvent.Context>;

@RegisterFDPacket("fdlib:default_shake")
public class DefaultShakePacket extends FDPacket {

    private FDShakeData data;

    public DefaultShakePacket(FDShakeData shakeData){
        this.data = shakeData;
    }

    public DefaultShakePacket(FriendlyByteBuf buf){
        this.data = FDShakeData.STREAM_CODEC.fromNetwork(buf);
    }


    @Override
    public void write(FriendlyByteBuf buf) {
        FDShakeData.STREAM_CODEC.toNetwork(buf,data);
    }

    @Override
    public void clientAction(Supplier<NetworkEvent.Context> context) {
        ClientMixinHandler.addShake(new DefaultShake(data));
    }

    @Override
    public void serverAction(Supplier<NetworkEvent.Context> context) {

    }

    public static void send(ServerLevel level, Vec3 pos,double radius,FDShakeData data){
        PacketDistributor.sendToPlayersNear(level,null,pos.x,pos.y,pos.z,radius,new DefaultShakePacket(data));
    }

}
