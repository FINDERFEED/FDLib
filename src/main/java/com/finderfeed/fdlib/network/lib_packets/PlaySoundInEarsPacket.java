package com.finderfeed.fdlib.network.lib_packets;

import com.finderfeed.fdlib.FDClientPacketExecutables;
import com.finderfeed.fdlib.network.FDPacket;
import com.finderfeed.fdlib.network.RegisterFDPacket;
import com.finderfeed.fdlib.systems.stream_codecs.NetworkCodec;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;


@RegisterFDPacket("fdlib:playsound_in_ears")
public class PlaySoundInEarsPacket extends FDPacket {

    private SoundEvent event;
    private float pitch;
    private float volume;

    public PlaySoundInEarsPacket(SoundEvent event,float pitch,float volume){
        this.event = event;
        this.pitch = pitch;
        this.volume = volume;
    }

    public PlaySoundInEarsPacket(FriendlyByteBuf buf){
        this.event = NetworkCodec.SOUND_EVENT.fromNetwork(buf);
        this.pitch = buf.readFloat();
        this.volume = buf.readFloat();
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        NetworkCodec.SOUND_EVENT.toNetwork(buf,event);
        buf.writeFloat(this.pitch);
        buf.writeFloat(this.volume);
    }

    @Override
    public void clientAction(Supplier<NetworkEvent.Context> context) {
        FDClientPacketExecutables.playsoundInEars(event,pitch,volume);
    }

    @Override
    public void serverAction(Supplier<NetworkEvent.Context> context) {

    }
}
