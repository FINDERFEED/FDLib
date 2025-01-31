package com.finderfeed.fdlib.systems.impact_frames;

import com.finderfeed.fdlib.ClientMixinHandler;
import com.finderfeed.fdlib.FDLib;
import com.finderfeed.fdlib.systems.shake.DefaultShake;
import com.finderfeed.fdlib.systems.shake.FDShakeData;
import com.mojang.blaze3d.platform.Window;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.PostChain;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.client.event.RenderFrameEvent;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.GAME,value = Dist.CLIENT,modid = FDLib.MOD_ID)
public class ImpactFramesHandler {

    private static final Queue<ImpactFrame> impactFrames = new ArrayDeque<>();
    private static int width = -1;
    private static int height = -1;
    public static PostChain impactFrameShader;

    @SubscribeEvent
    public static void renderFrameEvent(RenderFrameEvent.Pre event){

    }

    @SubscribeEvent
    public static void tick(ClientTickEvent.Pre event){
        manageImpactFrames();
    }

    private static void manageImpactFrames(){
        if (impactFrameShader == null) return;

        resizeImpactShaderIfNeeded();
        GameRenderer renderer = Minecraft.getInstance().gameRenderer;
        if (!impactFrames.isEmpty()) {

            var frame = impactFrames.poll();

            impactFrameShader.setUniform("treshhold",frame.getTreshhold());
            impactFrameShader.setUniform("treshholdLerp",frame.getTreshholdLerp());
            impactFrameShader.setUniform("invert",frame.isInverted() ? 1 : 0);

            renderer.postEffect = impactFrameShader;
            renderer.effectActive = true;
        }else{
            if (isImpactFrameShaderActive()){
                renderer.postEffect = null;
                renderer.effectActive = false;
            }
        }
    }

    public static void lightTextureMixin(Vector3f light, CallbackInfo ci){
        if (isImpactFrameShaderActive()) {
            light.set(1, 1, 1);
            ci.cancel();
        }
    }

    public static boolean isImpactFrameShaderActive(){
        GameRenderer renderer = Minecraft.getInstance().gameRenderer;
        return renderer.postEffect != null && renderer.postEffect.getName().equals(impactFrameShader.getName());
    }



    @SubscribeEvent
    public static void hurtEvent(AttackEntityEvent event){
        Player player = event.getEntity();
        if (player.level().isClientSide){
            ImpactFrame base = new ImpactFrame(0.6f,0,false);
            addImpactFrame(new ImpactFrame(base));
            addImpactFrame(new ImpactFrame(base).setInverted(true));
            ClientMixinHandler.addShake(new DefaultShake(FDShakeData.builder()
                    .frequency(20)
                    .amplitude(0.1f)
                    .outTime(10)
                    .build()));
        }
    }


    public static void addImpactFrame(ImpactFrame frame){
        impactFrames.offer(frame);
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
