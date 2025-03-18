package com.finderfeed.fdlib.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.HashMap;

public abstract class FDPacket implements CustomPacketPayload {

    public static final HashMap<String,Type<FDPacket>> REGISTERED_TYPES = new HashMap<>();

    private Type<? extends FDPacket> type;

    public abstract void write(RegistryFriendlyByteBuf buf);

    public abstract void clientAction(IPayloadContext context);
    public abstract void serverAction(IPayloadContext context);


    public Type<? extends FDPacket> type(){
        if (this.type == null) {
            RegisterFDPacket packet = this.getClass().getAnnotation(RegisterFDPacket.class);
            if (packet == null) throw new RuntimeException("Packet " + this.getClass().getName() + " doesn't have @RegisterFDPacket annotation");
            this.type = typeFor(packet);
            return this.type;
        }else{
            return this.type;
        }
    }

    public static Type<FDPacket> typeFor(RegisterFDPacket packet){
        ResourceLocation loc = ResourceLocation.tryParse(packet.value());
        String locString = loc.toString();
        Type<FDPacket> t = REGISTERED_TYPES.get(locString);
        if (t == null) {
            Type<FDPacket> type = new Type<>(loc);
            REGISTERED_TYPES.put(locString, type);
            t = type;
        }
        return t;
    }




}