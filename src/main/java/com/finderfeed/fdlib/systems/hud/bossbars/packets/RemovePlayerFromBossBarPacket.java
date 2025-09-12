package com.finderfeed.fdlib.systems.hud.bossbars.packets;

import com.finderfeed.fdlib.network.FDPacket;
import com.finderfeed.fdlib.network.RegisterFDPacket;
import com.finderfeed.fdlib.systems.hud.bossbars.FDBossbars;
import com.finderfeed.fdlib.systems.hud.bossbars.FDServerBossBar;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraftforge.neoforge.network.handling.IPayloadContext;

import java.util.UUID;

@RegisterFDPacket("fdlib:remove_player_from_boss_bar")
public class RemovePlayerFromBossBarPacket extends FDPacket {

    private UUID uuid;

    public RemovePlayerFromBossBarPacket(FDServerBossBar bossBar){
        this.uuid = bossBar.getUUID();
    }

    public RemovePlayerFromBossBarPacket(FriendlyByteBuf buf){
        this.uuid = buf.readUUID();
    }

    @Override
    public void write(RegistryFriendlyByteBuf buf) {
        buf.writeUUID(this.uuid);
    }

    @Override
    public void clientAction(IPayloadContext context) {
        FDBossbars.removeBossBar(uuid);
    }

    @Override
    public void serverAction(IPayloadContext context) {

    }
}
