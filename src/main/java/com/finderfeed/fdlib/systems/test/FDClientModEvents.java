package com.finderfeed.fdlib.systems.test;

import com.finderfeed.fdlib.FDLib;
import com.finderfeed.fdlib.init.FDEntities;
import com.finderfeed.fdlib.init.FDModels;
import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.entity.renderer.FDEntityRendererBuilder;
import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.entity.renderer.FDRenderLayerOptions;
import com.finderfeed.fdlib.util.rendering.FDRenderUtil;
import net.minecraft.client.renderer.RenderType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD,value = Dist.CLIENT,modid = FDLib.MOD_ID)
public class FDClientModEvents {

    @SubscribeEvent
    public static void addRenderers(EntityRenderersEvent.RegisterRenderers event){
        event.registerEntityRenderer(FDEntities.TEST.get(),
                FDEntityRendererBuilder.builder()
                        .addLayer(FDRenderLayerOptions.builder()
                                .model(FDModels.TEST)
                                .renderType(RenderType.entityCutout(FDLib.location("textures/entities/uldera_crystal_inner.png")))
                                .renderCondition((entity)->{
                                    return entity.tickCount % 20 > 10;
                                })
                                .build()
                        )
                        .addLayer(FDRenderLayerOptions.builder()
                                .model(FDModels.TEST)
                                .renderType(RenderType.eyes(FDLib.location("textures/entities/uldera_crystal_glow.png")))
                                .transformation((entity,stack,pticks)->{
                                    stack.mulPose(FDRenderUtil.rotationDegrees(FDRenderUtil.YP(),(entity.tickCount + pticks)));
                                })
                                .build()
                        )
                        .build()
        );
    }
}
