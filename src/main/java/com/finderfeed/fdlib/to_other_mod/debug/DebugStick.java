package com.finderfeed.fdlib.to_other_mod.debug;

import com.finderfeed.fdlib.to_other_mod.BossEntities;
import com.finderfeed.fdlib.to_other_mod.entities.radial_earthquake.RadialEarthquakeEntity;
import com.finderfeed.fdlib.to_other_mod.projectiles.ChesedBlockProjectile;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;

public class DebugStick extends Item {
    public DebugStick(Properties p_41383_) {
        super(p_41383_);
    }


    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {

        if (!level.isClientSide){

            RadialEarthquakeEntity radialEarthquakeEntity = RadialEarthquakeEntity.summon(level,player.getOnPos(),1,50,0.35f,10);
            //            ChesedBlockProjectile projectile = new ChesedBlockProjectile(BossEntities.BLOCK_PROJECTILE.get(),level);
//            projectile.setPos(player.position().add(player.getLookAngle()).add(0,2,0));
//            projectile.setDeltaMovement(player.getLookAngle().multiply(1,1,1));
//
//            projectile.setRotationSpeed(20);
//            projectile.setBlockState(Blocks.GRASS_BLOCK.defaultBlockState());
//            level.addFreshEntity(projectile);

//            FlyingBlockEntity block = new FlyingBlockEntity(FDEntities.FLYING_BLOCK.get(),level);
//            block.setPos(player.position().add(player.getLookAngle()).add(0,2,0));
//            block.setDeltaMovement(player.getLookAngle());
//            block.setRotationSpeed(20);
//            block.setBlockState(Blocks.STONE.defaultBlockState());
//            level.addFreshEntity(block);
        }

        return super.use(level, player, hand);
    }
}
