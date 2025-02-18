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

@EventBusSubscriber(modid = FDLib.MOD_ID,bus = EventBusSubscriber.Bus.GAME,value = Dist.CLIENT)
public class FDScreenParticleEngine {

    private static HashMap<ParticleRenderType, List<FDScreenParticle>> SCREEN_PARTICLES = new HashMap<>();
    private static HashMap<ParticleRenderType, List<FDScreenParticle>> OVERLAY_PARTICLES = new HashMap<>();

    @SubscribeEvent
    public static void renderScreenEvent(ScreenEvent.Render.Post event){
        if (NeoForge.EVENT_BUS.post(new ScreenParticlesRenderEvent.Screen()).isCanceled()) return;
        render(SCREEN_PARTICLES,event.getGuiGraphics(),event.getPartialTick());
    }

    @SubscribeEvent
    public static void onGuiRender(RenderGuiEvent.Post event){

        if (NeoForge.EVENT_BUS.post(new ScreenParticlesRenderEvent.Gui()).isCanceled()) return;

        GuiGraphics graphics = event.getGuiGraphics();
        int layerCount = Minecraft.getInstance().gui.getLayerCount();
        graphics.pose().pushPose();
        graphics.pose().translate(0,0, GuiLayerManager.Z_SEPARATION * layerCount);
        render(OVERLAY_PARTICLES,graphics,event.getPartialTick().getGameTimeDeltaPartialTick(true));
        graphics.pose().popPose();
    }


    @SubscribeEvent
    public static void clientTickEvent(ClientTickEvent.Pre event){
        tickParticles(OVERLAY_PARTICLES);
        tickParticles(SCREEN_PARTICLES);
    }

    @SubscribeEvent
    public static void onScreenClose(ScreenEvent.Closing event){
        clearParticles();
    }


    @SubscribeEvent
    public static void onLogoff(ClientPlayerNetworkEvent.LoggingOut event){
        clearParticles();
    }

    private static void render(HashMap<ParticleRenderType, List<FDScreenParticle>> particles,GuiGraphics graphics,float partialTicks){


        Tesselator tesselator = RenderSystem.renderThreadTesselator();
        TextureManager manager = Minecraft.getInstance().getTextureManager();



        for (var entry : particles.entrySet()){
            var list = entry.getValue();
            if (list.isEmpty()) continue;

            ParticleRenderType renderType = entry.getKey();


            var builder = renderType.begin(tesselator,manager);

            for (FDScreenParticle particle : list){
                particle.render(graphics,builder,partialTicks);
            }

            BufferUploader.drawWithShader(builder.build());

            if (renderType instanceof FDParticleRenderType fdParticleRenderType){
                fdParticleRenderType.end();
            }
        }

    }


    public static void tickParticles(HashMap<ParticleRenderType, List<FDScreenParticle>> particles){
        for (var entry : particles.entrySet()){
            Iterator<FDScreenParticle> iter = entry.getValue().iterator();
            while (iter.hasNext()){
                FDScreenParticle particle = iter.next();
                if (particle.isRemoved()){
                    iter.remove();
                }else{
                    particle.tick();
                }
            }
        }
    }

    public static void clearParticles(){
        SCREEN_PARTICLES.clear();
    }


    public static void addScreenParticle(FDScreenParticle particle){
        ParticleRenderType renderType = particle.getParticleRenderType();
        SCREEN_PARTICLES.computeIfAbsent(renderType, type->{
            LinkedList<FDScreenParticle> particles = new LinkedList<>();
            return particles;
        }).add(particle);
        particle.onAddedToEngine();
    }

    public static void addOverlayParticle(FDScreenParticle particle){
        ParticleRenderType renderType = particle.getParticleRenderType();
        OVERLAY_PARTICLES.computeIfAbsent(renderType, type->{
            LinkedList<FDScreenParticle> particles = new LinkedList<>();
            return particles;
        }).add(particle);
        particle.onAddedToEngine();
    }

}
