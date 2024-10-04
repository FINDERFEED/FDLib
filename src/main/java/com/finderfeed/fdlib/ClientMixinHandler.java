package com.finderfeed.fdlib;

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
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@EventBusSubscriber(modid = FDLib.MOD_ID,bus = EventBusSubscriber.Bus.GAME,value = Dist.CLIENT)
public class ClientMixinHandler {


    private static Vec3 shakePos = null;

    public static int shakeTime = 0;
    public static int dur = 0;


    @SubscribeEvent
    public static void clientTick(ClientTickEvent.Post event){

        if (dur == 0) {
            shakeTime = Mth.clamp(shakeTime + 1, 0, 10);
            if (shakeTime == 10) {
                dur = 20;
                shakeTime = 0;
            }
        }else{
            dur = Mth.clamp(dur - 1,0,Integer.MAX_VALUE);
        }

    }


    public static void bobHurt(PoseStack matrices,float pticks){
        if (true) return;
        if (Minecraft.getInstance().level == null) return;

        if (dur > 0) return;
        float p = (Mth.clamp(shakeTime + pticks,0,10)) / 10f;
        p = FDEasings.easeOut(p);

        float s = p * FDMathUtil.FPI * 2 * 5;

        matrices.translate(-20f,0,0);
        matrices.mulPose(Axis.ZP.rotation((float)Math.sin(s) * FDMathUtil.FPI / 160));
        matrices.translate(20f,0,0);


    }

}
