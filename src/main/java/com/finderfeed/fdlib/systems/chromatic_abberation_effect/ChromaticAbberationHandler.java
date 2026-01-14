package com.finderfeed.fdlib.systems.chromatic_abberation_effect;

import com.finderfeed.fdlib.FDClientHelpers;
import com.finderfeed.fdlib.FDLib;
import com.finderfeed.fdlib.systems.post_shaders.FDPostShaderInitializeEvent;
import com.finderfeed.fdlib.systems.post_shaders.FDRenderPostShaderEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.PostChain;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = FDLib.MOD_ID,value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ChromaticAbberationHandler {

    public static PostChain chromaticAbberation;

    public static ChromaticAbberationEffect currentEffect;

    @SubscribeEvent
    public static void registerPostShaderEvent(FDPostShaderInitializeEvent event){
        event.registerPostChain(((textureManager, resourceProvider, renderTarget) -> {
            try {
                return new PostChain(textureManager, resourceProvider, renderTarget,
                        FDLib.location("shaders/post/chromatic_abberation.json"));
            }catch (Exception e){
                throw new RuntimeException(e);
            }
        }), chain -> {
            chromaticAbberation = chain;
        });
    }

    @SubscribeEvent
    public static void renderShatteredScreen(FDRenderPostShaderEvent.Level event){

        if (chromaticAbberation == null || currentEffect == null) return;

        float strength = currentEffect.getStrength(event.getPartialTicks());
        FDClientHelpers.setShaderUniform(chromaticAbberation, "chromaticAbberationStrength", strength);
        chromaticAbberation.process(event.getPartialTicks());

    }

    @SubscribeEvent
    public static void clientTickEvent(TickEvent.ClientTickEvent event){

        if (Minecraft.getInstance().isPaused() || event.phase != TickEvent.Phase.START) return;
        if (currentEffect != null){
            if (currentEffect.tick()){
                currentEffect = null;
            }
        }
    }

    public static void setEffect(ChromaticAbberationEffect effect){
        currentEffect = effect;
    }

}
