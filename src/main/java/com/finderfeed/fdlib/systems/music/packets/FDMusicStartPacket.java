package com.finderfeed.fdlib.systems.music.packets;

import com.finderfeed.fdlib.network.FDPacket;
import com.finderfeed.fdlib.network.RegisterFDPacket;
import com.finderfeed.fdlib.systems.music.FDMusicSystem;
import com.finderfeed.fdlib.systems.music.data.FDMusicData;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.neoforge.network.handling.Supplier<NetworkEvent.Context>;

@RegisterFDPacket("fdlib:start_music_packet")
public class FDMusicStartPacket extends FDPacket {

    private FDMusicData data;

    public FDMusicStartPacket(FDMusicData musicData){
        this.data = musicData;
    }

    public FDMusicStartPacket(FriendlyByteBuf buf){
        this.data = FDMusicData.STREAM_CODEC.fromNetwork(buf);
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        FDMusicData.STREAM_CODEC.toNetwork(buf, data);
    }

    @Override
    public void clientAction(Supplier<NetworkEvent.Context> context) {
        FDMusicSystem.addMusic(data);
    }

    @Override
    public void serverAction(Supplier<NetworkEvent.Context> context) {

    }

}
