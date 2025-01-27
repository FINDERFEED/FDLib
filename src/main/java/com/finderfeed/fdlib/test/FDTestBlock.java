package com.finderfeed.fdlib.test;

import com.finderfeed.fdlib.init.FDBlockEntities;
import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.tile.FDEntityBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class FDTestBlock extends FDEntityBlock {

    public FDTestBlock(Properties props) {
        super(props);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos p_153215_, BlockState p_153216_) {
        return FDBlockEntities.TEST.get().create(p_153215_,p_153216_);
    }

    @Override
    protected RenderShape getRenderShape(BlockState p_60550_) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }
}
