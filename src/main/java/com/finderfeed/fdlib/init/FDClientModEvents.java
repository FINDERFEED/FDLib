package com.finderfeed.fdlib.init;

import com.finderfeed.fdlib.FDLib;
import com.finderfeed.fdlib.util.client.InvisibleParticle;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;

@EventBusSubscriber(modid = FDLib.MOD_ID,bus = EventBusSubscriber.Bus.MOD,value = Dist.CLIENT)
public class FDClientModEvents {


    @SubscribeEvent
    public static void assignFactories(RegisterParticleProvidersEvent event){
        event.registerSpriteSet(FDParticles.INVISIBLE.get(),InvisibleParticle.Factory::new);
    }

}
