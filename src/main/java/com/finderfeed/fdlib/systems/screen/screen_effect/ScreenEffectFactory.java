package com.finderfeed.fdlib.systems.screen.screen_effect;

@FunctionalInterface
public interface ScreenEffectFactory<D extends ScreenEffectData, T extends ScreenEffect<D>> {

    T create(D data, int inTime, int stayTime, int outTime);


}
