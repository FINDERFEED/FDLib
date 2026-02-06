package com.finderfeed.fdlib.systems.bedrock.animations.animation_system.model_system.attachments.instances.particle;

import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.model_system.attachments.ModelAttachment;
import net.minecraft.client.particle.Particle;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import org.jetbrains.annotations.UnknownNullability;
import org.joml.Vector3f;

import java.util.List;

public class FDParticleAttachment implements ModelAttachment<FDParticleAttachment, FDParticleAttachmentData> {

    private FDParticleAttachmentData data;
    private ParticleContainer particleContainer = new ParticleContainer();

    public FDParticleAttachment(){

    }

    public FDParticleAttachment(FDParticleAttachmentData data) {
        this.data = data;
    }

    public ParticleContainer getParticleContainer() {
        return particleContainer;
    }

    @Override
    public FDParticleAttachmentData attachmentData() {
        return data;
    }

    @Override
    public CompoundTag serializeNBT(HolderLookup.Provider provider) {
        CompoundTag compoundTag = new CompoundTag();
        var offset = data.offset;
        compoundTag.putFloat("x", offset.x);
        compoundTag.putFloat("y", offset.y);
        compoundTag.putFloat("z", offset.z);
        return compoundTag;
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag compoundTag) {
        this.data = new FDParticleAttachmentData(
                new Vector3f(compoundTag.getFloat("x"),
                compoundTag.getFloat("y"),
                compoundTag.getFloat("z"))
        );
    }

}
