package com.finderfeed.fdlib.systems.screen.screen_particles;

import net.minecraft.client.gui.screens.Screen;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.ICancellableEvent;

public abstract class ScreenParticlesRenderEvent extends Event implements ICancellableEvent {


    public static class Screen extends ScreenParticlesRenderEvent {

    }

    public static class Gui extends ScreenParticlesRenderEvent {

    }

}
