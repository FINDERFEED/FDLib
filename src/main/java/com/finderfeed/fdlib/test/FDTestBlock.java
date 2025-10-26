package com.finderfeed.fdlib.test;

import com.finderfeed.fdlib.init.FDBlockEntities;
import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.tile.FDEntityBlock;
import com.finderfeed.fdlib.systems.broken_screen_effect.ShatteredScreenEffectHandler;
import com.finderfeed.fdlib.systems.broken_screen_effect.ShatteredScreenSettings;
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

    private static boolean testBoolean = true;

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult p_60508_) {
       if (!level.isClientSide){
           UUID uuid = UUID.fromString("5c6cd8c0-7e3e-44a3-9c2e-2459a61377f3");
//
//           if (!player.isCrouching()){
//               FDMusicData fdMusicData = new FDMusicData(uuid,
//                       new FDMusicPartData(FDSounds.MALKUTH_THEME_INTRO_TEST.get(), 14.75f))
//                       .addMusicPart(new FDMusicPartData(FDSounds.MALKUTH_THEME_MAIN_TEST.get(), 103.375f)
//                               .setLooping(true))
//                       .fadeInTime(80)
//                       .inactiveDeleteTime(600);
//               FDMusicAreasHandler.addArea(uuid, new FDMusicArea(level.dimension(), pos.getCenter(), new FDMusicAreaCylinder(10,10),fdMusicData));
//           }else{
//               FDMusicAreasHandler.removeArea(((ServerLevel)level).getServer(),uuid);
//           }


//           if (!player.isCrouching()) {
//               FDMusicData fdMusicData = new FDMusicData(uuid,
//                       new FDMusicPartData(FDSounds.MALKUTH_THEME_INTRO_TEST.get(), 14.75f))
//                       .addMusicPart(new FDMusicPartData(FDSounds.MALKUTH_THEME_MAIN_TEST.get(), 103.375f)
//                               .setLooping(true))
//                       .inactiveDeleteTime(60)
//                       .fadeInTime(80);
//
//               FDMusicSystem.addMusic(fdMusicData);
//           }else{
//               FDMusic fdMusic = FDMusicSystem.getMusic(uuid);
//               if (fdMusic != null){
//                   if (testBoolean){
//                       fdMusic.fadeOut(50,false);
//                   }else{
//                       fdMusic.fadeIn(50);
//                   }
//                   testBoolean = !testBoolean;
//               }
//           }
       }else{
           ShatteredScreenEffectHandler.setCurrentEffect(new ShatteredScreenSettings(
                   ShatteredScreenSettings.DATA_1_GLASSY,
                   1,
                   1000,
                   50,
                   0.3f,
                   true
           ));
       }
        return super.useWithoutItem(state, level, pos, player, p_60508_);
    }

    @Override
    protected RenderShape getRenderShape(BlockState p_60550_) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }
}
