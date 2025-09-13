package com.finderfeed.fdlib.systems.music.packets;

import com.finderfeed.fdlib.network.FDPacket;
import com.finderfeed.fdlib.network.RegisterFDPacket;
import com.finderfeed.fdlib.systems.music.FDMusic;
import com.finderfeed.fdlib.systems.music.FDMusicSystem;
import com.finderfeed.fdlib.systems.music.data.FDMusicData;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;


@RegisterFDPacket("fdlib:enter_music_area_packet")
public class FDMusicAreaEnterPacket extends FDPacket {

    private FDMusicData data;
    private int fadeInTime;

    public FDMusicAreaEnterPacket(FDMusicData musicData, int fadeInTime){
        this.data = musicData;
        this.fadeInTime = fadeInTime;
    }

    public FDMusicAreaEnterPacket(FriendlyByteBuf buf){
        this.data = FDMusicData.STREAM_CODEC.fromNetwork(buf);
        this.fadeInTime = buf.readInt();
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        FDMusicData.STREAM_CODEC.toNetwork(buf, data);
        buf.writeInt(fadeInTime);
    }

    @Override
    public void clientAction(Supplier<NetworkEvent.Context> context) {
        FDMusic music;
        if ((music = FDMusicSystem.getMusic(data.getMusicSourceUUID())) == null) {
            FDMusicSystem.addMusic(data);
        }else{
            music.fadeIn(fadeInTime);
        }
    }

    @Override
    public void serverAction(Supplier<NetworkEvent.Context> context) {

    }

}
