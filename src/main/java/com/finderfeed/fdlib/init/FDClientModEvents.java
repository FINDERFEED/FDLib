package com.finderfeed.fdlib.init;

import com.finderfeed.fdlib.FDLib;
import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.tile.renderer.FDBlockEntityRendererBuilder;
import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.tile.renderer.FDBlockRenderLayerOptions;
import com.finderfeed.fdlib.systems.impact_frames.FDPostShadersReloadableResourceListener;
import com.finderfeed.fdlib.test.FDTestBlockEntity;
import com.finderfeed.fdlib.util.FDColor;
import com.finderfeed.fdlib.util.client.particles.FDTerrainParticle;
import com.finderfeed.fdlib.util.client.particles.InvisibleParticle;
import com.finderfeed.fdlib.util.client.particles.ball_particle.BallParticle;
import com.finderfeed.fdlib.util.client.particles.lightning_particle.LightningParticle;
import com.finderfeed.fdlib.util.rendering.renderers.QuadRenderer;
import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.math.Axis;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.RegisterClientReloadListenersEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import org.lwjgl.glfw.GLFW;

@EventBusSubscriber(modid = FDLib.MOD_ID,bus = EventBusSubscriber.Bus.MOD,value = Dist.CLIENT)
public class FDClientModEvents {

    public static final String FDLIB_KEY_CATEGORY = "fdlib.key_category";

    public static final KeyMapping END_CUTSCENE = new KeyMapping("fdlib.key.end_cutscene", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_J, FDLIB_KEY_CATEGORY);

    @SubscribeEvent
    public static void registerKeyMappings(RegisterKeyMappingsEvent event){
        event.register(END_CUTSCENE);
    }

    @SubscribeEvent
    public static void registerReloadListeners(RegisterClientReloadListenersEvent event){
        event.registerReloadListener(new FDPostShadersReloadableResourceListener());
    }

    @SubscribeEvent
    public static void assignFactories(RegisterParticleProvidersEvent event){
        event.registerSpriteSet(FDParticles.INVISIBLE.get(),InvisibleParticle.Factory::new);
        event.registerSpriteSet(FDParticles.BALL_PARTICLE.get(),BallParticle.Factory::new);
        event.registerSpecial(FDParticles.LIGHTNING_PARTICLE.get(), new LightningParticle.Factory());
        event.registerSpecial(FDParticles.TERRAIN_PARTICLE.get(), new FDTerrainParticle.Provider());
    }

    @SubscribeEvent
    public static void registerBERenderers(FMLClientSetupEvent event){

            BlockEntityRenderers.register(FDBlockEntities.TEST.get(), FDBlockEntityRendererBuilder.<FDTestBlockEntity>builder()
                            .addLayer(FDBlockRenderLayerOptions.<FDTestBlockEntity>builder()
                                    .model(FDModels.TEST)
                                    .renderType((entity,pticks)->{

                                        float t = entity.getLevel().getGameTime() % 10;

                                        if (t < 5){
                                            return RenderType.eyes(TextureAtlas.LOCATION_BLOCKS);
                                        }

                                        return RenderType.entityCutout(FDLib.location("textures/texture.png"));
                                    })
                                    .color(((object, partialTicks) -> {

                                        Level l = object.getLevel();
                                        float t = (l.getGameTime() + partialTicks) % 20;


                                        return new FDColor(t / 20,1,1,1);
                                    }))
                                    .build())
                            .addLayer(FDBlockRenderLayerOptions.<FDTestBlockEntity>builder()
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

                            .freeRender((blockEntity, pticks, matrices, src, light, overlay) -> {

                                long gametime = Minecraft.getInstance().level.getGameTime();
                                float time = (gametime + pticks) / 10;

                                QuadRenderer.start(src.getBuffer(RenderType.entityTranslucent(ResourceLocation.parse("minecraft:textures/block/prismarine.png"))))
                                        .pose(matrices)
                                        .light(light)
                                        .translate((float)Math.sin(time),3,(float)Math.cos(time))
                                        .direction(new Vec3(1,1,1))
                                        .rotationDegrees(time * 20)
                                        .offsetOnDirection((float)Math.sin(time) * 5f)
                                        .setAnimated(Math.round(time) % 4,4)
                                        .render();


                            })
                    .build());



    }

}
