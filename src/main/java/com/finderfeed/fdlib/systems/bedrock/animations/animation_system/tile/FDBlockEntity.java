package com.finderfeed.fdlib.systems.bedrock.animations.animation_system.tile;

import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.AnimatedObject;
import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.AnimationSystem;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class FDBlockEntity extends BlockEntity implements AnimatedObject {

    private AnimationSystem system;

    /**
     * If FDEntityBlock is used - constructor should only have pos and blockstate as arguments!
     */
    public FDBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        this.system = TileAnimationSystem.create(this);
    }

    public static void tick(BlockEntity entity){
        ((FDBlockEntity)entity).tick();
    }

    public void tick(){
        this.tickAnimationSystem();
    }

    @Override
    public void onLoad() {
        super.onLoad();
    }

    @Override
    public AnimationSystem getSystem() {
        return system;
    }
}
