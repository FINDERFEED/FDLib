package com.finderfeed.fdlib.systems.music.packets;

import com.finderfeed.fdlib.network.FDPacket;
import com.finderfeed.fdlib.network.RegisterFDPacket;
import com.finderfeed.fdlib.systems.music.FDMusicSystem;
import com.finderfeed.fdlib.systems.music.data.FDMusicData;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;


import java.util.UUID;
import java.util.function.Supplier;

@RegisterFDPacket("fdlib:end_music_packet")
public class FDMusicEndPacket extends FDPacket {

    private UUID data;
    private int fadeOutTime;

    public FDMusicEndPacket(UUID uuid, int fadeOutTime){
        this.data = uuid;
        this.fadeOutTime = fadeOutTime;
    }

    public FDMusicEndPacket(FriendlyByteBuf buf){
        this.data = buf.readUUID();
        this.fadeOutTime = buf.readInt();
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeUUID(data);
        buf.writeInt(fadeOutTime);
    }

    @Override
    public void clientAction(Supplier<NetworkEvent.Context> context) {
        FDMusicSystem.endMusic(data, fadeOutTime);
    }

    @Override
    public void serverAction(Supplier<NetworkEvent.Context> context) {

    }
    
}
