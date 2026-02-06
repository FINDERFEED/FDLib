package com.finderfeed.fdlib.systems.bedrock.animations.animation_system.model_system.attachments.instances.particle;

import com.finderfeed.fdlib.FDClientPacketExecutables;
import com.finderfeed.fdlib.network.FDPacket;
import com.finderfeed.fdlib.network.RegisterFDPacket;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.UUID;

@RegisterFDPacket("fdlib:spawn_particle_entity_attachment")
public class SpawnParticleAtAttachmentEntityPacket extends FDPacket {

    private ParticleOptions particleOptions;
    private int layerId;
    private String boneId;
    private UUID uuid;
    private int entityId;

    public SpawnParticleAtAttachmentEntityPacket(ParticleOptions particleOptions, int layerId, String boneId, UUID attachmentId, int entityId){
        this.particleOptions = particleOptions;
        this.layerId = layerId;
        this.boneId = boneId;
        this.uuid = attachmentId;
        this.entityId = entityId;
    }

    public SpawnParticleAtAttachmentEntityPacket(RegistryFriendlyByteBuf buf){
        this.particleOptions = ParticleTypes.STREAM_CODEC.decode(buf);
        this.layerId = buf.readInt();
        this.boneId = buf.readUtf();
        this.uuid = buf.readUUID();
        this.entityId = buf.readInt();
    }

    @Override
    public void write(RegistryFriendlyByteBuf buf) {
        ParticleTypes.STREAM_CODEC.encode(buf, particleOptions);
        buf.writeInt(this.layerId);
        buf.writeUtf(this.boneId);
        buf.writeUUID(uuid);
        buf.writeInt(this.entityId);
    }

    @Override
    public void clientAction(IPayloadContext context) {
        FDClientPacketExecutables.spawnParticleAtEntityAttachmentPacket(particleOptions, layerId, boneId, uuid, entityId);
    }

    @Override
    public void serverAction(IPayloadContext context) {

    }
}
