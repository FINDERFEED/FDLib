package com.finderfeed.fdlib.systems.particle.particle_emitter;

import com.finderfeed.fdlib.network.FDPacket;
import com.finderfeed.fdlib.network.RegisterFDPacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.neoforged.neoforge.network.handling.IPayloadContext;

@RegisterFDPacket("fdlib:emitter_packet")
public class ParticleEmitterPacket extends FDPacket {

    private ParticleEmitterData data;

    public ParticleEmitterPacket(ParticleEmitterData data){
        this.data = data;
    }

    public ParticleEmitterPacket(FriendlyByteBuf buf){
        this.data = ParticleEmitterData.STREAM_CODEC.decode(buf);
    }

    @Override
    public void write(RegistryFriendlyByteBuf buf) {
        ParticleEmitterData.STREAM_CODEC.encode(buf,data);
    }

    @Override
    public void clientAction(IPayloadContext context) {
        ParticleEmitterHandler.addParticleEmitter(data);
    }

    @Override
    public void serverAction(IPayloadContext context) {

    }
}
