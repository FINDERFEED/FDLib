package com.finderfeed.fdlib.systems.particle.particle_emitter;

import com.finderfeed.fdlib.FDLib;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.LinkedList;
import java.util.List;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE,value = Dist.CLIENT,modid = FDLib.MOD_ID)
public class ParticleEmitterHandler {


    private static List<ParticleEmitter> activeEmitters = new LinkedList<>();


    @SubscribeEvent
    public static void tickEmitters(TickEvent.LevelTickEvent event){

        if (event.phase != TickEvent.Phase.START) return;

        if (!event.level.isClientSide) return;

        var iterator = activeEmitters.listIterator();

        while (iterator.hasNext()){

            var emitter = iterator.next();
            if (emitter.removed){
                iterator.remove();
                continue;
            }

            emitter.tick();

        }

    }


    public static void addParticleEmitter(ParticleEmitterData data){
        ParticleEmitter emitter = new ParticleEmitter(data);
        activeEmitters.add(emitter);
        emitter.init();
    }
}
