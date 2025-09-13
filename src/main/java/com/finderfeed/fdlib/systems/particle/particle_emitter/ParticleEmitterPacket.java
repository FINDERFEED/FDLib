package com.finderfeed.fdlib.systems.particle.particle_emitter;

import com.finderfeed.fdlib.network.FDPacket;
import com.finderfeed.fdlib.network.RegisterFDPacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;


@RegisterFDPacket("fdlib:emitter_packet")
public class ParticleEmitterPacket extends FDPacket {

    private ParticleEmitterData data;

    public ParticleEmitterPacket(ParticleEmitterData data){
        this.data = data;
    }

    public ParticleEmitterPacket(FriendlyByteBuf buf){
        this.data = ParticleEmitterData.STREAM_CODEC.fromNetwork(buf);
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        ParticleEmitterData.STREAM_CODEC.toNetwork(buf,data);
    }

    @Override
    public void clientAction(Supplier<NetworkEvent.Context> context) {
        ParticleEmitterHandler.addParticleEmitter(data);
    }

    @Override
    public void serverAction(Supplier<NetworkEvent.Context> context) {

    }
}
