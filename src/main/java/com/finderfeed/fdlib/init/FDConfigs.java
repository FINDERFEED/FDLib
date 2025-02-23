package com.finderfeed.fdlib.init;

import com.finderfeed.fdlib.FDLib;
import com.finderfeed.fdlib.systems.FDRegistries;
import com.finderfeed.fdlib.systems.config.JsonConfig;
import com.finderfeed.fdlib.systems.config.test.TestConfig;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class FDConfigs {

    public static final DeferredRegister<JsonConfig> CONFIGS = DeferredRegister.create(FDRegistries.CONFIGS, FDLib.MOD_ID);

    public static final Supplier<TestConfig> TEST = CONFIGS.register("test", TestConfig::new);

}
