package com.finderfeed.fdlib.systems.broken_screen_effect;

import com.finderfeed.fdlib.FDClientHelpers;
import com.finderfeed.fdlib.FDLib;
import com.finderfeed.fdlib.systems.post_shaders.FDPostShaderInitializeEvent;
import com.finderfeed.fdlib.systems.post_shaders.FDRenderPostShaderEvent;
import com.finderfeed.fdlib.util.rendering.FDRenderUtil;
import com.google.gson.JsonSyntaxException;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.EffectInstance;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.PostChain;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.io.IOException;

@Mod.EventBusSubscriber(modid = FDLib.MOD_ID,bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ShatteredScreenEffectHandler implements IGuiOverlay {

    private static PostChain shatteredScreenShader;

    private static ShatteredScreenEffectInstance currentEffect;

    @Override
    public void render(ForgeGui gui, GuiGraphics guiGraphics, float partialTick, int screenWidth, int screenHeight) {
        if (currentEffect == null) return;

        var settings = currentEffect.settings;
        var texture = settings.shatteredScreenTexture;

        if (texture.equals(ShatteredScreenSettings.NULL_LOCATION)){
            return;
        }

        FDRenderUtil.bindTexture(texture);

        float alpha = currentEffect.getCurrentPercent(partialTick);

        Window window = Minecraft.getInstance().getWindow();
        float width = window.getGuiScaledWidth();
        float height = window.getGuiScaledHeight();

        FDRenderUtil.blitWithBlend(guiGraphics.pose(),0,0,width,height,0,0,1,1,1,1,0,alpha);
    }

    public static void setCurrentEffect(ShatteredScreenSettings settings){
        currentEffect = new ShatteredScreenEffectInstance(settings);
    }



    @SubscribeEvent
    public static void renderShatteredScreen(FDRenderPostShaderEvent.Screen event){

        if (currentEffect == null || shatteredScreenShader == null) return;

        if (currentEffect.settings.onScreen) {
            float currentStrength = currentEffect.getCurrentPercent(event.getPartialTicks()) * currentEffect.settings.maxOffset;

            FDClientHelpers.setShaderUniform(shatteredScreenShader, "maxOffset", currentStrength);
            FDClientHelpers.setShaderUniform(shatteredScreenShader, "chromaticAbberationStrength", currentEffect.settings.chromaticAbberationStrength);

            shatteredScreenShader.process(event.getPartialTicks());
        }

    }


    @SubscribeEvent
    public static void renderLevelShatteredScreen(FDRenderPostShaderEvent.Level event){

        if (currentEffect == null || shatteredScreenShader == null) return;

        if (!currentEffect.settings.onScreen) {

            float currentStrength = currentEffect.getCurrentPercent(event.getPartialTicks()) * currentEffect.settings.maxOffset;

            FDClientHelpers.setShaderUniform(shatteredScreenShader, "maxOffset", currentStrength);
            FDClientHelpers.setShaderUniform(shatteredScreenShader, "chromaticAbberationStrength", currentEffect.settings.chromaticAbberationStrength);

            shatteredScreenShader.process(event.getPartialTicks());

        }
    }

    @SubscribeEvent
    public static void clientTick(TickEvent.ClientTickEvent event){

        if (shatteredScreenShader == null || event.phase != TickEvent.Phase.START) return;

        if (!Minecraft.getInstance().isPaused()){
            tickCurrentEffect();
        }

    }

    private static void tickCurrentEffect(){
        GameRenderer gameRenderer = Minecraft.getInstance().gameRenderer;

        if (currentEffect != null && currentEffect.hasEnded()) currentEffect = null;

        if (currentEffect != null){
            currentEffect.tick();
        }

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

        public BrokenScreenPostChain(TextureManager p_110018_, ResourceManager p_330592_, RenderTarget p_110020_, ResourceLocation p_110021_) throws IOException, JsonSyntaxException {
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
