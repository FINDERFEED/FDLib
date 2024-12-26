package com.finderfeed.fdlib.to_other_mod.init;

import com.finderfeed.fdlib.FDLib;
import com.finderfeed.fdlib.to_other_mod.effects.ChesedGazeEffect;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.neoforged.neoforge.registries.DeferredRegister;

public class BossEffects {

    public static final DeferredRegister<MobEffect> MOB_EFFECTS = DeferredRegister.create(Registries.MOB_EFFECT, FDLib.MOD_ID);

    public static final Holder<MobEffect> CHESED_GAZE = MOB_EFFECTS.register("chesed_gaze", ChesedGazeEffect::new);


}
