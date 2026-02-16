package com.finderfeed.fdlib.systems.bedrock.animations.animation_system.entity.head;

import com.finderfeed.fdlib.FDClientHelpers;
import com.finderfeed.fdlib.network.FDPacket;
import com.finderfeed.fdlib.network.RegisterFDPacket;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.control.LookControl;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.handling.IPayloadContext;

@RegisterFDPacket("fdlib:look_straight_packet")
public class LookStraightPacket extends FDPacket {

    private int entityId;

    public LookStraightPacket(RegistryFriendlyByteBuf buf){
        this.entityId = buf.readInt();
    }

    public LookStraightPacket(Mob entity){
        this.entityId = entity.getId();
    }

    @Override
    public void write(RegistryFriendlyByteBuf registryFriendlyByteBuf) {
        registryFriendlyByteBuf.writeInt(entityId);
    }

    @Override
    public void clientAction(IPayloadContext iPayloadContext) {
        if (FDClientHelpers.getClientLevel().getEntity(entityId) instanceof Mob mob){
            LookControl lookControl = mob.getLookControl();
            Vec3 eyePosition = mob.getEyePosition();
            Vec3 faraway = eyePosition.add(mob.getLookAngle().scale(100));
            lookControl.setLookAt(faraway.x,faraway.y,faraway.z);
        }
    }

    @Override
    public void serverAction(IPayloadContext iPayloadContext) {

    }
}