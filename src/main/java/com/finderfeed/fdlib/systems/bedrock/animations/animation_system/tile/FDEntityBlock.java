package com.finderfeed.fdlib.systems.bedrock.animations.animation_system.tile;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public abstract class FDEntityBlock extends Block implements EntityBlock {

    public FDEntityBlock(Properties props) {
        super(props);
    }


    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {

        if (!level.isClientSide && this.shouldTickServerside(level,state,type)){
            return (lv, pos, blockstate, tile)->{
                ((FDBlockEntity)tile).tick();
            };
        }

        if (level.isClientSide && this.shouldTickClientside(level,state,type)){
            return (lv, pos, blockstate, tile)->{
                ((FDBlockEntity)tile).tick();
            };
        }

        return null;
    }



    public <T extends BlockEntity> boolean shouldTickServerside(Level level, BlockState state, BlockEntityType<T> type){
        return true;
    }

    public <T extends BlockEntity> boolean shouldTickClientside(Level level, BlockState state, BlockEntityType<T> type){
        return true;
    }

}
