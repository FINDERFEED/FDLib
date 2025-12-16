package com.finderfeed.fdlib.systems.chromatic_abberation_effect;

import com.finderfeed.fdlib.FDLib;
import com.finderfeed.fdlib.systems.post_shaders.FDPostShaderInitializeEvent;
import com.finderfeed.fdlib.systems.post_shaders.FDRenderPostShaderEvent;
import net.minecraft.client.renderer.PostChain;
import net.neoforged.api.distmarker.Dist;

@EventBusSubscriber(modid = FDLib.MOD_ID,value = Dist.CLIENT,bus = EventBusSubscriber.Bus.GAME)
public class FDChromaticAbberationHandler {

    public static PostChain chromaticAbberation;

    @SubscriveEvent
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

        if (chromaticAbberation == null) return;

        chromaticAbberation.setUniform("chromaticAbberationStrength", 0.05f);
        chromaticAbberation.process(event.getDeltaTracker().getGameTimeDeltaPartialTick(false));
    }

}
