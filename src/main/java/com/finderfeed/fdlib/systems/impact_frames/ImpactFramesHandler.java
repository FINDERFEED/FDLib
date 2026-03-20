package com.finderfeed.fdlib.systems.impact_frames;

import com.finderfeed.fdlib.FDClientHelpers;
import com.finderfeed.fdlib.FDLib;
import com.finderfeed.fdlib.init.FDConfigs;
import com.finderfeed.fdlib.systems.post_shaders.FDPostShaderInitializeEvent;
import com.finderfeed.fdlib.systems.post_shaders.FDRenderPostShaderEvent;
import com.mojang.blaze3d.pipeline.RenderTarget;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.PostChain;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.joml.Vector2i;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

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
    public static void renderImpactFrameShader(FDRenderPostShaderEvent.Level event) {
        if (isImpactFrameShaderActive()) {
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

    public static void beforePostEffect() {
        if (!isImpactFrameShaderActive()) return;

        RenderTarget main = Minecraft.getInstance().getMainRenderTarget();
        main.bindRead();

        int width = main.width;
        int height = main.height;

        if (width <= 0 || height <= 0) {
            FDLib.LOGGER.info("invalid size: {}x{}", width, height);
            return;
        }

        int fbo = org.lwjgl.opengl.GL11.glGetInteger(org.lwjgl.opengl.GL30.GL_READ_FRAMEBUFFER_BINDING);
        int status = org.lwjgl.opengl.GL30.glCheckFramebufferStatus(org.lwjgl.opengl.GL30.GL_READ_FRAMEBUFFER);
        int readBuffer = org.lwjgl.opengl.GL11.glGetInteger(org.lwjgl.opengl.GL11.GL_READ_BUFFER);

        FDLib.LOGGER.info("FBO: {} | status: {} | readBuffer: {}", fbo, status, readBuffer);

        float[] pixel = new float[3];

        int x = width / 2;
        int y = height / 2;

        int errBefore = org.lwjgl.opengl.GL11.glGetError();

        org.lwjgl.opengl.GL11.glReadPixels(x, y, 1, 1, org.lwjgl.opengl.GL11.GL_RGB, org.lwjgl.opengl.GL11.GL_FLOAT, pixel);

        int errAfter = org.lwjgl.opengl.GL11.glGetError();

        FDLib.LOGGER.info("read @ ({}, {}) -> {}, {}, {}", x, y, pixel[0], pixel[1], pixel[2]);
        FDLib.LOGGER.info("GL error before: {} | after: {}", errBefore, errAfter);

        float grayscale = Math.max(pixel[0], Math.max(pixel[1], pixel[2]));

        FDClientHelpers.setShaderUniform(impactFrameShader, "maxEstimatedGrayscale", grayscale);
    }

    public static boolean isImpactFrameShaderActive(){
        return currentImpactFrame != null;
    }

}