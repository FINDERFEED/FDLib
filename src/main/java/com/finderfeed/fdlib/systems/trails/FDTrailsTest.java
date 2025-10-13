package com.finderfeed.fdlib.systems.trails;

import com.finderfeed.fdlib.FDLib;
import com.finderfeed.fdlib.systems.shapes.FD2DShape;
import com.finderfeed.fdlib.util.FDColor;
import com.finderfeed.fdlib.util.rendering.renderers.ShapeOnCurveRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderPlayerEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import org.joml.Vector3f;

//@EventBusSubscriber(modid = FDLib.MOD_ID, bus = EventBusSubscriber.Bus.GAME, value = Dist.CLIENT)
public class FDTrailsTest {

//    public static FDTrailDataGenerator<Player> trail;
//
//    @SubscribeEvent
//    public static void playerTickEvent(PlayerTickEvent.Pre tickEvent){
//
//        Player player = tickEvent.getEntity();
//
//        if (!player.level().isClientSide){
//            return;
//        }
//
//
//        if (trail == null){
//            trail = new FDTrailDataGenerator<>(Player::getPosition, 10, 0.001f);
//        }
//
//        trail.tick(player);
//
//
//    }
//
//    @SubscribeEvent
//    public static void renderPlayerEvent(RenderPlayerEvent.Pre event){
//
//        var src = event.getMultiBufferSource();
//        var matrices = event.getPoseStack();
//
//        var object = event.getEntity();
//
//        var partialTicks = event.getPartialTick();
//
//        FDTrailRenderer.renderTrail(object, trail, src.getBuffer(RenderType.lightning()),
//                matrices,
//                1f,
//                6,
//                30,
//                partialTicks,
//                new FDColor(1f,1f,1f,1f),
//                new FDColor(1f,1f,1f,1f)
//        );
//
//
//    }


}
