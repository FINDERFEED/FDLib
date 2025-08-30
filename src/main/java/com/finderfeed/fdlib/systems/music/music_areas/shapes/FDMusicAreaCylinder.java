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
        return FDTargetFinder.getEntitiesInCylinder(ServerPlayer.class, level, position, height, radius);
    }

}
