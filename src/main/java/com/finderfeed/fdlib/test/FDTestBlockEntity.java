package com.finderfeed.fdlib.test;

import com.finderfeed.fdlib.init.FDBlockEntities;
import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.tile.FDBlockEntity;
import com.finderfeed.fdlib.util.client.particles.ball_particle.BallParticleOptions;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class FDTestBlockEntity extends FDBlockEntity {

    public FDTestBlockEntity(BlockPos pos, BlockState state) {
        super(FDBlockEntities.TEST.get(), pos, state);
    }


    @Override
    public void tick() {
        super.tick();

        if (level.isClientSide && level.getGameTime() % 5 == 0){
            level.addParticle(BallParticleOptions.builder()
                            .color(1f,0.4f,0.1f)
                            .size(0.25f)
                            .brightness(30)
                            .scalingOptions(0,0,20)
                    .build(),true,this.getBlockPos().getX() + level.random.nextFloat(), this.getBlockPos().getY() + 2 + level.random.nextFloat(),this.getBlockPos().getZ() + level.random.nextFloat(), 0, 0, 0);

        }

    }
}
