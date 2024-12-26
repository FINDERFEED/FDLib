package com.finderfeed.fdlib.to_other_mod.init;

import com.finderfeed.fdlib.FDLib;
import com.finderfeed.fdlib.systems.FDRegistries;
import com.finderfeed.fdlib.systems.hud.bossbars.FDBossBarFactory;
import com.finderfeed.fdlib.to_other_mod.entities.chesed_boss.ChesedBossBar;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class BossBars {

    public static final DeferredRegister<FDBossBarFactory<?>> BOSS_BARS = DeferredRegister.create(FDRegistries.FD_BOSS_BARS, FDLib.MOD_ID);

    public static final DeferredHolder<FDBossBarFactory<?>,FDBossBarFactory<ChesedBossBar>> CHESED_BOSS_BAR = BOSS_BARS.register("chesed_boss_bar",()->ChesedBossBar::new);

}
