package com.finderfeed.fdlib.systems.particle.particle_emitter;

import com.finderfeed.fdlib.FDLib;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.LevelTickEvent;

import java.util.LinkedList;
import java.util.List;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.GAME,value = Dist.CLIENT,modid = FDLib.MOD_ID)
public class ParticleEmitterHandler {


    private static List<ParticleEmitter> activeEmitters = new LinkedList<>();


    @SubscribeEvent
    public static void tickEmitters(LevelTickEvent.Pre event){

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
