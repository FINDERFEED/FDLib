package com.finderfeed.fdlib.systems.bedrock.animations.animation_system.entity.head;

import com.finderfeed.fdlib.FDClientHelpers;
import com.finderfeed.fdlib.network.FDPacket;
import com.finderfeed.fdlib.network.RegisterFDPacket;
import com.finderfeed.fdlib.systems.bedrock.animations.animation_system.AnimatedObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Mob;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;


@RegisterFDPacket("fdlib:head_controller_change_mode")
public class ChangeHeadControllerModePacket<T extends Mob & AnimatedObject  & IHasHead<T>> extends FDPacket {

    private HeadControllerContainer.Mode mode;
    private int entityId;

    public ChangeHeadControllerModePacket(T entity, HeadControllerContainer.Mode mode){
        this.entityId = entity.getId();
        this.mode = mode;
    }

    public ChangeHeadControllerModePacket(FriendlyByteBuf buf){
        this.entityId = buf.readInt();
        this.mode = buf.readEnum(HeadControllerContainer.Mode.class);
    }

    @Override
    public void write(FriendlyByteBuf FriendlyByteBuf) {
        FriendlyByteBuf.writeInt(entityId);
        FriendlyByteBuf.writeEnum(mode);
    }

    @Override
    public void clientAction(Supplier<NetworkEvent.Context> ctx) {
        if (FDClientHelpers.getClientLevel().getEntity(entityId) instanceof Mob mob){
            T e = (T) mob;
            e.getHeadControllerContainer().setControllersMode(mode);
        }
    }

    @Override
    public void serverAction(Supplier<NetworkEvent.Context> ctx) {

    }
}
