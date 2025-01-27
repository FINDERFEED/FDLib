package com.finderfeed.fdlib.systems.hud.bossbars.packets;

import com.finderfeed.fdlib.network.FDPacket;
import com.finderfeed.fdlib.network.RegisterFDPacket;
import com.finderfeed.fdlib.systems.FDRegistries;
import com.finderfeed.fdlib.systems.hud.bossbars.FDBossBar;
import com.finderfeed.fdlib.systems.hud.bossbars.FDBossBarFactory;
import com.finderfeed.fdlib.systems.hud.bossbars.FDBossbars;
import com.finderfeed.fdlib.systems.hud.bossbars.FDServerBossBar;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.UUID;

@RegisterFDPacket("fdlib:add_player_to_boss_bar")
public class AddPlayerToBossBarPacket extends FDPacket {

    private ResourceLocation location;
    private int entityId = -1;
    private UUID uuid;
    private float percentage;

    public AddPlayerToBossBarPacket(FDServerBossBar serverBossBar){
        this.location = serverBossBar.getBossBarHolder().getId();
        this.entityId = serverBossBar.getEntity() != null ? serverBossBar.getEntity().getId() : -1;
        this.uuid = serverBossBar.getUUID();
        this.percentage = serverBossBar.getPercentage();
    }

    public AddPlayerToBossBarPacket(FriendlyByteBuf buf){
        this.location = buf.readResourceLocation();
        this.entityId = buf.readInt();
        this.uuid = buf.readUUID();
        this.percentage = buf.readFloat();
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeResourceLocation(location);
        buf.writeInt(this.entityId);
        buf.writeUUID(this.uuid);
        buf.writeFloat(this.percentage);
    }

    @Override
    public void clientAction(IPayloadContext context) {

        FDBossBarFactory<?> factory = FDRegistries.BOSS_BARS.get(location);
        if (factory == null) return;

        FDBossBar bossBar = factory.create(uuid,entityId);
        bossBar.setPercentage(percentage);
        FDBossbars.addBossBar(bossBar);

    }

    @Override
    public void serverAction(IPayloadContext context) {

    }
}
