package com.finderfeed.fdlib.systems.broken_screen_effect;

import com.finderfeed.fdlib.FDLib;
import com.finderfeed.fdlib.systems.post_shaders.FDPostShaderInitializeEvent;
import com.finderfeed.fdlib.util.rendering.FDRenderUtil;
import com.google.gson.JsonSyntaxException;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.client.renderer.EffectInstance;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.PostChain;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceProvider;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RenderFrameEvent;
import net.neoforged.neoforge.client.event.RenderGuiEvent;

import java.io.IOException;

@EventBusSubscriber(modid = FDLib.MOD_ID, value = Dist.CLIENT)
public class ShatteredScreenEffectHandler implements LayeredDraw.Layer {

    private static PostChain shatteredScreenShader;

    private static ShatteredScreenEffectInstance currentEffect;

    @Override
    public void render(GuiGraphics graphics, DeltaTracker tracker) {
        if (currentEffect == null) return;

        var settings = currentEffect.settings;
        var texture = settings.shatteredScreenTexture;
        FDRenderUtil.bindTexture(texture);

        float alpha = currentEffect.getCurrentPercent(tracker.getGameTimeDeltaPartialTick(false));

        Window window = Minecraft.getInstance().getWindow();
        float width = window.getGuiScaledWidth();
        float height = window.getGuiScaledHeight();

        FDRenderUtil.blitWithBlend(graphics.pose(),0,0,width,height,0,0,1,1,1,1,0,alpha);
    }

    public static void setCurrentEffect(ShatteredScreenSettings settings){
        currentEffect = new ShatteredScreenEffectInstance(settings);
    }

    @SubscribeEvent
    public static void clientTick(ClientTickEvent.Pre event){

        if (shatteredScreenShader == null) return;

        if (!Minecraft.getInstance().isPaused()){
            tickCurrentEffect();
        }

    }

    private static void tickCurrentEffect(){
        GameRenderer gameRenderer = Minecraft.getInstance().gameRenderer;

        if (currentEffect != null && currentEffect.hasEnded()) currentEffect = null;

        if (currentEffect != null){
            currentEffect.tick();
        }else{
            if (gameRenderer.postEffect == shatteredScreenShader){
                gameRenderer.postEffect = null;
                gameRenderer.effectActive = false;
            }
        }

    }

    @SubscribeEvent
    public static void setupAndRenderShatteredScreen(RenderFrameEvent.Pre event){

        if (currentEffect == null || shatteredScreenShader == null) return;


        float currentStrength = currentEffect.getCurrentPercent(event.getPartialTick().getGameTimeDeltaPartialTick(false)) * currentEffect.settings.maxOffset;

        shatteredScreenShader.setUniform("maxOffset", currentStrength);

        GameRenderer gameRenderer = Minecraft.getInstance().gameRenderer;
        gameRenderer.postEffect = shatteredScreenShader;
        gameRenderer.effectActive = true;


    }


    @SubscribeEvent
    public static void onLogoff(ClientPlayerNetworkEvent.LoggingOut event){
        currentEffect = null;
    }

    @SubscribeEvent
    public static void registerShader(FDPostShaderInitializeEvent event){
        event.registerPostChain(((textureManager, resourceProvider, renderTarget) -> {
            try {
                return new BrokenScreenPostChain(textureManager,resourceProvider,renderTarget,FDLib.location("shaders/post/shattered_screen.json"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }), chain -> shatteredScreenShader = chain);
    }


    public static class BrokenScreenPostChain extends PostChain{

        public BrokenScreenPostChain(TextureManager p_110018_, ResourceProvider p_330592_, RenderTarget p_110020_, ResourceLocation p_110021_) throws IOException, JsonSyntaxException {
            super(p_110018_, p_330592_, p_110020_, p_110021_);
        }

        @Override
        public void process(float pticks) {
            var dataTexture = currentEffect.settings.shatteredScreenDataTexture;
            FDRenderUtil.bindTexture(dataTexture);
            int texture = RenderSystem.getShaderTexture(0);

            for (var pass : this.passes){
                EffectInstance effectInstance = pass.getEffect();
                effectInstance.setSampler("sampler0", ()-> texture);
            }

            super.process(pticks);
        }

    }

}
