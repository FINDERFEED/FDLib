package com.finderfeed.fdlib.systems.impact_frames;

import com.finderfeed.fdlib.ClientMixinHandler;
import com.finderfeed.fdlib.FDLib;
import com.finderfeed.fdlib.init.FDConfigs;
import com.finderfeed.fdlib.systems.shake.DefaultShake;
import com.finderfeed.fdlib.systems.shake.FDShakeData;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.PostChain;
import net.minecraft.core.Holder;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.client.event.RenderFrameEvent;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;
import org.joml.Vector2i;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.IOException;
import java.nio.IntBuffer;
import java.util.*;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.GAME,value = Dist.CLIENT,modid = FDLib.MOD_ID)
public class ImpactFramesHandler {

    private static final Queue<ImpactFrame> impactFrames = new ArrayDeque<>();
    private static int width = -1;
    private static int height = -1;
    private static ImpactFrame currentImpactFrame = null;
    private static int currentTick = 0;

    public static PostChain impactFrameShader;

    @SubscribeEvent
    public static void tick(ClientTickEvent.Pre event){
        manageImpactFrames();
    }

    private static void manageImpactFrames(){
        if (impactFrameShader == null) return;

        resizeImpactShaderIfNeeded();
        GameRenderer renderer = Minecraft.getInstance().gameRenderer;

        if (currentImpactFrame == null){
            if (!impactFrames.isEmpty()){
                currentImpactFrame = impactFrames.poll();
                activateImpactShader(currentImpactFrame);
                currentTick = 1;
            }else{
                currentTick = 0;
                renderer.postEffect = null;
                renderer.effectActive = false;
            }
        }else{
            if (currentTick >= currentImpactFrame.getDuration()){
                if (!impactFrames.isEmpty()){
                    currentImpactFrame = impactFrames.poll();
                    activateImpactShader(currentImpactFrame);
                    currentTick = 1;
                }else{
                    currentImpactFrame = null;
                    currentTick = 0;
                    renderer.postEffect = null;
                    renderer.effectActive = false;
                }
            }else{
                activateImpactShader(currentImpactFrame);
                currentTick++;
            }
        }





    }

    private static void activateImpactShader(ImpactFrame frame){
        GameRenderer renderer = Minecraft.getInstance().gameRenderer;
        impactFrameShader.setUniform("treshhold",frame.getTreshhold());
        impactFrameShader.setUniform("treshholdLerp",frame.getTreshholdLerp());
        impactFrameShader.setUniform("invert",frame.isInverted() ? 1 : 0);
        renderer.postEffect = impactFrameShader;
        renderer.effectActive = true;
    }

    public static void beforePostEffect(DeltaTracker deltaTracker, boolean idk){

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

            impactFrameShader.setUniform("maxEstimatedGrayscale", maxGrayscale);
        }

    }


    public static boolean isImpactFrameShaderActive(){
        GameRenderer renderer = Minecraft.getInstance().gameRenderer;
        return renderer.postEffect != null && renderer.postEffect.getName().equals(impactFrameShader.getName());
    }

    public static void addImpactFrame(ImpactFrame frame){
        if (FDConfigs.CLIENTSIDE_CONFIG.get().impactFramesEnabled) {
            impactFrames.offer(frame);
        }
    }


    private static void resizeImpactShaderIfNeeded(){
        Window window = Minecraft.getInstance().getWindow();

        if (width != window.getWidth() || height != window.getHeight()){
            impactFrameShader.resize(window.getWidth(),window.getHeight());
            width = window.getWidth();
            height = window.getHeight();
        }
    }


    public static void initializeImpactShaderOrResizeIfNeeded(boolean forceReload) throws IOException {
        if (impactFrameShader == null || forceReload) {
            if (impactFrameShader != null){
                impactFrameShader.close();
            }
            impactFrameShader = new PostChain(Minecraft.getInstance().getTextureManager(), Minecraft.getInstance().getResourceManager(), Minecraft.getInstance().getMainRenderTarget(),
                    FDLib.location("shaders/post/impact_frame.json"));
        }

        Window window = Minecraft.getInstance().getWindow();

        if (width != window.getWidth() || height != window.getHeight() || forceReload){
            impactFrameShader.resize(window.getWidth(),window.getHeight());
            width = window.getWidth();
            height = window.getHeight();
        }
    }


}
