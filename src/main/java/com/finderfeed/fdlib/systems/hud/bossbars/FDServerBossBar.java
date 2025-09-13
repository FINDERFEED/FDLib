package com.finderfeed.fdlib.systems.hud.bossbars;

import com.finderfeed.fdlib.network.FDPacketHandler;
import com.finderfeed.fdlib.systems.hud.bossbars.packets.AddPlayerToBossBarPacket;
import com.finderfeed.fdlib.systems.hud.bossbars.packets.BossBarEventPacket;
import com.finderfeed.fdlib.systems.hud.bossbars.packets.RemovePlayerFromBossBarPacket;
import com.finderfeed.fdlib.systems.hud.bossbars.packets.SetBossBarProgressPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.registries.RegistryObject;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class FDServerBossBar {

    private RegistryObject<FDBossBarFactory<?>> bossBarHolder;
    private UUID uuid;
    private Entity entity;
    private Set<ServerPlayer> players = new HashSet<>();
    private float percentage = 0;

    /**
     * If entity context is needed on client
     */
    public FDServerBossBar(RegistryObject<FDBossBarFactory<?>> holder,Entity entity){
        this.uuid = entity.getUUID();
        this.entity = entity;
        this.bossBarHolder = holder;
    }

    public FDServerBossBar(RegistryObject<FDBossBarFactory<?>> holder){
        this.uuid = UUID.randomUUID();
        this.entity = null;
        this.bossBarHolder = holder;
    }

    public void setPercentage(float percentage){
        this.percentage = percentage;
        for (ServerPlayer player : players){
            FDPacketHandler.INSTANCE.sendTo(new SetBossBarProgressPacket(this, percentage), player.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
        }
    }

    public void addPlayer(ServerPlayer player){
        this.players.add(player);
        FDPacketHandler.INSTANCE.sendTo(new AddPlayerToBossBarPacket(this), player.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
    }

    public void removePlayer(ServerPlayer player){
        this.players.remove(player);
        FDPacketHandler.INSTANCE.sendTo(new RemovePlayerFromBossBarPacket(this), player.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
    }

    public void broadcastEvent(int eventId,int data){
        for (ServerPlayer player : players){
            FDPacketHandler.INSTANCE.sendTo(new BossBarEventPacket(this,eventId,data), player.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
        }
    }

    public UUID getUUID() {
        return uuid;
    }

    public Entity getEntity() {
        return entity;
    }

    public float getPercentage() {
        return percentage;
    }

    public RegistryObject<FDBossBarFactory<?>> getBossBarHolder() {
        return bossBarHolder;
    }
}
