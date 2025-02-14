package com.finderfeed.fdlib.systems.hud.bossbars;

import com.finderfeed.fdlib.FDClientHelpers;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

import java.util.UUID;

public abstract class FDBossBar {

    private UUID uuid;
    private int entityId;
    private float percentage;

    public FDBossBar(UUID uuid, int entityId){
        this.uuid = uuid;
        this.entityId = entityId;
    }

    /**
     * Return the height of boss bar
     */
    public abstract void render(GuiGraphics graphics, float partialTicks);

    public abstract void tick(float topOffset);

    public abstract float height();

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

    public <T extends Entity> T getEntity() {
        Level level = FDClientHelpers.getClientLevel();
        if (level == null) return null;

        Entity entity = level.getEntity(this.entityId);
        if (entity == null) return null;

        return (T)entity;
    }
}
