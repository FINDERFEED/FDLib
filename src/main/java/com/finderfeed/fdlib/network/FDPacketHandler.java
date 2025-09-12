package com.finderfeed.fdlib.network;


import com.finderfeed.fdlib.FDLib;
import com.finderfeed.fdlib.FDHelpers;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.fml.util.thread.EffectiveSide;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

public class FDPacketHandler {

    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            FDLib.location("main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );
    private static int ID = 0;
    public static int nextID() {
        return ID++;
    }

    private static <T extends FDPacket> void registerPacket(Class<T> packetClass) throws NoSuchMethodException {
        Constructor<?> constructor = packetClass.getConstructor(FriendlyByteBuf.class);
        INSTANCE.registerMessage(nextID(), packetClass, FDPacket::write,(buf)->{
            try {
                return (T) constructor.newInstance(buf);
            } catch (InstantiationException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        },(msg, ctx)->{
            if (EffectiveSide.get().isClient()){
                msg.clientAction(ctx);
            }else{
                msg.serverAction(ctx);
            }

            ctx.get().setPacketHandled(true);
        });
    }

    public static void registerMessages() {

        List<Class<?>> packets = FDHelpers.getAnnotatedClasses(RegisterFDPacket.class);
        for (Class<?> clazz : packets){

            Class<? extends FDPacket> castedPacket = (Class<? extends FDPacket>) clazz;

            try {
                registerPacket(castedPacket);
            }catch (Exception e){
                throw new RuntimeException(e);
            }
        }

    }


}
