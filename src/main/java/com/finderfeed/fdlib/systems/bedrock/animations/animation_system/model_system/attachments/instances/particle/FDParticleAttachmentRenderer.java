package com.finderfeed.fdlib.systems.bedrock.animations.animation_system.model_system.attachments.instances.particle;

import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.model_system.ModelSystem;
import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.model_system.attachments.ModelAttachmentRenderContext;
import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.model_system.attachments.ModelAttachmentRenderer;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class FDParticleAttachmentRenderer extends ModelAttachmentRenderer<FDParticleAttachment> {

    public static final FDParticleAttachmentRenderer INSTANCE = new FDParticleAttachmentRenderer();

    @Override
    public void render(FDParticleAttachment attachment, ModelAttachmentRenderContext ctx, ModelSystem modelSystem, PoseStack matrices, MultiBufferSource src, float partialTicks, int light, int overlay) {
        var particleContainer = attachment.getParticleContainer();

        var particles = particleContainer.getParticles();
        if (particles.isEmpty()) return;

        Camera camera = Minecraft.getInstance().gameRenderer.getMainCamera();

        var data = attachment.attachmentData();
        var offset = data.offset;


        matrices.pushPose();
        matrices.translate(offset.x,offset.y,offset.z);
        Matrix4f mat = matrices.last().pose();
        Vec3 vec = new Vec3(mat.transformPosition(new Vector3f()));
        var position = camera.getPosition();
        Vec3 worldPos = position.add(vec);

        List<Particle> particleList = new ArrayList<>();

        for (var particle : particles){
            particle.setPos(worldPos.x,worldPos.y,worldPos.z);
            particle.setParticleSpeed(0,0,0);
            if (!particle.isAlive()) {
                particleList.add(particle);
            }
        }

        particles.removeAll(particleList);


        matrices.popPose();

    }

}
