package com.finderfeed.fdlib.systems.test;

import com.finderfeed.fdlib.init.FDAnimations;
import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.AnimationTicker;
import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.entity.FDLivingEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public class ChesedEntity extends FDLivingEntity {

    public ChesedEntity(EntityType<? extends LivingEntity> type, Level level) {
        super(type, level);
    }


    @Override
    public void tick() {
        super.tick();
        if (!this.level().isClientSide){
            this.getSystem().startAnimation("IDLE", new AnimationTicker(FDAnimations.CHESED_IDLE.get()));
        }
    }
}
