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
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import org.joml.AxisAngle4f;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;
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
        if (Minecraft.getInstance().level == null) return;
        if (Minecraft.getInstance().isPaused()) return;
        if (Minecraft.getInstance().player == null) return;
        if (dur > 0) return;

        Camera camera = Minecraft.getInstance().gameRenderer.getMainCamera();

        var player = Minecraft.getInstance().player;
        shakePos = camera.getPosition().add(10,10,10);
        Vec3 cameraPos = camera.getPosition();
        Vec3 look = Minecraft.getInstance().player.getLookAngle();
        Vec3 left = new Vec3(0,1,0).cross(look);
        Vec3 eyePos = cameraPos;
        Vec3 b = shakePos.subtract(eyePos);
        Vec3 proj = FDMathUtil.projectVectorOntoPlane(b,look);
        Vec3 up = look.cross(left);
        double angle = FDMathUtil.angleBetweenVectors(up,proj);
        if (Double.isNaN(angle)){
            angle = 0;
        }


        float p = (Mth.clamp(shakeTime + pticks,0,10)) / 10f;
        p = FDEasings.easeOut(p);

        float s = p * FDMathUtil.FPI * 2 * 5;

        if (proj.dot(left) < 0){
            angle = -angle;
        }

        Vector3f axis = new Vector3f(0,1,0).rotateZ((float)(angle + FDMathUtil.FPI / 2));

        matrices.mulPose(new Quaternionf(new AxisAngle4f((float)Math.sin(s) * FDMathUtil.FPI / 80,(float)axis.x,axis.y,axis.z)));


    }

}
