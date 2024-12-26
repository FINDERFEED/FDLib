package com.finderfeed.fdlib.to_other_mod.entities.chesed_boss;

import com.finderfeed.fdlib.systems.hud.bossbars.FDBossBar;
import com.mojang.blaze3d.vertex.PoseStack;

import java.util.UUID;

public class ChesedBossBar extends FDBossBar {

    public ChesedBossBar(UUID uuid, int entityId) {
        super(uuid, entityId);
    }

    @Override
    public float render(PoseStack matrices, float partialTicks) {
        return 0;
    }


    @Override
    public void tick() {

    }

    @Override
    public void hanldeBarEvent(int eventId, int data) {

    }
}
