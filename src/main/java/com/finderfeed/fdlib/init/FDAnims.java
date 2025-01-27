package com.finderfeed.fdlib.init;

import com.finderfeed.fdlib.FDLib;
import com.finderfeed.fdlib.systems.FDRegistries;
import com.finderfeed.fdlib.systems.bedrock.animations.Animation;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class FDAnims {

    public static final DeferredRegister<Animation> ANIMATIONS = DeferredRegister.create(FDRegistries.ANIMATIONS, FDLib.MOD_ID);

    public static final Supplier<Animation> TEST = ANIMATIONS.register("testanim",()->new Animation(FDLib.location("test")));

}
