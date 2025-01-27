package com.finderfeed.fdlib.init;

import com.finderfeed.fdlib.FDLib;
import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.tile.renderer.FDBlockEntityRendererBuilder;
import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.tile.renderer.FDBlockRenderLayerOptions;
import com.finderfeed.fdlib.util.client.particles.FDTerrainParticle;
import com.finderfeed.fdlib.util.client.particles.InvisibleParticle;
import com.finderfeed.fdlib.util.client.particles.ball_particle.BallParticle;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;

@EventBusSubscriber(modid = FDLib.MOD_ID,bus = EventBusSubscriber.Bus.MOD,value = Dist.CLIENT)
public class FDClientModEvents {


    @SubscribeEvent
    public static void assignFactories(RegisterParticleProvidersEvent event){
        event.registerSpriteSet(FDParticles.INVISIBLE.get(),InvisibleParticle.Factory::new);
        event.registerSpriteSet(FDParticles.BALL_PARTICLE.get(),BallParticle.Factory::new);
        event.registerSpecial(FDParticles.TERRAIN_PARTICLE.get(), new FDTerrainParticle.Provider());
    }

    @SubscribeEvent
    public static void registerBERenderers(FMLClientSetupEvent event){

            BlockEntityRenderers.register(FDBlockEntities.TEST.get(), FDBlockEntityRendererBuilder.builder()
                            .addLayer(FDBlockRenderLayerOptions.builder()
                                    .model(FDModels.TEST)
                                    .renderType(RenderType.entityCutout(FDLib.location("textures/texture.png")))
                                    .build())
                            .addLayer(FDBlockRenderLayerOptions.builder()
                                    .model(FDModels.TEST2)
                                    .renderType(RenderType.eyes(FDLib.location("textures/texture.png")))
                                    .transformation(((blockEntity, stack, partialTicks) -> {
                                        stack.mulPose(Axis.YP.rotationDegrees(90));
                                        stack.mulPose(Axis.XP.rotationDegrees(90));
                                    }))
                                    .renderCondition((entity -> {
                                        if (entity.getLevel() == null) return false;
                                        return entity.getLevel().getGameTime() % 60 > 10;
                                    }))
                                    .build())
                    .build());



    }

}
