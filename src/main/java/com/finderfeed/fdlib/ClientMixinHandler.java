package com.finderfeed.fdlib;

import com.finderfeed.fdlib.systems.shake.ScreenShake;
import com.finderfeed.fdlib.systems.shake.ScreenShakeInstance;
import com.finderfeed.fdlib.util.math.FDMathUtil;
import com.finderfeed.fdlib.util.rendering.FDEasings;
import com.finderfeed.fdlib.util.rendering.FDRenderUtil;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import org.joml.AxisAngle4f;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;


@EventBusSubscriber(modid = FDLib.MOD_ID,bus = EventBusSubscriber.Bus.GAME,value = Dist.CLIENT)
public class ClientMixinHandler {


    private static final List<ScreenShakeInstance> SHAKES = new ArrayList<>();

    @SubscribeEvent
    public static void clientTick(ClientTickEvent.Post event){

        var iter = SHAKES.iterator();
        while (iter.hasNext()){
            var inst = iter.next();
            if (!inst.hasEnded()){
                inst.tick();
            }else{
                iter.remove();
            }
        }

    }

    @SubscribeEvent
    public static void onLogoff(ClientPlayerNetworkEvent.LoggingOut event){
        SHAKES.clear();
    }

    private static boolean renderShake = true;

    public static void beforeLevel(){
        renderShake = true;
    }

    public static void beforeHand(){
        renderShake = false;
    }

    public static void bobHurt(PoseStack matrices,float pticks){
        if (!renderShake) return;
        if (Minecraft.getInstance().level == null) return;
        if (Minecraft.getInstance().isPaused()) return;
        if (Minecraft.getInstance().player == null) return;
        for (ScreenShakeInstance instance : SHAKES){
            var shake = instance.shake;
            var time = instance.currentTime;
            shake.process(matrices,time,pticks);
        }
    }

    public static void addShake(ScreenShake shake){
        SHAKES.add(new ScreenShakeInstance(shake));
    }

}
