package com.finderfeed.fdlib.systems.post_shaders;

import com.finderfeed.fdlib.systems.impact_frames.ImpactFramesHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.PostChain;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.NeoForgeMod;

import java.util.ArrayList;
import java.util.List;

public class FDPostShadersReloadableResourceListener extends SimplePreparableReloadListener<Integer> {
    @Override
    protected Integer prepare(ResourceManager resourceManager, ProfilerFiller profiler) {
        return 1;
    }

    @Override
    protected void apply(Integer lolwhat, ResourceManager manager, ProfilerFiller profiler) {


        List<Runnable> errors = new ArrayList<>();

        try {

            for (var shader : FDPostShadersHandler.POST_SHADERS){
                shader.close();
            }

            FDPostShadersHandler.POST_SHADERS.clear();


            ImpactFramesHandler.initializeImpactShaderOrResizeIfNeeded(true);

            FDPostShaderInitializeEvent event = new FDPostShaderInitializeEvent();
            NeoForge.EVENT_BUS.post(event);
            var registry = event.getPostChainRegistry();

            for (var shader : registry) {
                try {
                    this.loadPostChain(shader);
                } catch (Exception e){
                    errors.add(e::printStackTrace);
                }
            }

        }catch (Exception e){
            errors.add(e::printStackTrace);
        }

        if (!errors.isEmpty()){
            for (var error : errors){
                error.run();
            }
            throw new RuntimeException("Failed to load shaders");
        }

    }

    private void loadPostChain(FDPostShaderInitializeEvent.PostChainShaderLoadInstance shader){
        var postChainLoad = shader.postChainLoad();
        PostChain postChain = postChainLoad.loadPostChain(Minecraft.getInstance().getTextureManager(), Minecraft.getInstance().getResourceManager(), Minecraft.getInstance().getMainRenderTarget());
        FDPostShadersHandler.POST_SHADERS.add(postChain);
        postChain.resize(
                Minecraft.getInstance().getWindow().getWidth(),
                Minecraft.getInstance().getWindow().getHeight()
        );
        shader.onLoad().accept(postChain);
    }

}
