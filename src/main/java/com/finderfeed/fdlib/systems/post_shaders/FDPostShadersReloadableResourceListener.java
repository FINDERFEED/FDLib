package com.finderfeed.fdlib.systems.post_shaders;

import com.finderfeed.fdlib.FDLib;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.PostChain;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraftforge.common.MinecraftForge;

import java.util.ArrayList;
import java.util.List;

import static com.finderfeed.fdlib.systems.post_shaders.FDPostShadersHandler.POST_SHADERS;

public class FDPostShadersReloadableResourceListener extends SimplePreparableReloadListener<Integer> {
    @Override
    protected Integer prepare(ResourceManager resourceManager, ProfilerFiller profiler) {
        return 1;
    }

    @Override
    protected void apply(Integer lolwhat, ResourceManager manager, ProfilerFiller profiler) {
        initializeShaders();
    }

    public static void loadPostChain(FDPostShaderInitializeEvent.PostChainShaderLoadInstance shader){
        var postChainLoad = shader.postChainLoad();
        PostChain postChain = postChainLoad.loadPostChain(Minecraft.getInstance().getTextureManager(), Minecraft.getInstance().getResourceManager(), Minecraft.getInstance().getMainRenderTarget());
        FDPostShadersHandler.POST_SHADERS.add(postChain);
        postChain.resize(
                Minecraft.getInstance().getWindow().getWidth(),
                Minecraft.getInstance().getWindow().getHeight()
        );
        shader.onLoad().accept(postChain);
    }

    public static void initializeShaders(){

        FDLib.LOGGER.info("Initializing shaders...");

        for (var shader : POST_SHADERS){
            shader.close();
        }

        POST_SHADERS.clear();

        FDPostShaderInitializeEvent event = new FDPostShaderInitializeEvent();
        MinecraftForge.EVENT_BUS.post(event);

        List<Runnable> errors = new ArrayList<>();
        var registry = event.getPostChainRegistry();

        for (var shader : registry) {
            try {
                FDPostShadersReloadableResourceListener.loadPostChain(shader);
            } catch (Exception e){
                errors.add(e::printStackTrace);
            }
        }

        if (!errors.isEmpty()){
            for (var error : errors){
                error.run();
            }
            throw new RuntimeException("Failed to load shaders");
        }

        FDLib.LOGGER.info("Finished shader initialization.");
    }

}
