package com.finderfeed.fdlib.to_other_mod;

import com.finderfeed.fdlib.FDLib;
import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.entity.renderer.FDEntityRendererBuilder;
import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.entity.renderer.FDRenderLayerOptions;
import com.finderfeed.fdlib.to_other_mod.client.FDParticles;
import com.finderfeed.fdlib.to_other_mod.client.particles.ArcLightningParticle;
import com.finderfeed.fdlib.to_other_mod.earthshatter_entity.EarthShatterRenderer;
import net.minecraft.client.renderer.RenderType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD,value = Dist.CLIENT,modid = FDLib.MOD_ID)
public class FDClientModEvents {


    @SubscribeEvent
    public static void registerParticleFactories(RegisterParticleProvidersEvent event){
        event.registerSpecial(FDParticles.ARC_LIGHTNING.get(), new ArcLightningParticle.Factory());
    }

    @SubscribeEvent
    public static void addRenderers(EntityRenderersEvent.RegisterRenderers event){
        event.registerEntityRenderer(FDEntities.CHESED.get(),
                FDEntityRendererBuilder.builder()
                        .shouldRender(((entity, frustum, x, y, z) -> {
                            return true;
                        }))
                        .addLayer(FDRenderLayerOptions.builder()
                                .model(FDModels.CHESED)
                                .renderType(RenderType.entityCutout(FDLib.location("textures/entities/chesed.png")))
                                .renderCondition((entity)->{
                                    return true;
                                })
                                .transformation((entity,matrices,pticks)->{

                                })
                                .build()
                        )
                        .build()
        );
        event.registerEntityRenderer(FDEntities.EARTH_SHATTER.get(), EarthShatterRenderer::new);
    }
}
