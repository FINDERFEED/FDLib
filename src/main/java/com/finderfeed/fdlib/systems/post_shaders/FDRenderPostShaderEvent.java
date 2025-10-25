package com.finderfeed.fdlib.systems.post_shaders;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.DeltaTracker;
import net.neoforged.bus.api.Event;

public abstract class FDRenderPostShaderEvent extends Event {

    private DeltaTracker deltaTracker;

    public FDRenderPostShaderEvent(DeltaTracker deltaTracker){
        this.deltaTracker = deltaTracker;
    }

    public DeltaTracker getDeltaTracker() {
        return deltaTracker;
    }

    public static class Level extends FDRenderPostShaderEvent {
        public Level(DeltaTracker deltaTracker) {
            super(deltaTracker);
        }
    }

    public static class Screen extends FDRenderPostShaderEvent {
        public Screen(DeltaTracker deltaTracker) {
            super(deltaTracker);
        }
    }

    public void doDefaultShaderBeforeShaderStuff(){
        RenderSystem.disableBlend();
        RenderSystem.disableDepthTest();
        RenderSystem.resetTextureMatrix();
    }

}
