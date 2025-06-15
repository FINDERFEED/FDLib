package com.finderfeed.fdlib.systems.render_types;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

@FunctionalInterface
public interface RenderTypeFactory {

    RenderType create(ResourceLocation textureLocation);

}
