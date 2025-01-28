package com.finderfeed.fdlib.systems.bedrock.animations.animation_system;

public interface RenderFunction<T,E> {

    E getValue(T object, float partialTicks);

}
