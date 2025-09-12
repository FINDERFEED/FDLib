package com.finderfeed.fdlib.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.HashMap;
import java.util.function.Supplier;

public abstract class FDPacket {

    public abstract void write(FriendlyByteBuf buf);

    public abstract void clientAction(Supplier<NetworkEvent.Context> ctx);
    public abstract void serverAction(Supplier<NetworkEvent.Context> ctx);

}