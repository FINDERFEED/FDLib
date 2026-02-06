package com.finderfeed.fdlib.systems.bedrock.animations.animation_system.model_system.attachments.instances.particle;

import com.finderfeed.fdlib.FDClientPacketExecutables;
import com.finderfeed.fdlib.network.FDPacket;
import com.finderfeed.fdlib.network.RegisterFDPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.UUID;

@RegisterFDPacket("fdlib:spawn_particle_block_entity_attachment")
public class SpawnParticleAtAttachmentBlockEntityPacket extends FDPacket {

    private ParticleOptions particleOptions;
    private int layerId;
    private String boneId;
    private UUID uuid;
    private BlockPos blockpos;

    public SpawnParticleAtAttachmentBlockEntityPacket(ParticleOptions particleOptions, int layerId, String boneId, UUID attachmentId, BlockPos blockPos){
        this.particleOptions = particleOptions;
        this.layerId = layerId;
        this.boneId = boneId;
        this.uuid = attachmentId;
        this.blockpos = blockPos;
    }

    public SpawnParticleAtAttachmentBlockEntityPacket(RegistryFriendlyByteBuf buf){
        this.particleOptions = ParticleTypes.STREAM_CODEC.decode(buf);
        this.layerId = buf.readInt();
        this.boneId = buf.readUtf();
        this.uuid = buf.readUUID();
        this.blockpos = buf.readBlockPos();
    }

    @Override
    public void write(RegistryFriendlyByteBuf buf) {
        ParticleTypes.STREAM_CODEC.encode(buf, particleOptions);
        buf.writeInt(this.layerId);
        buf.writeUtf(this.boneId);
        buf.writeUUID(uuid);
        buf.writeBlockPos(blockpos);
    }

    @Override
    public void clientAction(IPayloadContext context) {
        FDClientPacketExecutables.spawnParticleAtBlockEntityAttachmentPacket(particleOptions, layerId, boneId, uuid, blockpos);
    }

    @Override
    public void serverAction(IPayloadContext context) {

    }
}
