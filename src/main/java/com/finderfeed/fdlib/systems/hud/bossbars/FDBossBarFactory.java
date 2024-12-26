package com.finderfeed.fdlib.systems.hud.bossbars;

import java.util.UUID;

public interface FDBossBarFactory<T extends FDBossBar> {

    T create(UUID uuid,int entityId);

}
