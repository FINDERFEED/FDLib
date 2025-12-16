package com.finderfeed.fdlib.systems.chromatic_abberation_effect;

import com.finderfeed.fdlib.FDLib;
import com.finderfeed.fdlib.systems.post_shaders.FDPostShaderInitializeEvent;
import com.finderfeed.fdlib.systems.post_shaders.FDRenderPostShaderEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.PostChain;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;

@EventBusSubscriber(modid = FDLib.MOD_ID,value = Dist.CLIENT)
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

        float strength = currentEffect.getStrength(event.getDeltaTracker().getGameTimeDeltaPartialTick(false));
        chromaticAbberation.setUniform("chromaticAbberationStrength", strength);
        chromaticAbberation.process(event.getDeltaTracker().getGameTimeDeltaPartialTick(false));

    }

    @SubscribeEvent
    public static void clientTickEvent(ClientTickEvent.Pre event){
        if (Minecraft.getInstance().isPaused()) return;
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
