package com.finderfeed.fdlib.systems.post_shaders;

import net.minecraft.client.renderer.PostChain;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.Event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

public class FDPostShaderInitializeEvent extends Event {

    private List<PostChainShaderLoadInstance> postChainRegistry = new ArrayList<>();

    public FDPostShaderInitializeEvent(){
    }

    public void registerPostChain(PostChainLoad load, Consumer<PostChain> onLoad){
        this.postChainRegistry.add(new PostChainShaderLoadInstance(load,onLoad));
    }

    protected List<PostChainShaderLoadInstance> getPostChainRegistry() {
        return postChainRegistry;
    }

    public record PostChainShaderLoadInstance(PostChainLoad postChainLoad, Consumer<PostChain> onLoad){

    }

}
