package com.finderfeed.fdlib.systems.bedrock.animations.animation_system.tile.renderer;

import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.AnimatedObject;
import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.RenderFunction;
import com.finderfeed.fdlib.systems.bedrock.models.FDModel;
import com.finderfeed.fdlib.util.FDColor;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.function.Function;

public record FDBlockEntityRenderLayer<T extends BlockEntity & AnimatedObject>(FDModel model,
                                                                               RenderFunction<T, RenderType> renderType,
                                                                               Function<T,Boolean> renderCondition,
                                                                               FDBlockEntityTransformation<T> matrixTransform,
                                                                               RenderFunction<T, FDColor> color,
                                                                               int light
) {
}