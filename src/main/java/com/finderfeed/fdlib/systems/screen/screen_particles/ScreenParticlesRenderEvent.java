package com.finderfeed.fdlib.systems.screen.screen_particles;

import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;

@Cancelable
public abstract class ScreenParticlesRenderEvent extends Event {


    public static class Screen extends ScreenParticlesRenderEvent {

    }

    public static class Gui extends ScreenParticlesRenderEvent {

    }

}
