package com.finderfeed.fdlib.systems.test;

import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.entity.FDLivingEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public class FDTestEntity extends FDLivingEntity {

    public FDTestEntity(EntityType<? extends LivingEntity> type, Level level) {
        super(type, level);
    }

}
