package com.finderfeed.fdlib.to_other_mod.init;

import com.finderfeed.fdlib.FDLib;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class BossSounds {

    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(BuiltInRegistries.SOUND_EVENT, FDLib.MOD_ID);


    public static final Supplier<SoundEvent> CHESED_RAY = SOUNDS.register("ray_attack",()->SoundEvent.createVariableRangeEvent(ResourceLocation.tryBuild(FDLib.MOD_ID,"ray_attack")));
    public static final Supplier<SoundEvent> ROCKFALL = SOUNDS.register("rockfall",()->SoundEvent.createVariableRangeEvent(ResourceLocation.tryBuild(FDLib.MOD_ID,"rockfall")));
    public static final Supplier<SoundEvent> RUMBLING = SOUNDS.register("rumbling",()->SoundEvent.createVariableRangeEvent(ResourceLocation.tryBuild(FDLib.MOD_ID,"rumbling")));
    public static final Supplier<SoundEvent> CHESED_LIGHTNING_RAY = SOUNDS.register("lightning_attack",()->SoundEvent.createVariableRangeEvent(ResourceLocation.tryBuild(FDLib.MOD_ID,"lightning_attack")));


}
