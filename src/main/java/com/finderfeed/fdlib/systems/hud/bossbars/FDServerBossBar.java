package com.finderfeed.fdlib.systems.hud.bossbars;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class FDServerBossBar {

    private UUID uuid;
    private Entity entity;
    private Set<ServerPlayer> players = new HashSet<>();
    private float percentage = 0;

    /**
     * If entity context is needed on client
     */
    public FDServerBossBar(Entity entity){
        this.uuid = entity.getUUID();
        this.entity = entity;
    }

    public FDServerBossBar(){
        this.uuid = UUID.randomUUID();
        this.entity = null;
    }

    public void setPercentage(float percentage){
        this.percentage = percentage;
    }

    public void addPlayer(ServerPlayer player){

    }

    public void removePlayer(ServerPlayer player){

    }

    public void broadcastEvent(int eventId,int data){

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
}
