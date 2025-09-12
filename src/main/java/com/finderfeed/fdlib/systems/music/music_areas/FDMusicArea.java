package com.finderfeed.fdlib.systems.music.music_areas;

import com.finderfeed.fdlib.network.FDPacketHandler;
import com.finderfeed.fdlib.systems.music.data.FDMusicData;
import com.finderfeed.fdlib.systems.music.music_areas.shapes.FDMusicAreaShape;
import com.finderfeed.fdlib.systems.music.packets.FDMusicAreaEnterPacket;
import com.finderfeed.fdlib.systems.music.packets.FDMusicEndPacket;
import com.finderfeed.fdlib.systems.music.packets.FDMusicFadeOutPacket;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.neoforge.network.PacketDistributor;
import net.minecraftforge.network.NetworkDirection;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public class FDMusicArea {

    private List<UUID> playersInside = new ArrayList<>();

    protected UUID uuid;
    private Vec3 position;
    private ResourceKey<Level> dimension;
    private FDMusicAreaShape shape;
    private FDMusicData musicData;
    private int autoDeletionTicker = 2 * 60 * 20; //2 minutes, then deletes itself, can be set manually
    private int maxDeletionTicker = autoDeletionTicker;
    private int playerDetectionFrequency = 5;

    public FDMusicArea(ResourceKey<Level> dimension, Vec3 position, FDMusicAreaShape shape, FDMusicData musicData){
        this.dimension = dimension;
        this.position = position;
        this.shape = shape;
        this.musicData = musicData;
    }

    public void tick(ServerLevel level){
        if (level.getGameTime() % playerDetectionFrequency == 0) {

            var playersInside = this.shape.getPlayersInside(level, position);
            var uuidsInside = playersInside.stream().map(ServerPlayer::getUUID).toList();


            for (var serverPlayer : playersInside){
                UUID uuid = serverPlayer.getUUID();
                if (!this.playersInside.contains(uuid)){
                    FDPacketHandler.INSTANCE.sendTo(new FDMusicAreaEnterPacket(this.musicData,20), serverPlayer.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
                    this.playersInside.add(uuid);
                }
            }




            List<UUID> playersThatExitedTheArea = new ArrayList<>(this.playersInside);

            playersThatExitedTheArea.removeAll(uuidsInside);
            this.playersInside.removeAll(playersThatExitedTheArea);

            for (var uuid : playersThatExitedTheArea){
                ServerPlayer serverPlayer = level.getServer().getPlayerList().getPlayer(uuid);
                if (serverPlayer == null) continue;

                FDPacketHandler.INSTANCE.sendTo(new FDMusicFadeOutPacket(this.musicData.getMusicSourceUUID(), 20), serverPlayer.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
            }


        }
    }

    public void onRemoval(MinecraftServer server, int fadeOutTime){
        for (var player : playersInside){
            var actualPlayer = server.getPlayerList().getPlayer(player);
            if (actualPlayer != null){
                FDPacketHandler.INSTANCE.sendTo(new FDMusicEndPacket(this.musicData.getMusicSourceUUID(), fadeOutTime), actualPlayer.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
            }
        }
    }

    protected void tickDeletionTicker(boolean ignorePlayersInside){
        if (playersInside.isEmpty() || ignorePlayersInside){
            autoDeletionTicker--;
        }else{
            autoDeletionTicker = maxDeletionTicker;
        }
    }

    public void setAutoDeletionTicker(int autoDeletionTicker) {
        this.autoDeletionTicker = autoDeletionTicker;
        this.maxDeletionTicker = autoDeletionTicker;
    }

    public boolean shouldBeDeleted(){
        return autoDeletionTicker < 0;
    }

    public void setPlayerDetectionFrequency(int playerDetectionFrequency) {
        this.playerDetectionFrequency = playerDetectionFrequency;
    }

    public UUID getUUID() {
        return uuid;
    }

    public FDMusicAreaShape getShape() {
        return shape;
    }

    public ResourceKey<Level> getDimension() {
        return dimension;
    }

    public Vec3 getPosition() {
        return position;
    }

    public FDMusicData getMusicData() {
        return musicData;
    }

    public void setMusicData(FDMusicData musicData) {
        this.musicData = musicData;
    }

}
