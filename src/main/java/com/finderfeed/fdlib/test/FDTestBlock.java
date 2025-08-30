package com.finderfeed.fdlib.test;

import com.finderfeed.fdlib.init.FDBlockEntities;
import com.finderfeed.fdlib.init.FDSounds;
import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.tile.FDEntityBlock;
import com.finderfeed.fdlib.systems.music.FDMusic;
import com.finderfeed.fdlib.systems.music.FDMusicSystem;
import com.finderfeed.fdlib.systems.music.data.FDMusicData;
import com.finderfeed.fdlib.systems.music.data.FDMusicPartData;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

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
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult p_60508_) {
       if (level.isClientSide){
           UUID uuid = UUID.fromString("5c6cd8c0-7e3e-44a3-9c2e-2459a61377f4");
           if (!player.isCrouching()) {
               FDMusicData fdMusicData = new FDMusicData(uuid,
                       new FDMusicPartData(FDSounds.MALKUTH_THEME_INTRO_TEST.get(), 14.75f)
                               .setDefaultFadeIn(80))
                       .addMusicPart(new FDMusicPartData(FDSounds.MALKUTH_THEME_MAIN_TEST.get(), 103.375f)
                               .setLooping(true));

               FDMusicSystem.addMusic(fdMusicData);
           }else{
               FDMusicSystem.endMusic(uuid, 100);
           }
       }
        return super.useWithoutItem(state, level, pos, player, p_60508_);
    }

    @Override
    protected RenderShape getRenderShape(BlockState p_60550_) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }
}
