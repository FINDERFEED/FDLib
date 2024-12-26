package com.finderfeed.fdlib.systems.hud.bossbars.packets;


import com.finderfeed.fdlib.network.FDPacket;
import com.finderfeed.fdlib.network.RegisterFDPacket;
import com.finderfeed.fdlib.systems.hud.bossbars.FDBossBar;
import com.finderfeed.fdlib.systems.hud.bossbars.FDBossbars;
import com.finderfeed.fdlib.systems.hud.bossbars.FDServerBossBar;
import net.minecraft.network.FriendlyByteBuf;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.UUID;

@RegisterFDPacket("fdlib:set_boss_bar_progress")
public class BossBarEventPacket extends FDPacket {

    private UUID uuid;
    private int event;
    private int data;


    public BossBarEventPacket(FDServerBossBar serverBossBar, int event,int data){
        this.uuid = serverBossBar.getUUID();
        this.event = event;
        this.data = data;
    }

    public BossBarEventPacket(FriendlyByteBuf buf){
        this.uuid = buf.readUUID();
        this.event = buf.readInt();
        this.data = buf.readInt();
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeUUID(this.uuid);
        buf.writeInt(this.event);
        buf.writeInt(this.data);
    }

    @Override
    public void clientAction(IPayloadContext context) {
        FDBossBar bossBar = FDBossbars.getBossBar(uuid);
        if (bossBar != null){
            bossBar.hanldeBarEvent(event,data);
        }
    }

    @Override
    public void serverAction(IPayloadContext context) {

    }
}