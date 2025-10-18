package com.finderfeed.fdlib.systems.post_shaders;

import com.mojang.blaze3d.pipeline.RenderTarget;
import net.minecraft.client.renderer.PostChain;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.server.packs.resources.ResourceProvider;

@FunctionalInterface
public interface PostChainLoad {

    PostChain loadPostChain(TextureManager textureManager, ResourceProvider resourceProvider, RenderTarget renderTarget);

}
