package com.finderfeed.fdlib.systems.post_shaders;

import net.minecraft.client.renderer.PostChain;
import net.minecraftforge.eventbus.api.Event;

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

    public List<PostChainShaderLoadInstance> getPostChainRegistry() {
        return postChainRegistry;
    }

    public record PostChainShaderLoadInstance(PostChainLoad postChainLoad, Consumer<PostChain> onLoad){

    }

}
