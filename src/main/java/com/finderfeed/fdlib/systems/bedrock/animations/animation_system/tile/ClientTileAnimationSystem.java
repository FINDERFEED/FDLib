package com.finderfeed.fdlib.systems.bedrock.animations.animation_system.tile;

import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.AnimationTicker;
import net.minecraft.world.level.block.entity.BlockEntity;

public class ClientTileAnimationSystem extends TileAnimationSystem{

    public ClientTileAnimationSystem(BlockEntity tile) {
        super(tile);
    }

    @Override
    public void onAnimationStart(String name, AnimationTicker ticker) {

    }

    @Override
    public void onAnimationStop(String name) {

    }

    @Override
    public void onFreeze(boolean state) {

    }
}
