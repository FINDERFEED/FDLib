package com.finderfeed.fdlib.systems.post_shaders;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraftforge.eventbus.api.Event;

public abstract class FDRenderPostShaderEvent extends Event {

    private float partialTicks;

    public FDRenderPostShaderEvent(float partialTicks){
        this.partialTicks = partialTicks;
    }

    public float getPartialTicks() {
        return partialTicks;
    }

    public static class Level extends FDRenderPostShaderEvent {
        public Level(float deltaTracker) {
            super(deltaTracker);
        }
    }

    public static class Screen extends FDRenderPostShaderEvent {
        public Screen(float deltaTracker) {
            super(deltaTracker);
        }
    }

    public void doDefaultShaderBeforeShaderStuff(){
        RenderSystem.disableBlend();
        RenderSystem.disableDepthTest();
        RenderSystem.resetTextureMatrix();
    }

}
