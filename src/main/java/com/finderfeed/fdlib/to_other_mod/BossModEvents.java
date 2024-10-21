package com.finderfeed.fdlib.to_other_mod;

import com.finderfeed.fdlib.FDLib;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD,modid = FDLib.MOD_ID)
public class BossModEvents {

    @SubscribeEvent
    public static void setupEvent(FMLCommonSetupEvent event){

        event.enqueueWork(()->{
        });

    }

}
