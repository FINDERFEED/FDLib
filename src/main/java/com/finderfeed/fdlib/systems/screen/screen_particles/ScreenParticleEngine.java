package com.finderfeed.fdlib.systems.screen.screen_particles;

import com.finderfeed.fdlib.systems.particle.FDParticleRenderType;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.Tesselator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.renderer.texture.TextureManager;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class ScreenParticleEngine {

    private HashMap<ParticleRenderType, List<FDScreenParticle>> particles = new HashMap<>();

    public void tick(){
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

    public void render(GuiGraphics graphics, float partialTicks){
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

    public void addParticle(FDScreenParticle<?> particle){
        ParticleRenderType renderType = particle.getParticleRenderType();
        particles.computeIfAbsent(renderType, type->{
            LinkedList<FDScreenParticle> particles = new LinkedList<>();
            return particles;
        }).add(particle);
        particle.onAddedToEngine();
    }

    public void clearParticles(){
        particles.clear();
    }

}
