package com.finderfeed.fdlib.systems.test;

import com.finderfeed.fdlib.FDLib;
import com.finderfeed.fdlib.init.FDEntities;
import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.FDEntityRenderTypes;
import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.entity.entity_types.renderers.FDEntityRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD,value = Dist.CLIENT,modid = FDLib.MOD_ID)
public class FDClientModEvents {

    @SubscribeEvent
    public static void addRenderers(EntityRenderersEvent.RegisterRenderers event){
        event.registerEntityRenderer(FDEntities.TEST.get(),FDEntityRenderer
                .createRenderer(ResourceLocation.tryBuild(FDLib.MOD_ID,"textures/entities/uldera_crystal_inner.png"),
                        new FDEntityRenderTypes<>()
                                .addRenderType("test",entity -> {
                                    return RenderType.entityTranslucent(ResourceLocation.tryBuild(FDLib.MOD_ID,"textures/entities/uldera_crystal_inner.png"));
                                })
                        ));
    }
}
