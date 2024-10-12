package com.finderfeed.fdlib.to_other_mod;

import com.finderfeed.fdlib.FDLib;
import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.entity.renderer.FDEntityRendererBuilder;
import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.entity.renderer.FDRenderLayerOptions;
import com.finderfeed.fdlib.to_other_mod.client.BossParticles;
import com.finderfeed.fdlib.to_other_mod.client.particles.arc_lightning.ArcLightningParticle;
import com.finderfeed.fdlib.to_other_mod.client.particles.chesed_attack_ray.ChesedAttackRayParticle;
import com.finderfeed.fdlib.to_other_mod.client.particles.sonic_particle.SonicParticle;
import com.finderfeed.fdlib.to_other_mod.entities.earthshatter_entity.EarthShatterRenderer;
import com.finderfeed.fdlib.to_other_mod.entities.flying_block_entity.FlyingBlockEntityRenderer;
import com.finderfeed.fdlib.to_other_mod.projectiles.renderers.BlockProjectileRenderer;
import com.finderfeed.fdlib.util.client.NullEntityRenderer;
import net.minecraft.client.renderer.RenderType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD,value = Dist.CLIENT,modid = FDLib.MOD_ID)
public class BossClientModEvents {


    @SubscribeEvent
    public static void registerParticleFactories(RegisterParticleProvidersEvent event){
        event.registerSpecial(BossParticles.CHESED_RAY_ATTACK.get(), new ChesedAttackRayParticle.Factory());
        event.registerSpecial(BossParticles.ARC_LIGHTNING.get(), new ArcLightningParticle.Factory());
        event.registerSpriteSet(BossParticles.SONIC_PARTICLE.get(), SonicParticle.Provider::new);
    }

    @SubscribeEvent
    public static void addRenderers(EntityRenderersEvent.RegisterRenderers event){
        event.registerEntityRenderer(BossEntities.CHESED.get(),
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
        event.registerEntityRenderer(BossEntities.EARTH_SHATTER.get(), EarthShatterRenderer::new);
        event.registerEntityRenderer(BossEntities.BLOCK_PROJECTILE.get(), BlockProjectileRenderer::new);
        event.registerEntityRenderer(BossEntities.FLYING_BLOCK.get(), FlyingBlockEntityRenderer::new);
        event.registerEntityRenderer(BossEntities.RADIAL_EARTHQUAKE.get(), NullEntityRenderer::new);
    }
}
