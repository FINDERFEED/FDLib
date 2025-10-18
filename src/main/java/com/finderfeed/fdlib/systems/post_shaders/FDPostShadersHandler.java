package com.finderfeed.fdlib.systems.post_shaders;

import com.finderfeed.fdlib.FDLib;
import com.mojang.blaze3d.platform.Window;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.PostChain;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.common.NeoForge;

import java.util.ArrayList;
import java.util.List;

@EventBusSubscriber(modid = FDLib.MOD_ID, value = Dist.CLIENT)
public class FDPostShadersHandler {

    protected static final List<PostChain> POST_SHADERS = new ArrayList<>();

    private static int width;
    private static int height;

    @SubscribeEvent
    public static void processPostShaders(ClientTickEvent.Pre event){
        resizeAllShadersIfNeeded();
    }

    private static void resizeAllShadersIfNeeded(){

        Window window = Minecraft.getInstance().getWindow();

        int width = window.getWidth();
        int height = window.getHeight();

        if (width != FDPostShadersHandler.width || height != FDPostShadersHandler.height) {
            for (PostChain postChain : POST_SHADERS) {
                postChain.resize(width,height);
            }
            FDPostShadersHandler.width = width;
            FDPostShadersHandler.height = height;
        }

    }

    @SubscribeEvent
    public static void loggingIn(ClientPlayerNetworkEvent.LoggingIn loggingIn){

        for (var shader : FDPostShadersHandler.POST_SHADERS){
            shader.close();
        }

        POST_SHADERS.clear();

        FDPostShaderInitializeEvent event = new FDPostShaderInitializeEvent();
        NeoForge.EVENT_BUS.post(event);

        List<Runnable> errors = new ArrayList<>();
        var registry = event.getPostChainRegistry();

        for (var shader : registry) {
            try {
                FDPostShadersReloadableResourceListener.loadPostChain(shader);
            } catch (Exception e){
                errors.add(e::printStackTrace);
            }
        }

        if (!errors.isEmpty()){
            for (var error : errors){
                error.run();
            }
            throw new RuntimeException("Failed to load shaders");
        }
    }

}
