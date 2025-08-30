package com.finderfeed.fdlib.systems.music.packets;

import com.finderfeed.fdlib.network.FDPacket;
import com.finderfeed.fdlib.network.RegisterFDPacket;
import com.finderfeed.fdlib.systems.music.FDMusic;
import com.finderfeed.fdlib.systems.music.FDMusicSystem;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.UUID;

@RegisterFDPacket("fdlib:fade_out_music_packet")
public class FDMusicFadeOutPacket extends FDPacket {

    private UUID data;
    private int fadeOutTime;

    public FDMusicFadeOutPacket(UUID uuid, int fadeOutTime){
        this.data = uuid;
        this.fadeOutTime = fadeOutTime;
    }

    public FDMusicFadeOutPacket(RegistryFriendlyByteBuf buf){
        this.data = buf.readUUID();
        this.fadeOutTime = buf.readInt();
    }

    @Override
    public void write(RegistryFriendlyByteBuf buf) {
        buf.writeUUID(data);
        buf.writeInt(fadeOutTime);
    }

    @Override
    public void clientAction(IPayloadContext context) {
        FDMusic music = FDMusicSystem.getMusic(data);
        if (music != null){
            music.fadeOut(fadeOutTime, false);
        }
    }

    @Override
    public void serverAction(IPayloadContext context) {

    }
    
}
