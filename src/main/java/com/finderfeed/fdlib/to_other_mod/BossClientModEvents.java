package com.finderfeed.fdlib.to_other_mod;

import com.finderfeed.fdlib.FDLib;
import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.entity.renderer.FDEntityRendererBuilder;
import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.entity.renderer.FDRenderLayerOptions;
import com.finderfeed.fdlib.to_other_mod.client.BossParticles;
import com.finderfeed.fdlib.to_other_mod.client.particles.arc_lightning.ArcLightningParticle;
import com.finderfeed.fdlib.to_other_mod.client.particles.ball_particle.BallParticle;
import com.finderfeed.fdlib.to_other_mod.client.particles.chesed_attack_ray.ChesedAttackRayParticle;
import com.finderfeed.fdlib.to_other_mod.client.particles.smoke_particle.BigSmokeParticle;
import com.finderfeed.fdlib.to_other_mod.client.particles.sonic_particle.SonicParticle;
import com.finderfeed.fdlib.to_other_mod.entities.chesed_boss.chesed_crystal.ChesedCrystalEntity;
import com.finderfeed.fdlib.to_other_mod.entities.chesed_boss.earthshatter_entity.EarthShatterRenderer;
import com.finderfeed.fdlib.to_other_mod.entities.chesed_boss.falling_block.ChesedFallingBlockRenderer;
import com.finderfeed.fdlib.to_other_mod.entities.chesed_boss.flying_block_entity.FlyingBlockEntityRenderer;
import com.finderfeed.fdlib.to_other_mod.init.BossEntities;
import com.finderfeed.fdlib.to_other_mod.init.BossModels;
import com.finderfeed.fdlib.to_other_mod.projectiles.renderers.BlockProjectileRenderer;
import com.finderfeed.fdlib.util.client.NullEntityRenderer;
import com.finderfeed.fdlib.util.rendering.FDRenderUtil;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.Mth;
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
        event.registerSpriteSet(BossParticles.SONIC_PARTICLE.get(), SonicParticle.Factory::new);
        event.registerSpriteSet(BossParticles.BALL_PARTICLE.get(), BallParticle.Factory::new);
        event.registerSpriteSet(BossParticles.BIS_SMOKE.get(), BigSmokeParticle.Factory::new);
    }

    @SubscribeEvent
    public static void addRenderers(EntityRenderersEvent.RegisterRenderers event){
        event.registerEntityRenderer(BossEntities.CHESED.get(),
                FDEntityRendererBuilder.builder()
                        .shouldRender(((entity, frustum, x, y, z) -> {
                            return true;
                        }))
                        .addLayer(FDRenderLayerOptions.builder()
                                .model(BossModels.CHESED)
                                .renderType(RenderType.entityCutout(FDLib.location("textures/entities/chesed.png")))
                                .build()
                        )
                        .build()
        );
        event.registerEntityRenderer(BossEntities.CHESED_ELECTRIC_SPHERE.get(),FDEntityRendererBuilder.builder()
                        .addLayer(FDRenderLayerOptions.builder()
                                .model(BossModels.CHESED_ELECTRIC_SPHERE)
                                .transformation((entity,matrices,pticks)->{
                                    float time = entity.tickCount + pticks;
                                    float md = 16;
                                    float scale = Mth.clamp(time / 20f,0,1) * ((float)Math.sin(time * 2) / md + (1 - 1 / md));
                                    matrices.scale(scale,scale,scale);
                                })
                                .renderType(RenderType.entityTranslucentCull(FDLib.location("textures/entities/electric_orb.png")))
                                .build())
                .build());
        event.registerEntityRenderer(BossEntities.CHESED_CRYSTAL.get(),FDEntityRendererBuilder.builder()
                        .shouldRender(((entity, frustum, x, y, z) -> true))
                        .addLayer(FDRenderLayerOptions.builder()
                                .model(BossModels.CHESED_CRYSTAL)
                                .renderCondition((entity -> true))
                                .transformation(((entity, stack, partialTicks) -> {

                                    stack.mulPose(FDRenderUtil.rotationDegrees(FDRenderUtil.YP(),entity.getId() * 42.343f));
                                    FDRenderUtil.applyMovementMatrixRotations(stack,((ChesedCrystalEntity)entity).getCrystalFacingDirection());
                                    stack.scale(3.2f,3,3.2f);
                                }))
                                .renderType(RenderType.eyes(FDLib.location("textures/entities/chesed_crystal.png")))
                                .build())
                .build());

        event.registerEntityRenderer(BossEntities.EARTH_SHATTER.get(), EarthShatterRenderer::new);
        event.registerEntityRenderer(BossEntities.BLOCK_PROJECTILE.get(), BlockProjectileRenderer::new);
        event.registerEntityRenderer(BossEntities.CHESED_FALLING_BLOCK.get(), ChesedFallingBlockRenderer::new);
        event.registerEntityRenderer(BossEntities.FLYING_BLOCK.get(), FlyingBlockEntityRenderer::new);
        event.registerEntityRenderer(BossEntities.RADIAL_EARTHQUAKE.get(), NullEntityRenderer::new);
    }
}
