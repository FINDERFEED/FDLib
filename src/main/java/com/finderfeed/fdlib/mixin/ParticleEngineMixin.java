package com.finderfeed.fdlib.mixin;

import com.finderfeed.fdlib.systems.particle.FDParticleRenderType;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.minecraft.client.Camera;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.culling.Frustum;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Predicate;

@Mixin(ParticleEngine.class)
public class ParticleEngineMixin {


    @Inject(method = "render(Lnet/minecraft/client/renderer/LightTexture;Lnet/minecraft/client/Camera;FLnet/minecraft/client/renderer/culling/Frustum;Ljava/util/function/Predicate;)V",
            at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/BufferUploader;drawWithShader(Lcom/mojang/blaze3d/vertex/MeshData;)V",shift = At.Shift.AFTER))
    public void render(LightTexture p_107339_, Camera p_107340_, float p_107341_, Frustum frustum, Predicate<ParticleRenderType> renderTypePredicate, CallbackInfo ci,@Local ParticleRenderType particlerendertype){
        if (particlerendertype instanceof FDParticleRenderType fdParticleRenderType){
            fdParticleRenderType.end();
        }
    }

}
