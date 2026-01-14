package com.finderfeed.fdlib.systems.post_shaders;

import com.finderfeed.fdlib.FDLib;
import com.mojang.blaze3d.platform.Window;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.PostChain;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = FDLib.MOD_ID, value = Dist.CLIENT)
public class FDPostShadersHandler {

    public static boolean WAS_LOADED_ONCE = false;

    public static final List<PostChain> POST_SHADERS = new ArrayList<>();

    private static int width;
    private static int height;

    @SubscribeEvent
    public static void processPostShaders(TickEvent.ClientTickEvent event){

        if (event.phase != TickEvent.Phase.START) return;

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

}
