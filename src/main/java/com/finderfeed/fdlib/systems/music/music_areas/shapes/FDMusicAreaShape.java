package com.finderfeed.fdlib.systems.music.music_areas.shapes;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public abstract class FDMusicAreaShape {

    public abstract List<ServerPlayer> getPlayersInside(ServerLevel level, Vec3 position);

}
