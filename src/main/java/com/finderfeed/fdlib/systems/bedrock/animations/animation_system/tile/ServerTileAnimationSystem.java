package com.finderfeed.fdlib.systems.bedrock.animations.animation_system.tile;

import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.AnimationTicker;
import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.tile.packets.FreezeTileAnimationsPacket;
import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.tile.packets.StartTileAnimationPacket;
import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.tile.packets.StopTileAnimationPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.network.PacketDistributor;

public class ServerTileAnimationSystem extends TileAnimationSystem {

    public ServerTileAnimationSystem(BlockEntity tile) {
        super(tile);
    }

    @Override
    public void onAnimationStart(String name, AnimationTicker ticker) {
        Level level = this.getTile().getLevel();
        PacketDistributor.sendToPlayersTrackingChunk((ServerLevel) level,
                new ChunkPos(this.getTile().getBlockPos()),
                new StartTileAnimationPacket(this.getTile().getBlockPos(),name,ticker)
        );
    }

    @Override
    public void onAnimationStop(String name) {
        Level level = this.getTile().getLevel();
        PacketDistributor.sendToPlayersTrackingChunk((ServerLevel) level,
                new ChunkPos(this.getTile().getBlockPos()),
                new StopTileAnimationPacket(this.getTile().getBlockPos(),name)
        );
    }

    @Override
    public void onFreeze(boolean state) {
        Level level = this.getTile().getLevel();
        PacketDistributor.sendToPlayersTrackingChunk((ServerLevel) level,
                new ChunkPos(this.getTile().getBlockPos()),
                new FreezeTileAnimationsPacket(this.getTile().getBlockPos(),state)
        );
    }

    @Override
    public void onVariableAdded(String name, float variable) {

    }
}
