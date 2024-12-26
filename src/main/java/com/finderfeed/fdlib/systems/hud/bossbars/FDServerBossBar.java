package com.finderfeed.fdlib.systems.hud.bossbars;

import com.finderfeed.fdlib.systems.hud.bossbars.packets.AddPlayerToBossBarPacket;
import com.finderfeed.fdlib.systems.hud.bossbars.packets.BossBarEventPacket;
import com.finderfeed.fdlib.systems.hud.bossbars.packets.RemovePlayerFromBossBarPacket;
import com.finderfeed.fdlib.systems.hud.bossbars.packets.SetBossBarProgressPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class FDServerBossBar {

    private DeferredHolder<FDBossBarFactory<?>,FDBossBarFactory<?>> bossBarHolder;
    private UUID uuid;
    private Entity entity;
    private Set<ServerPlayer> players = new HashSet<>();
    private float percentage = 0;

    /**
     * If entity context is needed on client
     */
    public FDServerBossBar(DeferredHolder<FDBossBarFactory<?>,FDBossBarFactory<?>> holder,Entity entity){
        this.uuid = entity.getUUID();
        this.entity = entity;
        this.bossBarHolder = holder;
    }

    public FDServerBossBar(DeferredHolder<FDBossBarFactory<?>,FDBossBarFactory<?>> holder){
        this.uuid = UUID.randomUUID();
        this.entity = null;
        this.bossBarHolder = holder;
    }

    public void setPercentage(float percentage){
        this.percentage = percentage;
        for (ServerPlayer player : players){
            PacketDistributor.sendToPlayer(player,new SetBossBarProgressPacket(this,percentage));
        }
    }

    public void addPlayer(ServerPlayer player){
        this.players.add(player);
        PacketDistributor.sendToPlayer(player,new AddPlayerToBossBarPacket(this));
    }

    public void removePlayer(ServerPlayer player){
        this.players.remove(player);
        PacketDistributor.sendToPlayer(player,new RemovePlayerFromBossBarPacket(this));
    }

    public void broadcastEvent(int eventId,int data){
        for (ServerPlayer player : players){
            PacketDistributor.sendToPlayer(player,new BossBarEventPacket(this,eventId,data));
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

    public DeferredHolder<FDBossBarFactory<?>, FDBossBarFactory<?>> getBossBarHolder() {
        return bossBarHolder;
    }
}
