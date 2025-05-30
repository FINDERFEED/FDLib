package com.finderfeed.fdlib.systems.hud.bossbars.packets;

import com.finderfeed.fdlib.network.FDPacket;
import com.finderfeed.fdlib.network.RegisterFDPacket;
import com.finderfeed.fdlib.systems.hud.bossbars.FDBossBar;
import com.finderfeed.fdlib.systems.hud.bossbars.FDBossbars;
import com.finderfeed.fdlib.systems.hud.bossbars.FDServerBossBar;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.UUID;


@RegisterFDPacket("fdlib:set_boss_bar_progress")
public class SetBossBarProgressPacket extends FDPacket {

    private UUID uuid;
    private float percentage;

    public SetBossBarProgressPacket(FDServerBossBar serverBossBar,float percentage){
        this.uuid = serverBossBar.getUUID();
        this.percentage = percentage;
    }

    public SetBossBarProgressPacket(FriendlyByteBuf buf){
        this.uuid = buf.readUUID();
        this.percentage = buf.readFloat();
    }

    @Override
    public void write(RegistryFriendlyByteBuf buf) {
        buf.writeUUID(this.uuid);
        buf.writeFloat(this.percentage);
    }

    @Override
    public void clientAction(IPayloadContext context) {
        FDBossBar bossBar = FDBossbars.getBossBar(uuid);
        if (bossBar != null){
            bossBar.setPercentage(percentage);
        }
    }

    @Override
    public void serverAction(IPayloadContext context) {

    }
}
