package com.finderfeed.fdlib.systems.shake;

import com.finderfeed.fdlib.ClientMixinHandler;
import com.finderfeed.fdlib.network.FDPacket;
import com.finderfeed.fdlib.network.RegisterFDPacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;

@RegisterFDPacket("fdlib:default_shake")
public class DefaultShakePacket extends FDPacket {

    private FDShakeData data;

    public DefaultShakePacket(FDShakeData shakeData){
        this.data = shakeData;
    }

    public DefaultShakePacket(FriendlyByteBuf buf){
        this.data = FDShakeData.STREAM_CODEC.decode(buf);
    }


    @Override
    public void write(RegistryFriendlyByteBuf buf) {
        FDShakeData.STREAM_CODEC.encode(buf,data);
    }

    @Override
    public void clientAction(IPayloadContext context) {
        ClientMixinHandler.addShake(new DefaultShake(data));
    }

    @Override
    public void serverAction(IPayloadContext context) {

    }

    public static void send(ServerLevel level, Vec3 pos,double radius,FDShakeData data){
        PacketDistributor.sendToPlayersNear(level,null,pos.x,pos.y,pos.z,radius,new DefaultShakePacket(data));
    }

}
