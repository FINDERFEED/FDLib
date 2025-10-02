package com.finderfeed.fdlib.systems.music.music_areas.shapes;

import com.finderfeed.fdlib.util.FDTargetFinder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class FDMusicAreaCylinder extends FDMusicAreaShape {

    private float radius;
    private float height;

    public FDMusicAreaCylinder(float radius, float height){
        this.radius = radius;
        this.height = height;
    }

    @Override
    public List<ServerPlayer> getPlayersInside(ServerLevel level, Vec3 position) {

        var playerList = level.getServer().getPlayerList();

        var players = playerList.getPlayers().stream().filter(player->{
            return FDTargetFinder.isPointInCylinder(player.position(), position, height, radius) && player.level().dimension().equals(level.dimension());
        }).toList();

        return players;
    }

}
