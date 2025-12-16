package com.finderfeed.fdlib.init;

import com.finderfeed.fdlib.FDLib;
import com.finderfeed.fdlib.systems.FDRegistries;
import com.finderfeed.fdlib.systems.screen.screen_effect.ScreenEffectType;
import com.finderfeed.fdlib.systems.screen.screen_effect.instances.ScreenColorEffect;
import com.finderfeed.fdlib.systems.screen.screen_effect.instances.chromatic_abberation.ChromaticAbberationData;
import com.finderfeed.fdlib.systems.screen.screen_effect.instances.chromatic_abberation.ChromaticAbberationScreenEffect;
import com.finderfeed.fdlib.systems.screen.screen_effect.instances.datas.ScreenColorData;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class FDScreenEffects {

    public static final DeferredRegister<ScreenEffectType<?,?>> SCREEN_EFFECT_TYPES = DeferredRegister.create(FDRegistries.SCREEN_EFFECTS, FDLib.MOD_ID);

    public static final Supplier<ScreenEffectType<ScreenColorData, ScreenColorEffect>> SCREEN_COLOR = SCREEN_EFFECT_TYPES.register("screen_color",()->{
        return ScreenEffectType.create(ScreenColorEffect::new, ScreenColorData.STREAM_CODEC);
    });

    public static final Supplier<ScreenEffectType<ChromaticAbberationData, ChromaticAbberationScreenEffect>> CHROMATIC_ABBERATION = SCREEN_EFFECT_TYPES.register("chromatic_abberation",()->{
        return ScreenEffectType.create(ChromaticAbberationScreenEffect::new, ChromaticAbberationData.STREAM_CODEC);
    });

}
