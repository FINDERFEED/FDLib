package com.finderfeed.fdlib.test;

import com.finderfeed.fdlib.init.FDBlockEntities;
import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.tile.FDBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class FDTestBlockEntity extends FDBlockEntity {

    public FDTestBlockEntity(BlockPos pos, BlockState state) {
        super(FDBlockEntities.TEST.get(), pos, state);
    }
}
