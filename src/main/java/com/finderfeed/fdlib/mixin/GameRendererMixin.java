package com.finderfeed.fdlib.mixin;

import com.finderfeed.fdlib.ClientMixinHandler;
import com.finderfeed.fdlib.systems.impact_frames.ImpactFramesHandler;
import com.finderfeed.fdlib.systems.post_shaders.FDRenderPostShaderEvent;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraftforge.common.MinecraftForge;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(GameRenderer.class)
public class GameRendererMixin {

    @Inject(method = "bobHurt",at = @At("HEAD"))
    public void bobHurt(PoseStack matrices, float pticks, CallbackInfo ci){
        ClientMixinHandler.bobHurt(matrices,pticks);
    }


    @Inject(method = "renderItemInHand",at = @At("HEAD"))
    public void renderItemInHand(PoseStack p_109121_, Camera p_109122_, float p_109123_, CallbackInfo ci){
        ClientMixinHandler.beforeHand();
    }

    @Inject(method = "renderLevel",at = @At("HEAD"))
    public void renderLevel(float p_109090_, long p_109091_, PoseStack p_109092_, CallbackInfo ci){
        ClientMixinHandler.beforeLevel();
    }

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/pipeline/RenderTarget;bindWrite(Z)V", shift = At.Shift.BEFORE))
    public void levelPostShaders(float pticks, long p_109095_, boolean p_109096_, CallbackInfo ci){
        MinecraftForge.EVENT_BUS.post(new FDRenderPostShaderEvent.Level(pticks));
    }

    @Inject(method = "render", at = @At("TAIL"))
    public void screenPostShaders(float pticks, long p_109095_, boolean p_109096_, CallbackInfo ci){
        MinecraftForge.EVENT_BUS.post(new FDRenderPostShaderEvent.Screen(pticks));
    }



}