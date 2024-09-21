package com.finderfeed.fdlib.to_other_mod.debug;

import com.finderfeed.fdlib.to_other_mod.FDEntities;
import com.finderfeed.fdlib.to_other_mod.entities.earthshatter_entity.EarthShatterEntity;
import com.finderfeed.fdlib.to_other_mod.entities.earthshatter_entity.EarthShatterSettings;
import com.finderfeed.fdlib.to_other_mod.projectiles.BlockProjectile;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;

public class DebugStick extends Item {
    public DebugStick(Properties p_41383_) {
        super(p_41383_);
    }


    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {

        if (!level.isClientSide){
            BlockProjectile projectile = new BlockProjectile(FDEntities.BLOCK_PROJECTILE.get(),level);
            projectile.setPos(player.position().add(player.getLookAngle()).add(0,2,0));
            projectile.setDeltaMovement(player.getLookAngle().multiply(0.1,0.1,0.1));

            projectile.setRotationSpeed(30);
            projectile.setBlockState(Blocks.GRASS_BLOCK.defaultBlockState());
            level.addFreshEntity(projectile);
        }

        return super.use(level, player, hand);
    }
}
