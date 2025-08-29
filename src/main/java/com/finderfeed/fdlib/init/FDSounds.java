package com.finderfeed.fdlib.init;

import com.finderfeed.fdlib.FDLib;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class FDSounds {

    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(Registries.SOUND_EVENT, FDLib.MOD_ID);

    public static final Supplier<SoundEvent> MALKUTH_THEME_INTRO_TEST = SOUNDS.register("malkuth_theme_intro_test", ()->SoundEvent.createVariableRangeEvent(FDLib.location("malkuth_theme_intro_test")));
    public static final Supplier<SoundEvent> MALKUTH_THEME_MAIN_TEST = SOUNDS.register("malkuth_theme_main_test", ()->SoundEvent.createVariableRangeEvent(FDLib.location("malkuth_theme_main_test")));

}
