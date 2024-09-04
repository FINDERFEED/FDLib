package com.finderfeed.fdlib.to_other_mod;

import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.AnimationSystem;
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
        AnimationSystem system = this.getSystem();

        system.setVariable("variable.radius",600);
        system.setVariable("variable.angle",180);
        if (!this.level().isClientSide){
            system.startAnimation("IDLE", new AnimationTicker(FDAnimations.CHESED_IDLE.get()));
        }
    }
}
