package com.finderfeed.fdlib.systems.impact_frames;

import com.finderfeed.fdlib.FDClientHelpers;
import com.finderfeed.fdlib.FDLib;
import com.finderfeed.fdlib.init.FDConfigs;
import com.finderfeed.fdlib.systems.post_shaders.FDPostShaderInitializeEvent;
import com.finderfeed.fdlib.systems.post_shaders.FDRenderPostShaderEvent;
import com.mojang.blaze3d.pipeline.RenderTarget;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.PostChain;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.joml.Vector2i;
import org.lwjgl.opengl.GL11;

import java.io.IOException;
import java.util.*;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE,modid = FDLib.MOD_ID)
public class ImpactFramesHandler {

    private static final Queue<ImpactFrame> impactFrames = new ArrayDeque<>();
    private static ImpactFrame currentImpactFrame = null;
    private static int currentTick = 0;

    public static PostChain impactFrameShader;

    @SubscribeEvent
    public static void registerShader(FDPostShaderInitializeEvent event){
        event.registerPostChain(((textureManager, resourceProvider, renderTarget) -> {
            try {
                return new PostChain(textureManager,resourceProvider,renderTarget,FDLib.location("shaders/post/impact_frame.json"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }),chain -> impactFrameShader = chain);
    }

    @SubscribeEvent
    public static void tick(TickEvent.ClientTickEvent event){
        if (event.phase != TickEvent.Phase.START) return;
        manageImpactFrames();
    }

    @SubscribeEvent
    public static void renderImpactFrameShader(FDRenderPostShaderEvent.Level event){


        if (isImpactFrameShaderActive()){
            beforePostEffect();
            event.doDefaultShaderBeforeShaderStuff();
            impactFrameShader.process(event.getPartialTicks());
        }

    }

    private static void manageImpactFrames(){

        if (impactFrameShader == null) return;

        if (currentImpactFrame == null){
            if (!impactFrames.isEmpty()){
                currentImpactFrame = impactFrames.poll();
                manageImpactFrameShaderUniforms(currentImpactFrame);
                currentTick = 1;
            }else{
                currentTick = 0;
            }
        }else{
            if (currentTick >= currentImpactFrame.getDuration()){
                if (!impactFrames.isEmpty()){
                    currentImpactFrame = impactFrames.poll();
                    manageImpactFrameShaderUniforms(currentImpactFrame);
                    currentTick = 1;
                }else{
                    currentImpactFrame = null;
                    currentTick = 0;
                }
            }else{
                manageImpactFrameShaderUniforms(currentImpactFrame);
                currentTick++;
            }
        }

    }

    public static void addImpactFrame(ImpactFrame frame){
        if (FDConfigs.CLIENTSIDE_CONFIG.get().impactFramesEnabled) {
            impactFrames.offer(frame);
        }
    }

    private static void manageImpactFrameShaderUniforms(ImpactFrame frame){
        FDClientHelpers.setShaderUniform(impactFrameShader,"treshhold", frame.getTreshhold());
        FDClientHelpers.setShaderUniform(impactFrameShader,"treshholdLerp", frame.getTreshholdLerp());
        FDClientHelpers.setShaderUniform(impactFrameShader,"invert", frame.isInverted() ? 1 : 0);
    }

    public static void beforePostEffect(){

        if (isImpactFrameShaderActive()) {
            RenderTarget main = Minecraft.getInstance().getMainRenderTarget();
            main.bindRead();


            int width = GL11.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D, 0, GL11.GL_TEXTURE_WIDTH);
            int height = GL11.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D, 0, GL11.GL_TEXTURE_HEIGHT);

            float[] pixel = new float[3];

            int hhalfW = width / 4;
            int hhalfH = height / 4;

            Vector2i[] samplePoints = new Vector2i[]{
                    //line on bottom
                    new Vector2i(hhalfW, hhalfH),
                    new Vector2i(hhalfW * 2, hhalfH),
                    new Vector2i(hhalfW * 3, hhalfH),
                    //line on center
                    new Vector2i(hhalfW, hhalfH * 2),
                    new Vector2i(hhalfW * 2, hhalfH * 2),
                    new Vector2i(hhalfW * 3, hhalfH * 2),
                    //line on top
                    new Vector2i(hhalfW, hhalfH * 3),
                    new Vector2i(hhalfW * 2, hhalfH * 3),
                    new Vector2i(hhalfW * 3, hhalfH * 3),
            };

            float maxGrayscale = 0.0f;

            for (Vector2i point : samplePoints) {

                GL11.glReadPixels(point.x, point.y, 1, 1, GL11.GL_RGB, GL11.GL_FLOAT, pixel);

                float grayscale = Math.max(pixel[0], Math.max(pixel[1], pixel[2]));
                maxGrayscale = Math.max(maxGrayscale, grayscale);
            }

            FDClientHelpers.setShaderUniform(impactFrameShader, "maxEstimatedGrayscale", maxGrayscale);
        }

    }

    public static boolean isImpactFrameShaderActive(){
        return currentImpactFrame != null;
    }

}