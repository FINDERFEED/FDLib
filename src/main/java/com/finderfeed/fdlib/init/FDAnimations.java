package com.finderfeed.fdlib.init;

import com.finderfeed.fdlib.FDLib;
import com.finderfeed.fdlib.systems.FDRegistries;
import com.finderfeed.fdlib.systems.bedrock.animations.Animation;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class FDAnimations {


    public static DeferredRegister<Animation> ANIMATIONS = DeferredRegister.create(FDRegistries.ANIMATIONS, FDLib.MOD_ID);

    public static DeferredHolder<Animation,Animation> TEST2 = ANIMATIONS.register("uldera_crystal_idle", ()->{
        return new Animation(ResourceLocation.tryBuild(FDLib.MOD_ID,"uldera_crystal_animations"));
    });
    public static DeferredHolder<Animation,Animation> TEST3 = ANIMATIONS.register("uldera_crystal_attack_1",()->{
        return new Animation(ResourceLocation.tryBuild(FDLib.MOD_ID,"uldera_crystal_animations"));
    });
    public static DeferredHolder<Animation,Animation> TEST4 = ANIMATIONS.register("uld_test", ()->{
        return new Animation(ResourceLocation.tryBuild(FDLib.MOD_ID,"uldera_crystal_animations"));
    });



}
