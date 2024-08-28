package com.finderfeed.fdlib.network;


import com.finderfeed.fdlib.FDLib;
import com.finderfeed.fdlib.FDHelpers;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.util.thread.EffectiveSide;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

@EventBusSubscriber(modid = FDLib.MOD_ID,bus = EventBusSubscriber.Bus.MOD)
public class PacketHandler {

    @SubscribeEvent
    public static void autoRegisterPackets(RegisterPayloadHandlersEvent event){
        final PayloadRegistrar registrar = event.registrar(FDLib.MOD_ID)
                .versioned("1")
                .optional();

        List<Class<?>> packets = FDHelpers.getAnnotatedClasses(RegisterFDPacket.class);
        for (Class<?> clazz : packets){

            RegisterFDPacket annotation = clazz.getAnnotation(RegisterFDPacket.class);
            CustomPacketPayload.Type<FDPacket> type = FDPacket.typeFor(annotation);


            try {
                Constructor<?> constructor = clazz.getConstructor(FriendlyByteBuf.class);

                StreamCodec<? super FriendlyByteBuf,FDPacket> codec = StreamCodec.of(
                        (buf,payload)->{
                            payload.write(buf);
                        },
                        (buf)->{
                            try {
                                FDPacket o = (FDPacket) constructor.newInstance(buf);
                                return o;
                            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                                throw new RuntimeException(e);
                            }
                        }
                );

                registrar.commonBidirectional(type,codec,((payload, context) -> {
                    context.enqueueWork(()->{
                        if (EffectiveSide.get().isClient()){
                            payload.clientAction(context);
                        }else{
                            payload.serverAction(context);
                        }
                    });
                }));
            }catch (Exception e){
                throw new RuntimeException(e);
            }
        }
    }




}
