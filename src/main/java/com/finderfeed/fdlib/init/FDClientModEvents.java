package com.finderfeed.fdlib.init;

import com.finderfeed.fdlib.FDLib;
import com.finderfeed.fdlib.util.client.particles.FDTerrainParticle;
import com.finderfeed.fdlib.util.client.particles.InvisibleParticle;
import com.finderfeed.fdlib.util.client.particles.ball_particle.BallParticle;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;

@EventBusSubscriber(modid = FDLib.MOD_ID,bus = EventBusSubscriber.Bus.MOD,value = Dist.CLIENT)
public class FDClientModEvents {


    @SubscribeEvent
    public static void assignFactories(RegisterParticleProvidersEvent event){
        event.registerSpriteSet(FDParticles.INVISIBLE.get(),InvisibleParticle.Factory::new);
        event.registerSpriteSet(FDParticles.BALL_PARTICLE.get(),BallParticle.Factory::new);
        event.registerSpecial(FDParticles.TERRAIN_PARTICLE.get(), new FDTerrainParticle.Provider());
    }

}
