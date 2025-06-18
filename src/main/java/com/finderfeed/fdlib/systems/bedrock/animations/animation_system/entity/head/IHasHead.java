package com.finderfeed.fdlib.systems.bedrock.animations.animation_system.entity.head;

import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.AnimatedObject;
import net.minecraft.world.entity.Mob;

public interface IHasHead<T extends Mob & AnimatedObject & IHasHead<T>> {

    HeadControllerContainer<T> getHeadControllerContainer();

}
