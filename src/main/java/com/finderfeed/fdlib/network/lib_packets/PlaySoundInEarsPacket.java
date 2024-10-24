package com.finderfeed.fdlib.network.lib_packets;

import com.finderfeed.fdlib.FDClientPacketExecutables;
import com.finderfeed.fdlib.network.FDPacket;
import com.finderfeed.fdlib.network.RegisterFDPacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;

@RegisterFDPacket("fdlib:playsound_in_ears")
public class PlaySoundInEarsPacket extends FDPacket {

    private SoundEvent event;

    public PlaySoundInEarsPacket(SoundEvent event){
        this.event = event;
    }

    public PlaySoundInEarsPacket(FriendlyByteBuf buf){
        this.event = SoundEvent.DIRECT_STREAM_CODEC.decode(buf);
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        SoundEvent.DIRECT_STREAM_CODEC.encode(buf,event);
    }

    @Override
    public void clientAction(IPayloadContext context) {
        FDClientPacketExecutables.playsoundInEars(event);
    }

    @Override
    public void serverAction(IPayloadContext context) {

    }
}
