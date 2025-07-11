package com.finderfeed.fdlib.init;

import com.finderfeed.fdlib.FDLib;
import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.tile.renderer.FDBlockEntityRendererBuilder;
import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.tile.renderer.FDBlockRenderLayerOptions;
import com.finderfeed.fdlib.systems.impact_frames.FDPostShadersReloadableResourceListener;
import com.finderfeed.fdlib.systems.render_types.FDRenderType;
import com.finderfeed.fdlib.systems.shapes.FD2DShape;
import com.finderfeed.fdlib.test.FDTestBlockEntity;
import com.finderfeed.fdlib.util.FDColor;
import com.finderfeed.fdlib.util.client.particles.FDTerrainParticle;
import com.finderfeed.fdlib.util.client.particles.InvisibleParticle;
import com.finderfeed.fdlib.util.client.particles.ball_particle.BallParticle;
import com.finderfeed.fdlib.util.client.particles.lightning_particle.LightningParticle;
import com.finderfeed.fdlib.util.math.FDMathUtil;
import com.finderfeed.fdlib.util.rendering.FDRenderUtil;
import com.finderfeed.fdlib.util.rendering.renderers.QuadRenderer;
import com.finderfeed.fdlib.util.rendering.renderers.ShapeOnCurveRenderer;
import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
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
import org.joml.*;
import org.lwjgl.glfw.GLFW;

import java.lang.Math;

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

                            .freeRender((blockEntity, pticks, matrices, src, light, overlay) -> {

                                VertexConsumer vertexConsumer = src.getBuffer(RenderType.entityCutoutNoCull(ResourceLocation.withDefaultNamespace("textures/block/magma.png")));
//                                VertexConsumer vertexConsumer = src.getBuffer(RenderType.lightning());

                                FD2DShape shape = FD2DShape.createSimpleCircleNVertexShape(1f, 5);

//                                FDRenderUtil.renderShapeOnCatmullromSpline(square, matrices, vertexConsumer, new FDColor(1,0,0,1), light, 10,
//                                        new Vector3f(),
//                                        new Vector3f(5,3,0),
//                                        new Vector3f(10,-2,0),
//                                        new Vector3f(15,-1,5),
//                                        new Vector3f(0,2,10)
//                                );

                                float time = (blockEntity.getLevel().getGameTime() + pticks);
                                float percent = (float) Math.sin(time / 50) / 2f + 0.5f;
                                float percent2 = (float) Math.sin(time / 50 - FDMathUtil.FPI / 8) / 2f + 0.5f;

                                ShapeOnCurveRenderer.start(vertexConsumer)
                                        .scalingFunction(v->{
                                            return (float) Math.sin(v  * FDMathUtil.FPI * 2 * 10 + time / 20f) / 4 + 1f;
                                        })
                                        .uModifier(10)
                                        .vModifier(2)
                                        .startPercent(percent2)
                                        .endPercent(percent)
                                        .shape(shape)
                                        .lod(100)
                                        .color(new FDColor(1,0,0,0.25f))
                                        .curvePositions(
                                                new Vector3f(0,10,0),
                                                new Vector3f(0,3,3),
                                                new Vector3f(10.1f,-2,0),
                                                new Vector3f(10,-1,5),
                                                new Vector3f(0,2,10),
                                                new Vector3f(-2,-4,0)
                                        )
                                        .pose(matrices)
                                        .render();

//                                Vector3f dir1 = new Vector3f(0.5f,0.5f,0);
//                                Vector3f dir2 = new Vector3f(-0.25f,0.5f,0);
//
//                                double angleY = Math.atan2(dir1.x,dir1.z);
//                                double angleX = Math.atan2(Math.sqrt(dir1.x*dir1.x + dir1.z*dir1.z),dir1.y);
//
//                                double angleYd = Math.atan2(dir2.x,dir2.z);
//                                double angleXd = Math.atan2(Math.sqrt(dir2.x*dir2.x + dir2.z*dir2.z),dir2.y);
//                                Quaternionf q11 = new Quaternionf(new AxisAngle4d(angleY,0,1,0));
//                                Quaternionf q12 = new Quaternionf(new AxisAngle4d(angleX,1,0,0));
//
//                                Quaternionf q21 = new Quaternionf(new AxisAngle4d(angleYd,0,1,0));
//                                Quaternionf q22 = new Quaternionf(new AxisAngle4d(angleXd,1,0,0));
//
//                                Quaternionf rotation1 = q11.mul(q12,new Quaternionf());
//
//                                Quaternionf rotation2 = new Quaternionf().rotationTo(dir1,dir2).mul(rotation1);
//
//
//                                matrices.pushPose();
//
//                                Matrix4f m = matrices.last().pose();
//
//                                Vector3f v1 = rotatePoint(rotation1, new Vector3f(-1,0,-1));
//                                Vector3f v2 = rotatePoint(rotation1, new Vector3f(1,0,-1));
//                                Vector3f v3 = rotatePoint(rotation1, new Vector3f(1,0,1));
//                                Vector3f v4 = rotatePoint(rotation1, new Vector3f(-1,0,1));
//
//                                Vector3f pv1 = rotatePoint(rotation2, new Vector3f(-1,0,-1));
//                                Vector3f pv2 = rotatePoint(rotation2, new Vector3f(1,0,-1));
//                                Vector3f pv3 = rotatePoint(rotation2, new Vector3f(1,0,1));
//                                Vector3f pv4 = rotatePoint(rotation2, new Vector3f(-1,0,1));
//
//                                vertexConsumer.addVertex(m, v4.x,v4.y,v4.z).setColor(1,1,1,1f);
//                                vertexConsumer.addVertex(m, v3.x,v3.y,v3.z).setColor(0,0,1,1f);
//                                vertexConsumer.addVertex(m, v2.x,v2.y,v2.z).setColor(0,1,0,1f);
//                                vertexConsumer.addVertex(m, v1.x,v1.y,v1.z).setColor(1,0,0,1f);
//
//                                vertexConsumer.addVertex(m, pv4.x,pv4.y + 2, pv4.z).setColor(1,1,1,1f);
//                                vertexConsumer.addVertex(m, pv3.x,pv3.y + 2, pv3.z).setColor(0,0,1,1f);
//                                vertexConsumer.addVertex(m, pv2.x,pv2.y + 2, pv2.z).setColor(0,1,0,1f);
//                                vertexConsumer.addVertex(m, pv1.x,pv1.y + 2, pv1.z).setColor(1,0,0,1f);
//

//                                matrices.popPose();
                            })
                    .build());



    }

    private static Vector3f rotatePoint(Quaternionf quaternionf, Vector3f point){
        return quaternionf.transform(point,new Vector3f());
    }

}
