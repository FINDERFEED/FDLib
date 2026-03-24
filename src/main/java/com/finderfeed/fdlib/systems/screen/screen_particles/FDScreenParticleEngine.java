package com.finderfeed.fdlib.systems.screen.screen_particles;

import com.finderfeed.fdlib.FDLib;
import com.finderfeed.fdlib.systems.particle.FDParticleRenderType;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.Tesselator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.renderer.texture.TextureManager;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RenderGuiEvent;
import net.neoforged.neoforge.client.event.ScreenEvent;
import net.neoforged.neoforge.client.gui.GuiLayerManager;
import net.neoforged.neoforge.common.NeoForge;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

@EventBusSubscriber(modid = FDLib.MOD_ID,value = Dist.CLIENT)
public class FDScreenParticleEngine {

    private static ScreenParticleEngine SCREEN_PARTICLE_ENGINE = new ScreenParticleEngine();
    private static ScreenParticleEngine OVERLAY_PARTICLE_ENGINE = new ScreenParticleEngine();

    @SubscribeEvent
    public static void renderScreenEvent(ScreenEvent.Render.Post event){
        if (NeoForge.EVENT_BUS.post(new ScreenParticlesRenderEvent.Screen()).isCanceled()) return;
        SCREEN_PARTICLE_ENGINE.render(event.getGuiGraphics(), event.getPartialTick());
    }

    @SubscribeEvent
    public static void onGuiRender(RenderGuiEvent.Post event){

        if (NeoForge.EVENT_BUS.post(new ScreenParticlesRenderEvent.Gui()).isCanceled()) return;

        GuiGraphics graphics = event.getGuiGraphics();
        int layerCount = Minecraft.getInstance().gui.getLayerCount();
        graphics.pose().pushPose();
        graphics.pose().translate(0,0, GuiLayerManager.Z_SEPARATION * layerCount);
        OVERLAY_PARTICLE_ENGINE.render(graphics, event.getPartialTick().getGameTimeDeltaPartialTick(true));
        graphics.pose().popPose();
    }


    @SubscribeEvent
    public static void clientTickEvent(ClientTickEvent.Pre event){
        SCREEN_PARTICLE_ENGINE.tick();
        OVERLAY_PARTICLE_ENGINE.tick();
    }

    @SubscribeEvent
    public static void onScreenClose(ScreenEvent.Closing event){
        clearParticles();
    }


    @SubscribeEvent
    public static void onLogoff(ClientPlayerNetworkEvent.LoggingOut event){
        clearParticles();
    }

    public static void clearParticles(){
        SCREEN_PARTICLE_ENGINE.clearParticles();
    }


    public static void addScreenParticle(FDScreenParticle particle){
        SCREEN_PARTICLE_ENGINE.addParticle(particle);
    }

    public static void addOverlayParticle(FDScreenParticle particle){
        OVERLAY_PARTICLE_ENGINE.addParticle(particle);
    }

}
