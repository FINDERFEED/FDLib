package com.finderfeed.fdlib.systems.music.packets;

import com.finderfeed.fdlib.network.FDPacket;
import com.finderfeed.fdlib.network.RegisterFDPacket;
import com.finderfeed.fdlib.systems.music.FDMusicSystem;
import com.finderfeed.fdlib.systems.music.data.FDMusicData;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.neoforged.neoforge.network.handling.IPayloadContext;

@RegisterFDPacket("fdlib:start_music_packet")
public class FDMusicStartPacket extends FDPacket {

    private FDMusicData data;

    public FDMusicStartPacket(FDMusicData musicData){
        this.data = musicData;
    }

    public FDMusicStartPacket(RegistryFriendlyByteBuf buf){
        this.data = FDMusicData.STREAM_CODEC.decode(buf);
    }

    @Override
    public void write(RegistryFriendlyByteBuf buf) {
        FDMusicData.STREAM_CODEC.encode(buf, data);
    }

    @Override
    public void clientAction(IPayloadContext context) {
        FDMusicSystem.addMusic(data);
    }

    @Override
    public void serverAction(IPayloadContext context) {

    }

}
