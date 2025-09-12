package com.finderfeed.fdlib.systems.bedrock.animations.animation_system.tile;

import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.AnimationSystem;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.fml.util.thread.EffectiveSide;

public abstract class TileAnimationSystem extends AnimationSystem {

    private BlockEntity tile;

    protected TileAnimationSystem(BlockEntity tile){
        this.tile = tile;
    }

    public BlockEntity getTile() {
        return tile;
    }

    public static TileAnimationSystem create(BlockEntity tile){
        if (EffectiveSide.get().isClient()){
            return new ClientTileAnimationSystem(tile);
        }else{
            return new ServerTileAnimationSystem(tile);
        }
    }

}
