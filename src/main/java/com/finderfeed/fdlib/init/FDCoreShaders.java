package com.finderfeed.fdlib.init;

import com.finderfeed.fdlib.FDLib;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterShadersEvent;

import java.io.IOException;

@EventBusSubscriber(modid = FDLib.MOD_ID,bus = EventBusSubscriber.Bus.MOD,value = Dist.CLIENT)
public class FDCoreShaders {

    public static ShaderInstance NOISE = null;

    @SubscribeEvent
    public static void registerShaders(RegisterShadersEvent event) throws IOException {

        event.registerShader(new ShaderInstance(event.getResourceProvider(), ResourceLocation.tryBuild(FDLib.MOD_ID,"noise"), DefaultVertexFormat.POSITION_TEX_COLOR),(inst)->{
            NOISE = inst;
        });

    }

}
