package com.finderfeed.fdlib.to_other_mod;

import com.finderfeed.fdlib.FDLib;
import com.finderfeed.fdlib.systems.FDRegistries;
import com.finderfeed.fdlib.systems.bedrock.animations.Animation;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class FDAnims {


    public static DeferredRegister<Animation> ANIMATIONS = DeferredRegister.create(FDRegistries.ANIMATIONS, FDLib.MOD_ID);

    public static DeferredHolder<Animation,Animation> CHESED_IDLE = ANIMATIONS.register("chesed_idle", ()->{
        return new Animation(ResourceLocation.tryBuild(FDLib.MOD_ID,"chesed"));
    });
    public static DeferredHolder<Animation,Animation> CHESED_ATTACK = ANIMATIONS.register("chesed_basic_attack", ()->{
        return new Animation(ResourceLocation.tryBuild(FDLib.MOD_ID,"chesed"));
    });
    public static DeferredHolder<Animation,Animation> CHESED_ROLL_UP = ANIMATIONS.register("chesed_roll_up", ()->{
        return new Animation(ResourceLocation.tryBuild(FDLib.MOD_ID,"chesed"));
    });
    public static DeferredHolder<Animation,Animation> CHESED_ROLL_UP_END = ANIMATIONS.register("chesed_roll_up_end", ()->{
        return new Animation(ResourceLocation.tryBuild(FDLib.MOD_ID,"chesed"));
    });
    public static DeferredHolder<Animation,Animation> CHESED_ROLL_UP_JUST_END /*_ALREADY YOU_ROLLING_BASTARD*/ = ANIMATIONS.register("chesed_roll_up_just_end", ()->{
        return new Animation(ResourceLocation.tryBuild(FDLib.MOD_ID,"chesed"));
    });
    public static DeferredHolder<Animation,Animation> CHESED_ROLL = ANIMATIONS.register("chesed_roll", ()->{
        return new Animation(ResourceLocation.tryBuild(FDLib.MOD_ID,"chesed"));
    });
    public static DeferredHolder<Animation,Animation> CHESED_ROLL_ROLL = ANIMATIONS.register("chesed_roll_roll", ()->{
        return new Animation(ResourceLocation.tryBuild(FDLib.MOD_ID,"chesed"));
    });
    public static DeferredHolder<Animation,Animation> CHESED_CAST = ANIMATIONS.register("chesed_cast", ()->{
        return new Animation(ResourceLocation.tryBuild(FDLib.MOD_ID,"chesed"));
    });
    public static DeferredHolder<Animation,Animation> CHESED_EARTHQUAKE_CAST = ANIMATIONS.register("chesed_earthquake_cast", ()->{
        return new Animation(ResourceLocation.tryBuild(FDLib.MOD_ID,"chesed"));
    });
    public static DeferredHolder<Animation,Animation> CHESED_ROCKFALL_CAST = ANIMATIONS.register("chesed_rockfall_cast", ()->{
        return new Animation(ResourceLocation.tryBuild(FDLib.MOD_ID,"chesed"));
    });

}
