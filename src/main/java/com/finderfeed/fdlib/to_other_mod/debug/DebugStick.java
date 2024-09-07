package com.finderfeed.fdlib.to_other_mod.debug;

import com.finderfeed.fdlib.to_other_mod.earthshatter_entity.EarthShatterEntity;
import com.finderfeed.fdlib.to_other_mod.earthshatter_entity.EarthShatterSettings;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class DebugStick extends Item {
    public DebugStick(Properties p_41383_) {
        super(p_41383_);
    }


    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {

        if (!level.isClientSide){
            EarthShatterEntity.summon(level,player.getOnPos(),
                    EarthShatterSettings.builder()
                            .upTime(4)
                            .stayTime(2)
                            .downTime(4)
                            .upDistance(0.5f)
                            .direction(1,1,0)
                            .build()
            );
        }

        return super.use(level, player, hand);
    }
}
