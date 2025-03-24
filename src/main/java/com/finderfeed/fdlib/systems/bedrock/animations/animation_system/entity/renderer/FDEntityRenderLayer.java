package com.finderfeed.fdlib.systems.bedrock.animations.animation_system.entity.renderer;

import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.AnimatedObject;
import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.RenderFunction;
import com.finderfeed.fdlib.systems.bedrock.models.FDModel;
import com.finderfeed.fdlib.util.FDColor;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.entity.Entity;

import java.util.function.Function;

public record FDEntityRenderLayer<T extends Entity & AnimatedObject>(FDModel model,
                                                                     RenderFunction<T,RenderType> renderType,
                                                                     Function<T,Boolean> renderCondition,
                                                                     FDEntityTransformation<T> matrixTransform,
                                                                     RenderFunction<T, FDColor> color,
                                                                     boolean ignoreHurtOverlay
) {
}
