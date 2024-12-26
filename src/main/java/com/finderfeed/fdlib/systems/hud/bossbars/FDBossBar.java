package com.finderfeed.fdlib.systems.hud.bossbars;

import java.util.UUID;

public abstract class FDBossBar {

    private UUID uuid;
    private int entityId;
    private float percentage;

    public FDBossBar(UUID uuid, int entityId){
        this.uuid = uuid;
        this.entityId = entityId;
    }

    public abstract void render();

    public abstract void tick();

    /**
     * Just to communicate with bossbar from server.
     */
    public abstract void hanldeBarEvent(int eventId,int data);


    public void setPercentage(float percentage) {
        this.percentage = percentage;
    }

    public float getPercentage() {
        return percentage;
    }

    public UUID getUUID() {
        return uuid;
    }

    /**
     * May return -1 which means no entity is bound
     */
    public int getEntityId() {
        return entityId;
    }
}
