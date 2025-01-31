package com.finderfeed.fdlib.systems.impact_frames;

import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;

public class FDPostShadersReloadableResourceListener extends SimplePreparableReloadListener<Integer> {
    @Override
    protected Integer prepare(ResourceManager resourceManager, ProfilerFiller profiler) {
        return 1;
    }

    @Override
    protected void apply(Integer lolwhat, ResourceManager manager, ProfilerFiller profiler) {

        try {
            ImpactFramesHandler.initializeImpactShaderOrResizeIfNeeded(true);
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }
}
