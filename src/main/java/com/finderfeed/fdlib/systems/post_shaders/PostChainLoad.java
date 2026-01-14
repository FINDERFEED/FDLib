package com.finderfeed.fdlib.systems.post_shaders;

import com.mojang.blaze3d.pipeline.RenderTarget;
import net.minecraft.client.renderer.PostChain;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.server.packs.resources.ResourceManager;

@FunctionalInterface
public interface PostChainLoad {

    PostChain loadPostChain(TextureManager textureManager, ResourceManager resourceProvider, RenderTarget renderTarget);

}
